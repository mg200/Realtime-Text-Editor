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
<<<<<<< Updated upstream
    public static String generateIdBetween(String beforeId, String afterId) {
        if (beforeId.equals(afterId)) {
            return beforeId + '0';
        } else if (beforeId.equals("9") && afterId.equals("10")) {
            return "95";
        } else if (afterId.startsWith(beforeId)) {
            return beforeId + '0';
        } else {
            int i = 0;
            while (i < beforeId.length() && i < afterId.length() && beforeId.charAt(i) == afterId.charAt(i)) {
                i++;
            }
            if (i < beforeId.length() && i < afterId.length()) {
                char nextChar = (char) (beforeId.charAt(i) + 1);
                if (nextChar > '9') {
                    return beforeId.substring(0, i) + '0' + '0';
                } else {
                    return beforeId.substring(0, i) + nextChar;
                }
            } else {
                return beforeId + '0';
            }
        }
=======

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
>>>>>>> Stashed changes
    }
}
