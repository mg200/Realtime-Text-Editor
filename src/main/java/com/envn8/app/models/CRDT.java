package com.envn8.app.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
@AllArgsConstructor
@Document
public class CRDT {
    @Id
    private String id;
    @Field
    private char character;
    @Field
    private String beforeId;
    @Field
    private String afterId;
    //constructor
    public CRDT(char character, String id, String beforeId, String afterId) {
        this.character = character;
        this.id = id;
        this.beforeId = beforeId;
        this.afterId = afterId;
    }

    //getters and setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public char getCharacter() {
        return character;
    }
    public void setCharacter(char character) {
        this.character = character;
    }
    public String getBeforeId() {
        return beforeId;
    }
    public void setBeforeId(String beforeId) {
        this.beforeId = beforeId;
    }
    public String getAfterId() {
        return afterId;
    }
    public void setAfterId(String afterId) {
        this.afterId = afterId;
    }

    @Override
    public String toString() {
        return "CRDT{" +
                "id='" + id + '\'' +
                ", character=" + this.character +
                ", beforeId='" + this.beforeId + '\'' +
                ", afterId='" + this.afterId + '\'' +
          '}';
    }

    public static String generateIdBetween(String beforeId, String afterId) {
        System.out.println("EH EL KALAAAAAM");
        double befID = Double.parseDouble(beforeId);
        double aftId = Double.parseDouble(afterId);
        double newId = (befID+aftId)/2;
        System.out.println("GENERATE ID VALUE IS "+newId);
        return  Double.toString(newId);
    }
}
