package elearning.api.services;

import com.mongodb.client.model.vault.EncryptOptions;
import elearning.api.controllers.AuthController;
import elearning.api.keymangement.KMSHandler;
import elearning.api.models.EncrypteUser;
import elearning.api.models.User;
import elearning.api.repositories.UserRepository;
import elearning.api.security.UserHelper;
import org.bson.BsonBinary;
import org.bson.BsonString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    KMSHandler kmsHandler;

    @Autowired
    UserHelper userHelper;

    public static final String DETERMINISTIC_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic";
    public static final String RANDOM_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Random";
    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BsonBinary userName = kmsHandler.getClientEncryption().encrypt(new BsonString(username), getEncryptOptions(DETERMINISTIC_ENCRYPTION_TYPE));
        EncrypteUser euser = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        User user = userHelper.getUser(euser);
        logger.info(user.getUsername());
        return UserDetailsImpl.build(user);
    }

    private EncryptOptions getEncryptOptions(String algorithm) {

        EncryptOptions encryptOptions = new EncryptOptions(algorithm);
        encryptOptions.keyId(new BsonBinary(kmsHandler.getEncryptionKeyUUID()));
        return encryptOptions;

    }

    public EncrypteUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        logger.info(currentPrincipalName);
        BsonBinary username = kmsHandler.getClientEncryption().encrypt(new BsonString(currentPrincipalName), getEncryptOptions(DETERMINISTIC_ENCRYPTION_TYPE));
        EncrypteUser euser = userRepository.findByUsername(username).get();
        return euser;
    }

    public boolean existsById(String idu) {
        return userRepository.existsById(idu);
    }

    public EncrypteUser getById(String idu) {
        return userRepository.findById(idu).get();
    }

    public void delete(String idu) {
        userRepository.deleteById(idu);
    }

    public List<EncrypteUser> getAll() {
        return userRepository.findAll();
    }

}
