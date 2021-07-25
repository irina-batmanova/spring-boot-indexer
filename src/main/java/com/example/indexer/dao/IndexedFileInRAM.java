package com.example.indexer.dao;

import com.example.indexer.model.IndexedFile;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Repository
public class IndexedFileInRAM implements IndexedFileDao {
    private static ConcurrentHashMap<UUID, IndexedFile> filesList = new ConcurrentHashMap<>();

    @Override
    public UUID saveFile(IndexedFile indexedFile) {
        filesList.put(indexedFile.getId(), indexedFile);
        return indexedFile.getId();
    }
}
