package elearning.api.controllers;

import com.mongodb.client.model.vault.EncryptOptions;
import elearning.api.keymangement.KMSHandler;
import elearning.api.models.*;
import elearning.api.payload.request.LoginRequest;
import elearning.api.payload.request.SignupRequest;
import elearning.api.payload.response.JwtResponse;
import elearning.api.payload.response.MessageResponse;
import elearning.api.repositories.RoleRepository;
import elearning.api.repositories.UserRepository;
import elearning.api.security.ContractService;
import elearning.api.security.UserHelper;
import elearning.api.security.jwt.JwtUtils;
import elearning.api.services.UserDetailsImpl;
import org.bson.BsonBinary;
import org.bson.BsonString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    KMSHandler kmsHandler;

    @Autowired
    UserHelper userHelper;

    @Autowired
    ContractService service;

    public static final String DETERMINISTIC_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic";
    public static final String RANDOM_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Random";
    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername()
                        ,
                        loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles,
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getClasse(),
                userDetails.getSpeciality(),
                userDetails.getBirthDate(),
                userDetails.getCreationDate()
        ));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getuser() {

        BsonBinary username = kmsHandler.getClientEncryption().encrypt(new BsonString("moetez223"), getEncryptOptions(DETERMINISTIC_ENCRYPTION_TYPE));
        EncrypteUser euser = userRepository.findByUsername(username).get();
        User user = userHelper.getUser(euser);
        return ResponseEntity.ok(new MessageResponse(user.getUsername() + " " + user.getEmail() + " " + user.getPassword() + " " + user.getRoles()));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws Exception {
        if (userRepository.existsByUsername(kmsHandler.getClientEncryption().encrypt(new BsonString(signUpRequest.getUsername()), getEncryptOptions(DETERMINISTIC_ENCRYPTION_TYPE)))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(kmsHandler.getClientEncryption().encrypt(new BsonString(signUpRequest.getEmail()), getEncryptOptions(DETERMINISTIC_ENCRYPTION_TYPE)))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        Contract newContract = new Contract();
        newContract.setFee(1);
        newContract.setFirstName(signUpRequest.getFirstName());
        newContract.setLastName(signUpRequest.getLastName());
        newContract = service.createContract(newContract);

        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getClasse(),
                signUpRequest.getSpeciality(),
                signUpRequest.getBirthDate());

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "teacher":
                        Role modRole = roleRepository.findByName(ERole.ROLE_TEACHER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        user.setContract(newContract);
        EncrypteUser euser = userHelper.getEncrypedUser(user);

        userRepository.save(euser);
        logger.info(euser.getRoles().toString());

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    private EncryptOptions getEncryptOptions(String algorithm) {

        EncryptOptions encryptOptions = new EncryptOptions(algorithm);
        encryptOptions.keyId(new BsonBinary(kmsHandler.getEncryptionKeyUUID()));
        return encryptOptions;

    }

}
