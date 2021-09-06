package elearning.api.security;

import com.mongodb.client.model.vault.EncryptOptions;
import elearning.api.keymangement.KMSHandler;
import elearning.api.models.EncrypteUser;
import elearning.api.models.User;
import org.bson.BsonBinary;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class UserHelper {

    @Autowired
    protected KMSHandler kmsHandler;

    public static final String DETERMINISTIC_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic";
    public static final String RANDOM_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Random";


    public EncrypteUser getEncrypedUser(User user) {
        EncrypteUser euser = new EncrypteUser(
                kmsHandler.getClientEncryption().encrypt(new BsonString(user.getUsername()), getEncryptOptions(DETERMINISTIC_ENCRYPTION_TYPE)),
                kmsHandler.getClientEncryption().encrypt(new BsonString(user.getEmail()), getEncryptOptions(DETERMINISTIC_ENCRYPTION_TYPE)),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getClasse(),
                user.getSpeciality(),
                user.getBirthDate().toString()
        );
        euser.setContract(user.getContract());
        euser.setCreationDate(user.getCreationDate());
        euser.setRoles(user.getRoles());
        return euser;
    }

    public User getUser(EncrypteUser euser) {

        User user = new User(
                kmsHandler.getClientEncryption().decrypt(euser.getUsername()).asString().getValue(),
                kmsHandler.getClientEncryption().decrypt(euser.getEmail()).asString().getValue(),
                euser.getPassword(),
                euser.getFirstName(),
                euser.getLastName(),
                euser.getClasse(),
                euser.getSpeciality(),
                euser.getBirthDate().toString()
        );
        user.setCreationDate(euser.getCreationDate());
        user.setRoles(euser.getRoles());
        user.setId(euser.getId());
        return user;
    }

    private EncryptOptions getEncryptOptions(String algorithm) {

        EncryptOptions encryptOptions = new EncryptOptions(algorithm);
        encryptOptions.keyId(new BsonBinary(kmsHandler.getEncryptionKeyUUID()));
        return encryptOptions;

    }
}
