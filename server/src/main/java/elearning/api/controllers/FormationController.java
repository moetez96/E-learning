package elearning.api.controllers;

import com.mongodb.client.model.vault.EncryptOptions;
import elearning.api.keymangement.KMSHandler;
import elearning.api.models.EncrypteUser;
import elearning.api.models.Formation;
import elearning.api.models.LoadFile;
import elearning.api.models.Meet;
import elearning.api.payload.request.*;
import elearning.api.payload.response.MessageResponse;
import elearning.api.repositories.FormationRepository;
import elearning.api.repositories.UserRepository;
import elearning.api.security.UserHelper;
import elearning.api.services.*;
import org.bson.BsonBinary;
import org.bson.BsonString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.repository.query.Param;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/formation")
public class FormationController {
    @Autowired
    FormationService formationService;
    @Autowired
    FileService fileService;
    @Autowired
    InvitationService invitationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    Logger logger = LoggerFactory.getLogger(FormationController.class);
    @Autowired
    KMSHandler kmsHandler;

    @Autowired
    UserHelper userHelper;

    public static final String DETERMINISTIC_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic";
    public static final String RANDOM_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Random";

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(formationService.getAll());
    }

    @GetMapping("/allUsers")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userDetailsService.getAll());
    }

    @GetMapping("/allByType/{type}")
    public ResponseEntity<?> getByType(@PathVariable String type) {
        if (!formationService.existsByType(type)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Formation Doesn't exists"));
        }
        return ResponseEntity.ok(formationService.getByType(type));
    }

    @GetMapping("/allByTeacher/{id}")
    public ResponseEntity<?> getByTeacher(@PathVariable String id) {
        return ResponseEntity.ok(formationService.getByTeacher(id));
    }

    @PostMapping("/add")
    //public ResponseEntity<?> organiserFormation(@RequestBody FormationRequest formationRequest) {
    public ResponseEntity<?> organiserFormation(@RequestPart("type") String type,
                                                @RequestPart("name") String name,
                                                @RequestPart("startDate") String startDate,
                                                @RequestPart("endDate") String endDate,
                                                @RequestPart("picture") MultipartFile picture,
                                                @RequestPart("description") String description) throws IOException {
        if (formationService.existsByName(name)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Formation Already exists"));
        }
        String idP = fileService.addFile(picture);
        Formation formation = new Formation(
                type,
                name,
                startDate,
                endDate,
                description
        );
        LoadFile pic = fileService.downloadFile(idP);
        pic.setId(idP);
        formation.setPicture(pic);
        formationService.add(formation);
        return ResponseEntity.ok(formation);
    }

    /*@PostMapping("/addFile")
    public ResponseEntity<?> addFile(@RequestBody String title, MultipartFile document) throws IOException {
        fileService.addPhoto(title,document);
        return ResponseEntity.ok("done");
    }*/

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateFormation(@PathVariable String id, @RequestPart("type") String type,
                                             @RequestPart("name") String name,
                                             @RequestPart("startDate") String startDate,
                                             @RequestPart("endDate") String endDate,
                                             @RequestPart("picture") MultipartFile picture,
                                             @RequestPart("description") String description) throws IOException {
        if (!formationService.existsById(id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Formation Doesn't exists"));
        }
        if (!formationService.getFormationTeacher(id).getId().equals(userDetailsService.getCurrentUser().getId())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You're not Authorised to delete this formation"));
        }
        FormationRequest formationRequest = new FormationRequest();
        formationRequest.setName(name);
        formationRequest.setType(type);
        formationRequest.setDescription(description);
        formationRequest.setEndDate(endDate);
        formationRequest.setStartDate(startDate);
        String idP = fileService.addFile(picture);

        return ResponseEntity.ok(formationService.updateFormation(id, formationRequest, idP));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getFormation(@PathVariable String id) {
        if (!formationService.existsById(id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Formation Doesn't exists"));
        }
        return ResponseEntity.ok(formationService.getById(id));
    }

    @PostMapping("/participate/{id}")
    public ResponseEntity<?> participateFormation(@PathVariable String id) {
        if (!formationService.existsById(id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Formation Doesn't Exist"));
        }
        if (formationService.existsInFormation(id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User Already Participated"));
        }
        return ResponseEntity.ok(formationService.participateCurrentUser(id));
    }

    @PostMapping("/addComment")
    public ResponseEntity<?> addComment(@RequestBody CommentRequest commentRequest) {
        return ResponseEntity.ok(formationService.addComment(commentRequest));
    }

    @PostMapping("/removeComment/{idf}/{idc}")
    public ResponseEntity<?> addComment(@PathVariable String idf, @PathVariable String idc) {
        return ResponseEntity.ok(formationService.removeComment(idf, idc));
    }

    @PostMapping("/addMeet/{id}")
    public ResponseEntity<?> addMeet(@PathVariable String id, @RequestBody MeetRequest meetRequest) {

        if (!formationService.existsById(id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Formation Doesn't Exist"));
        }
        Meet meeting = new Meet();
        meeting.setLink(meetRequest.getLink());
        meeting.setDate(meetRequest.getDate());
        return ResponseEntity.ok(formationService.addMeeting(id, meeting));
    }

    @PostMapping("/upload/{id}")
    public ResponseEntity<?> upload(@PathVariable String id, @RequestParam("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(fileService.addFile(file, id), HttpStatus.OK);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String id) throws IOException {
        LoadFile loadFile = fileService.downloadFile(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(loadFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + loadFile.getFilename() + "\"")
                .body(new ByteArrayResource(loadFile.getFile()));
    }

    @GetMapping("/removeFile/{id}/{idf}")
    public ResponseEntity<?> removeFile(@PathVariable String id, @PathVariable String idf) throws IOException {
        return new ResponseEntity<>(fileService.removeFile(id, idf), HttpStatus.OK);

    }

    @PostMapping("/cancelParticipation/{id}")
    public ResponseEntity<?> cancelParticipationFormation(@PathVariable String id) {
        if (!formationService.existsById(id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Formation Doesn't Exist"));
        }
        if (!formationService.existsInFormation(id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User Doesn't exist in Participants"));
        }
        return ResponseEntity.ok(formationService.cancelparticipation(id));
    }

    @PostMapping("/removeParticipation/{id}/{uid}")
    public ResponseEntity<?> removeParticipationFormation(@PathVariable String id, @PathVariable String uid) {
        if (!formationService.existsById(id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Formation Doesn't Exist"));
        }
        if (!formationService.existsFormation(uid, id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User Doesn't exist in Participants"));
        }
        return ResponseEntity.ok(formationService.removeUser(uid, id));
    }

    @DeleteMapping("/deleteFormation/{id}")
    public ResponseEntity<?> deleteFormation(@PathVariable String id) {
        if (!formationService.getFormationTeacher(id).getId().equals(userDetailsService.getCurrentUser().getId())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You're not Authorised to delete this formation"));
        }
        formationService.deleteFormation(id);
        return ResponseEntity.ok("formation deleted");
    }

    @DeleteMapping("/deleteFormationByAdmin/{id}")
    public ResponseEntity<?> deleteFormationByAdmin(@PathVariable String id) {
        formationService.deleteFormation(id);
        return ResponseEntity.ok(formationService.getAll());
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        userDetailsService.delete(id);
        return ResponseEntity.ok(userDetailsService.getAll());
    }

    @PostMapping("/sendInvite/{idu}/{idf}")
    public ResponseEntity<?> sendInvite(@PathVariable String idu, @PathVariable String idf) {
        if (!formationService.existsById(idf)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Formation Doesn't Exist"));
        }
        if (!userDetailsService.existsById(idu)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User Doesn't Exist"));
        }
        return ResponseEntity.ok(invitationService.sendInvite(idu, userDetailsService.getCurrentUser().getId(), idf));
    }

    private EncryptOptions getEncryptOptions(String algorithm) {

        EncryptOptions encryptOptions = new EncryptOptions(algorithm);
        encryptOptions.keyId(new BsonBinary(kmsHandler.getEncryptionKeyUUID()));
        return encryptOptions;

    }
}
