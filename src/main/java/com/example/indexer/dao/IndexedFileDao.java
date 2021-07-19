package com.example.indexer.dao;

import com.example.indexer.model.IndexedFile;

import java.util.UUID;

public interface IndexedFileDao {

    int saveFileWithId(UUID id, IndexedFile indexedFile);

    default int saveFile(IndexedFile indexedFile) {
        UUID id = UUID.randomUUID();
        return saveFileWithId(id, indexedFile);
    }
}
