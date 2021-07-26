package com.indexer.indexer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class IndexedFile {

    public IndexedFile(@JsonProperty("id") UUID id,
                       @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

    public IndexedFile(@JsonProperty("name") String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    private final UUID id;
    private final String name;
}
