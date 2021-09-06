package elearning.api.services;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import elearning.api.models.EncrypteUser;
import elearning.api.models.Formation;
import elearning.api.models.LoadFile;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FileService {

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private FormationService formationService;

    @Autowired
    private GridFsOperations operations;

    public String addFile(MultipartFile upload) throws IOException {

        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", upload.getSize());

        Object fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(), metadata);

        return fileID.toString();
    }

    public Formation addFile(MultipartFile upload, String id) throws IOException {

        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", upload.getSize());

        Object fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(), metadata);
        Formation formation = formationService.getById(id);
        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(fileID)));

        LoadFile loadFile = new LoadFile();

        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            loadFile.setId(fileID.toString());

            loadFile.setFilename(gridFSFile.getFilename());

            loadFile.setFileType(gridFSFile.getMetadata().get("_contentType").toString());

            loadFile.setFileSize(gridFSFile.getMetadata().get("fileSize").toString());

            loadFile.setFile(IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()));
        }

        formation.getDocuments().add(loadFile);
        formationService.save(formation);
        return formation;
    }

    public LoadFile downloadFile(String id) throws IOException {

        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));

        LoadFile loadFile = new LoadFile();

        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            loadFile.setFilename(gridFSFile.getFilename());

            loadFile.setFileType(gridFSFile.getMetadata().get("_contentType").toString());

            loadFile.setFileSize(gridFSFile.getMetadata().get("fileSize").toString());

            loadFile.setFile(IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()));
        }

        return loadFile;
    }

    public Formation removeFile(String id, String idf) throws IOException {
        Formation formation = formationService.getById(idf);
        Set<LoadFile> documents = formation.getDocuments().stream()
                .filter(d -> !d.getId().equals(id) || d.getId().equals(null)).collect(Collectors.toSet());

        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));

        LoadFile loadFile = new LoadFile();

        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            loadFile.setFilename(gridFSFile.getFilename());

            loadFile.setFileType(gridFSFile.getMetadata().get("_contentType").toString());

            loadFile.setFileSize(gridFSFile.getMetadata().get("fileSize").toString());

            loadFile.setFile(IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()));
        }
        documents.remove(loadFile);
        formation.setDocuments(documents);
        formationService.save(formation);
        return formation;
    }

}
