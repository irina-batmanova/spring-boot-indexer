package com.example.indexer.dao;


import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public interface IndexDao {

    void addToIndex(String pieceOfFile, UUID fileId);

    HashSet<UUID> getFilesByWord(String word);

}
