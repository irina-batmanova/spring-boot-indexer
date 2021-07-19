package com.example.indexer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;
import java.util.UUID;

public class IndexedFile {

    public IndexedFile(@JsonProperty("id") UUID id,
                       @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
//        this.file = file;
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
//    private final File file;
}
