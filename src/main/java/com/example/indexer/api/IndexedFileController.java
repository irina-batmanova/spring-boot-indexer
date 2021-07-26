package com.example.indexer.api;
import com.example.indexer.model.IndexedFile;
import com.example.indexer.service.IndexedFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

@RequestMapping("api/v1/files")
@RestController
public class IndexedFileController {

    @Autowired
    public IndexedFileController(IndexedFileService fileService) {
        this.fileService = fileService;
    }

    private final IndexedFileService fileService;

    @PostMapping(headers=("content-type=multipart/*"))
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) {
        UUID fileId = this.fileService.saveFile(file);
        if (fileId == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to save file, check logs for details");
        }
        String endpointPath = this.getClass().getAnnotation(RequestMapping.class).value()[0] + "/";
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(endpointPath)
                .path(String.valueOf(fileId)).path("/download/")
                .toUriString();
        return ResponseEntity.ok(fileDownloadUri);
    }

    @GetMapping("{fileId:.+}/download")
    public ResponseEntity downloadFile(@PathVariable UUID fileId) {
        IndexedFile file = this.fileService.getFile(fileId);
        Path path = this.fileService.getFilePath(fileId);
        if (file == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "File not found"
            );
        }
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (
                MalformedURLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "File not found"
            );
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + file.getName() + "\"")
                .body(resource);

    }

    @GetMapping
    public ResponseEntity findFiles(@RequestParam(required = false) String word) {
        if (word == null) {
            ArrayList<IndexedFile> files = this.fileService.getFiles();
            return ResponseEntity.ok().body(files);
        }
        HashSet<UUID> filesWithWord = this.fileService.getFiles(word);
        ArrayList<IndexedFile> arr = new ArrayList<>();
        if (filesWithWord == null) {
            return ResponseEntity.ok().body(arr);
        }
        for (UUID id : filesWithWord) {
            arr.add(this.fileService.getFile(id));
        }
        return ResponseEntity.ok().body(arr);
    }

    @DeleteMapping
    public ResponseEntity deleteFile(@PathVariable UUID fileId) {
        Integer status = this.fileService.deleteFile(fileId);
        if (status != 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete" +
                    " file, check logs for details");
        }
        HashMap<String, String> resp = new HashMap<>();
        resp.put("message", "Successfully deleted file " + fileId);
        return ResponseEntity.ok().body(resp);
    }
}
