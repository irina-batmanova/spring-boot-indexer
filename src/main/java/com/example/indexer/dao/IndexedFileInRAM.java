package com.example.indexer.dao;

import com.example.indexer.model.IndexedFile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Repository
public class IndexedFileInRAM implements IndexedFileDao {
    private static ConcurrentHashMap<UUID, IndexedFile> files = new ConcurrentHashMap<>();

    @Override
    public UUID saveFile(IndexedFile indexedFile) {
        files.put(indexedFile.getId(), indexedFile);
        return indexedFile.getId();
    }

    @Override
    public IndexedFile getFile(UUID fileId) {
        return files.get(fileId);
    }

    @Override
    public ArrayList<IndexedFile> getFiles() {
        return new ArrayList<IndexedFile>(files.values());
    }

    @Override
    public void deleteFile(UUID fileId) {
        files.remove(fileId);
    }

}
