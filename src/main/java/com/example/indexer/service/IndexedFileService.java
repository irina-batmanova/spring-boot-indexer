package com.example.indexer.service;

import com.example.indexer.dao.IndexedFileDao;
import com.example.indexer.model.IndexedFile;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class IndexedFileService {

    private final IndexedFileDao fileDao;

    public IndexedFileService(IndexedFileDao fileDao) {
        this.fileDao = fileDao;
    }

    public UUID saveFile(String fileName) {
        UUID uuid = UUID.randomUUID();
        IndexedFile f = new IndexedFile(uuid, fileName);
        return this.fileDao.saveFile(f);
    }
}
