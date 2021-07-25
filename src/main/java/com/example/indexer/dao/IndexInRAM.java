package com.example.indexer.dao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class IndexInRAM implements IndexDao {
    private static ConcurrentHashMap<String, HashSet<UUID>> index = new ConcurrentHashMap<>();

    @Override
    public void addToIndex(String pieceOfFile, UUID fileId) {
        String[] words = pieceOfFile.split(" ");
        for (String word : words) {
            if (!index.contains(word)) {
                index.put(word, new HashSet<>());
            }
            HashSet<UUID> filesWithWord = index.get(word);
            filesWithWord.add(fileId);
        }
    }

    @Override
    public HashSet<UUID> getFilesByWord(String word) {
        return index.get(word);
    }

}
