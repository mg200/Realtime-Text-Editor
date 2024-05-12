package com.envn8.app.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class CharacterSequence {
    private List<CHAR> chars;
    private String siteID;
    private int count;

    public CharacterSequence() {
        this.chars = new ArrayList<>();
        this.chars.add(new CHAR(0, "bof", this.siteID, new Object()));
        this.chars.add(new CHAR(10000, "eof", this.siteID, new Object()));
        this.siteID = UUID.randomUUID().toString();
        this.count = 100;
    }

    public double generateIndex(int indexStart, int indexEnd) {
        double diff = (indexEnd - indexStart);
        double index;
        if (diff <= 10) {
            index = indexStart + diff / 100;
        } else if (diff <= 1000) {
            index = Math.round(indexStart + diff / 10);
        } else if (diff <= 5000) {
            index = Math.round(indexStart + diff / 100);
        } else {
            index = Math.round(indexStart + diff / 1000);
        }
        return index;
    }

    public CHAR insert(int indexStart, int indexEnd, String charValue, Object attributes, String id) {
        double index = generateIndex(indexStart, indexEnd);
        System.out.println("Insert index: " + index);
        CHAR charObj = (id != null) ? new CHAR((int) index, charValue,  id, attributes, id) :
                new CHAR((int) index, charValue, id, attributes);

        this.chars.add((int) index, charObj);
        this.chars.sort(Comparator.comparingInt(CHAR::getIndex));
        return charObj;
    }

    public void remoteInsert(CHAR charObj) {
        this.chars.add(charObj);
        this.chars.sort(Comparator.comparingInt(CHAR::getIndex)
                .thenComparing(CHAR::getSiteID)
                .reversed()); 
    }

    public void delete(String id) {
        for (CHAR c : this.chars) {
            if (c.getId().equals(id)) {
                c.setFlagDelete(true);
                break;
            }
        }
    }
    public String getSequence() {
        StringBuilder seq = new StringBuilder();
        for (CHAR c : this.chars) {
            if (!c.isFlagDelete() && !"bof".equals(c.getChar()) && !"eof".equals(c.getChar())) {
                seq.append(c.getChar());
            }
        }
        return seq.toString();
    }

}
