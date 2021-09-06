package elearning.api.db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoDriverInformation;
import com.mongodb.client.MongoClient;
import com.mongodb.client.internal.MongoClientImpl;
import com.mongodb.internal.build.MongoDriverVersion;
import elearning.api.keymangement.KMSHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;

import static java.lang.String.format;
import static java.lang.System.getProperty;

@Configuration
@EnableMongoRepositories(basePackages = "elearning.api.repositories")
public class MongoConfig extends AbstractMongoClientConfiguration {


    @Value(value = "${spring.data.mongodb.database}")
    private String DB_DATABASE;
    @Value(value = "${spring.data.mongodb.uri}")
    private String DB_CONNECTION;
    @Autowired
    private KMSHandler kmsHandler;

    private MongoDriverInformation getMongoDriverInfo() {
        return MongoDriverInformation.builder()
                .driverName(MongoDriverVersion.NAME)
                .driverVersion(MongoDriverVersion.VERSION)
                .driverPlatform(format("Java/%s/%s", getProperty("java.vendor", "unknown-vendor"),
                        getProperty("java.runtime.version", "unknown-version")))
                .build();
    }

    private MongoClientSettings getMongoClientSettings() {

        kmsHandler.buildOrValidateVault();
        return MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(DB_CONNECTION))
                .build();
    }

    @Autowired
    private MappingMongoConverter mongoConverter;

    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mongoConverter);
    }

    @Override
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(
                Arrays.asList(new BinaryToBsonBinaryConverter(),
                        new BsonBinaryToBinaryConverter()));
    }


    @Override
    public MongoClient mongoClient() {
        kmsHandler.buildOrValidateVault();
        MongoClient mongoClient = new MongoClientImpl(getMongoClientSettings(), getMongoDriverInfo());
        return mongoClient;
    }

    @Override
    protected String getDatabaseName() {
        return DB_DATABASE;
    }

}

