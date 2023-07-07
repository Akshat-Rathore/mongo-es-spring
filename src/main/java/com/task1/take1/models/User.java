package com.task1.take1.models;

import lombok.Data;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
//@org.springframework.data.elasticsearch.annotations.Document(indexName="test")
public class User {
    @Id
    private String _id;
    private String nconst;
    private String primaryName;
    private String birthYear;
    private String deathYear;
    private String primaryProfession;
    private List<String> knownForTitles;

    public User(String nconst, String primaryName, String birthYear, String deathYear, String primaryProfession, List<String> knownForTitles) {
        this.nconst = nconst;
        this.primaryName = primaryName;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
        this.primaryProfession = primaryProfession;
        this.knownForTitles = knownForTitles;
    }
}

