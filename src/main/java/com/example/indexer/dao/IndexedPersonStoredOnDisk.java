package com.example.indexer.dao;

import com.example.indexer.model.IndexedFile;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IndexedPersonStoredOnDisk implements IndexedFileDao {

    // TODO: take dir name for files from config
    // TODO: create dir here?
//    String homedir = System.getProperty("user.home");
//    File file = new File(homedir + "indexer_data");
    private static ConcurrentHashMap<UUID, IndexedFile> filesList = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, ConcurrentHashMap<String, String>> index = new ConcurrentHashMap<>();

    @Override
    public int saveFileWithId(UUID id, IndexedFile indexedFile) {
        filesList.put(id, indexedFile);

        return 0;
    }
}
