package com.example.indexer.model;

import java.io.File;
import java.util.UUID;

public class IndexedFile {

    public IndexedFile(UUID id, String name, File file) {
        this.id = id;
        this.name = name;
        this.file = file;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

//    public

    private final UUID id;
    private final String name;
    private final File file;
}
