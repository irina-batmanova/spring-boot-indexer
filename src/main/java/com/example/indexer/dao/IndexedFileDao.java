package com.example.indexer.dao;

import com.example.indexer.model.IndexedFile;

import java.util.UUID;

public interface IndexedFileDao {

//    UUID saveFileWithId(UUID id, IndexedFile indexedFile);

    UUID saveFile(IndexedFile indexedFile);
}
