package com.example.indexer.api;

import com.example.indexer.model.IndexedFile;
import com.example.indexer.service.IndexedFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    public ResponseEntity uploadToLocalFileSystem(@RequestParam("file") MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        UUID fileId = this.fileService.saveFile(fileName);
        String fileBasePath = "/Users/irinanifantova/indexer_data/";
        Path path = Paths.get(fileBasePath + fileId);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/files/")
                .path(String.valueOf(fileId)).path("/download/")
                .toUriString();
        return ResponseEntity.ok(fileDownloadUri);
    }

    @GetMapping("{fileName:.+}/download")
    public ResponseEntity downloadFileFromLocal(@PathVariable String fileName) {
        String fileBasePath = "/Users/irinanifantova/indexer_data/";
        Path path = Paths.get(fileBasePath + fileName);
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
