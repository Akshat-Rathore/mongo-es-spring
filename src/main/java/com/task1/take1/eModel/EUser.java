package com.task1.take1.eModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(indexName = "moviez")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EUser {
    @Id
    private String _id;
    private String nconst;
    private String primaryName;
    private String birthYear;
    private String deathYear;
    private String primaryProfession;
//    @Field(type = FieldType.)
    private List<String> knownForTitles;


    public EUser(String nconst, String primaryName, String birthYear, String deathYear, String primaryProfession, List<String> knownForTitles) {
        this.nconst = nconst;
        this.primaryName = primaryName;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
        this.primaryProfession = primaryProfession;
        this.knownForTitles = knownForTitles;
    }
    public EUser(){
        this._id="_id";
        this.nconst = "nconst";
        this.primaryName = "primaryName";
        this.birthYear = "birthYear";
        this.deathYear = "deathYear";
        this.primaryProfession = "primaryProfession";
        this.knownForTitles = new ArrayList<String>();
    }

    public String get_id() {
        return _id;
    }

    public String getNconst() {
        return nconst;
    }

    public String getPrimaryName() {
        return primaryName;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public String getDeathYear() {
        return deathYear;
    }

    public String getPrimaryProfession() {
        return primaryProfession;
    }

    public List<String> getKnownForTitles() {
        return knownForTitles;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setNconst(String nconst) {
        this.nconst = nconst;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public void setDeathYear(String deathYear) {
        this.deathYear = deathYear;
    }

    public void setPrimaryProfession(String primaryProfession) {
        this.primaryProfession = primaryProfession;
    }

    public void setKnownForTitles(List<String> knownForTitles) {
        this.knownForTitles = knownForTitles;
    }
}

