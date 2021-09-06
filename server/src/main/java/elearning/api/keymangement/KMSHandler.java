package elearning.api.keymangement;

import com.mongodb.ClientEncryptionSettings;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.vault.DataKeyOptions;
import com.mongodb.client.vault.ClientEncryption;
import com.mongodb.client.vault.ClientEncryptions;
import org.bson.BsonBinary;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class KMSHandler {


    Logger logger = LoggerFactory.getLogger(KMSHandler.class);
    @Value(value = "${spring.data.mongodb.uri}")
    private String DB_CONNECTION;
    @Value(value = "${spring.data.mongodb.key.vault.database}")
    private String KEY_VAULT_DATABASE;
    @Value(value = "${spring.data.mongodb.key.vault.collection}")
    private String KEY_VAULT_COLLECTION;
    @Value(value = "${spring.data.mongodb.kmsprovider}")
    private String KMS_PROVIDER;
    @Value(value = "${spring.data.mongodb.key.vault.name}")
    private String KEY_NAME;
    @Value(value = "${spring.data.mongodb.encryption.masterKeyPath}")
    private String MASTER_KEY_PATH;
    private String encryptionKeyBase64;
    private UUID encryptionKeyUUID;

    public String getEncryptionKeyBase64() {
        return encryptionKeyBase64;
    }

    public UUID getEncryptionKeyUUID() {
        return encryptionKeyUUID;
    }

    public void buildOrValidateVault() {

        if (doesEncryptionKeyExist()) {
            return;
        }
        DataKeyOptions dataKeyOptions = new DataKeyOptions();
        dataKeyOptions.keyAltNames(Arrays.asList(KEY_NAME));
        BsonBinary dataKeyId = getClientEncryption().createDataKey(KMS_PROVIDER, dataKeyOptions);

        this.encryptionKeyUUID = dataKeyId.asUuid();
        logger.debug("DataKeyID [UUID]{}", dataKeyId.asUuid());

        String base64DataKeyId = Base64.getEncoder().encodeToString(dataKeyId.getData());
        this.encryptionKeyBase64 = base64DataKeyId;
        logger.debug("DataKeyID [base64]: {}", base64DataKeyId);
    }

    private boolean doesEncryptionKeyExist() {

        MongoClient mongoClient = MongoClients.create(DB_CONNECTION);
        MongoCollection<Document> collection = mongoClient.getDatabase(KEY_VAULT_DATABASE).getCollection(KEY_VAULT_COLLECTION);

        Bson query = Filters.in("keyAltNames", KEY_NAME);
        Document doc = collection
                .find(query)
                .first();

        return Optional.ofNullable(doc)
                .map(o -> {
                    logger.debug("The Document is {}", doc);
                    this.encryptionKeyUUID = fromStandardBinaryUUID((Binary) o.get("_id"));
                    this.encryptionKeyBase64 = Base64.getEncoder().encodeToString(new BsonBinary(fromStandardBinaryUUID((Binary) o.get("_id"))).getData());
                    return true;
                })
                .orElse(false);


    }

    /**
     * Convert a UUID object to a Binary with a subtype 0x04
     */
    public static Binary toStandardBinaryUUID(java.util.UUID uuid) {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();

        byte[] uuidBytes = new byte[16];

        for (int i = 15; i >= 8; i--) {
            uuidBytes[i] = (byte) (lsb & 0xFFL);
            lsb >>= 8;
        }

        for (int i = 7; i >= 0; i--) {
            uuidBytes[i] = (byte) (msb & 0xFFL);
            msb >>= 8;
        }

        return new Binary((byte) 0x04, uuidBytes);
    }

    /**
     * Convert a Binary with a subtype 0x04 to a UUID object
     * Please note: the subtype is not being checked.
     */
    public static UUID fromStandardBinaryUUID(Binary binary) {
        long msb = 0;
        long lsb = 0;
        byte[] uuidBytes = binary.getData();

        for (int i = 8; i < 16; i++) {
            lsb <<= 8;
            lsb |= uuidBytes[i] & 0xFFL;
        }

        for (int i = 0; i < 8; i++) {
            msb <<= 8;
            msb |= uuidBytes[i] & 0xFFL;
        }

        return new UUID(msb, lsb);
    }


    private byte[] getMasterKey() {

        byte[] localMasterKey = new byte[96];

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(MASTER_KEY_PATH);
            fis.read(localMasterKey, 0, 96);
        } catch (Exception e) {
            logger.error("Error Initializing the master key");
        }
        return localMasterKey;
    }

    private Map<String, Map<String, Object>> getKMSMap() {
        Map<String, Object> keyMap = Stream.of(
                new AbstractMap.SimpleEntry<>("key", getMasterKey())
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return Stream.of(
                new AbstractMap.SimpleEntry<>(KMS_PROVIDER, keyMap)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public ClientEncryption getClientEncryption() {


        String keyVaultNamespace = KEY_VAULT_DATABASE + "." + KEY_VAULT_COLLECTION;
        ClientEncryptionSettings clientEncryptionSettings = ClientEncryptionSettings.builder()
                .keyVaultMongoClientSettings(MongoClientSettings.builder()
                        .applyConnectionString(new ConnectionString(DB_CONNECTION))
                        .build())
                .keyVaultNamespace(keyVaultNamespace)
                .kmsProviders(this.getKMSMap())
                .build();

        ClientEncryption clientEncryption = ClientEncryptions.create(clientEncryptionSettings);

        return clientEncryption;
    }
}
