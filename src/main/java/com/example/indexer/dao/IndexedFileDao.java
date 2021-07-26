package com.example.indexer.dao;

import com.example.indexer.model.IndexedFile;

import java.util.ArrayList;
import java.util.UUID;

public interface IndexedFileDao {

    UUID saveFile(IndexedFile indexedFile);

    IndexedFile getFile(UUID fileId);

    ArrayList<IndexedFile> getFiles();

    void deleteFile(UUID fileId);
}
