package com.example.indexer.dao;

import com.example.indexer.model.IndexedFile;

import java.util.UUID;

public interface IndexedFileDao {

    UUID saveFile(IndexedFile indexedFile);
}
