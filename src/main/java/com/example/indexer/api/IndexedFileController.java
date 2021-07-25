package com.example.indexer.api;
import com.example.indexer.service.IndexedFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    @PostMapping
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) {
        UUID fileId = this.fileService.saveFile(file);
        String endpointPath = this.getClass().getAnnotation(RequestMapping.class).value()[0] + "/";
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(endpointPath)
                .path(String.valueOf(fileId)).path("/download/")
                .toUriString();
        return ResponseEntity.ok(fileDownloadUri);
    }

    @GetMapping("{fileId:.+}/download")
    public ResponseEntity downloadFile(@PathVariable UUID fileId) {
        String fileBasePath = "/opt/indexer_data/";
        Path path = Paths.get(fileBasePath + fileId);
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }

    @GetMapping
    public ResponseEntity getFiles(@RequestParam String word) {
        String endpointPath = this.getClass().getAnnotation(RequestMapping.class).value()[0];
        ArrayList<String> fileDownloadUris = new ArrayList<>();
        HashSet<UUID> filesWithWord = this.fileService.getFiles(word);
        for (UUID fileId : filesWithWord) {
            fileDownloadUris.add(ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(endpointPath)
                    .path(String.valueOf(fileId)).path("/download/")
                    .toUriString());
        }
        return ResponseEntity.ok().body(fileDownloadUris);
    }

//    @DeleteMapping
//    public ResponseEntity deleteFile(@PathVariable String fileId) {
//        return ResponseEntity.ok().body({"ava":"ava"});
//    }
}
