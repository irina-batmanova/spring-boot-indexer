package com.example.indexer.dao;

import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class IndexInRAM implements IndexDao {
    private static ConcurrentHashMap<String, HashSet<UUID>> index = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<UUID, HashSet<String>> reversedIndex = new ConcurrentHashMap<>();

    @Override
    public void addToIndex(String pieceOfFile, UUID fileId) {
        String value = null;
        try {
            byte bytes[] = pieceOfFile.getBytes("ISO-8859-1");
            value = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(value);
        value = value.replaceAll("\\p{Punct}", "");
        String[] words = value.split(" ");
        if (!reversedIndex.contains(fileId)) {
            reversedIndex.put(fileId, new HashSet<>());
        }
        HashSet<String> wordsInFile = reversedIndex.get(fileId);
        for (String word : words) {
            String lowerWord = word.toLowerCase(Locale.ROOT);
            if (!index.contains(lowerWord)) {
                index.put(lowerWord, new HashSet<>());
            }
            HashSet<UUID> filesWithWord = index.get(lowerWord);
            wordsInFile.add(lowerWord);
            filesWithWord.add(fileId);
        }
    }

    @Override
    public HashSet<UUID> getFilesByWord(String word) {
        return index.get(word);
    }

    @Override
    public void deleteFromIndex(UUID fileId) {
        HashSet<String> words = reversedIndex.get(fileId);
        if (words == null) {
            return;
        }
        for (String word : words) {
            HashSet<UUID> filesWithWord = index.get(word);
            filesWithWord.remove(fileId);
        }
        reversedIndex.remove(fileId);
    }

}
