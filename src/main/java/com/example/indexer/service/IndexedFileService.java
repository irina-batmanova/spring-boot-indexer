package com.example.indexer.service;

import com.example.indexer.dao.IndexDao;
import com.example.indexer.dao.IndexedFileDao;
import com.example.indexer.model.IndexedFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;


@Service
public class IndexedFileService {

    private final IndexedFileDao fileDao;
    private final IndexDao indexDao;
    @Value("${storage.path}")
    private String fileBasePath;

    public IndexedFileService(IndexedFileDao fileDao, IndexDao indexDao) {
        this.fileDao = fileDao;
        this.indexDao = indexDao;
    }

    public UUID saveFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        IndexedFile f = new IndexedFile(fileName);
        Path path = getFilePath(f.getId());
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try (BufferedReader br = Files.newBufferedReader(Paths.get(String.valueOf(path)),
                StandardCharsets.ISO_8859_1)) {
            for (String line = null; (line = br.readLine()) != null;) {
                this.indexDao.addToIndex(line, f.getId());
            }
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return this.fileDao.saveFile(f);
    }

    public HashSet<UUID> getFiles(String word) {
        return this.indexDao.getFilesByWord(word);
    }

    public ArrayList<IndexedFile> getFiles() {
        return this.fileDao.getFiles();
    }

    public IndexedFile getFile(UUID fileId) {
        return this.fileDao.getFile(fileId);
    }

    public Path getFilePath(UUID fileID) {
        return Paths.get(fileBasePath + fileID);
    }

    public Integer deleteFile(UUID fileId) {
        File f = new File(fileBasePath + fileId);
        if (!f.delete()) {
            return -1;
        }
        this.indexDao.deleteFromIndex(fileId);
        this.fileDao.deleteFile(fileId);
        return 0;
    }

}
