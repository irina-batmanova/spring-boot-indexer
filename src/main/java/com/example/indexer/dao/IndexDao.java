package com.example.indexer.dao;


import java.util.HashSet;
import java.util.UUID;

public interface IndexDao {

    void addToIndex(String pieceOfFile, UUID fileId);

    HashSet<UUID> getFilesByWord(String word);

    void deleteFromIndex(UUID fileId);

    HashSet<String> getWordsInFile(UUID fileId);

}
