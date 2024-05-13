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
    public boolean getStartIndex(double index) {
       for(CHAR c : this.chars){
           if(c.getIndex() == index){
               return true;
           }
       }
        return false;
    }
    public double generateIndex(double indexStart, double indexEnd) {

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
        // while (getStartIndex(index))
        // {
        //     index-=0.01;
        // }
        return index;
    }
    
    public CHAR insert(double indexStart, double indexEnd, String charValue, Object attributes, String id) {
        double index = generateIndex(indexStart, indexEnd);
        System.out.println("Insert index: " + index);
        CHAR charObj = (id != null) ? new CHAR(index, charValue,  id, attributes, id) :
                new CHAR(index, charValue, id, attributes);

        this.chars.add(charObj);
        this.chars.sort(Comparator.comparingDouble(CHAR::getIndex));
        return charObj;
    }

    public void remoteInsert(CHAR charObj) {
        this.chars.add(charObj);
        this.chars.sort(Comparator.comparingDouble(CHAR::getIndex)
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


    public List<CHAR> getRelativeIndex(int index) {
        List<CHAR> result = new ArrayList<>();
        int aliveIndex = 0;
        boolean itemsFound = false;
        CHAR charStart = null;
        CHAR charEnd = null;
        System.out.println("ALOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO"+index);

        for (CHAR c : this.chars) {
            if (!c.isFlagDelete()) {
                System.out.println("zh2ttttttttttttttttt"+aliveIndex);
                if (aliveIndex > (int)index) {
                    charEnd = c;
                    itemsFound = true;
                    System.out.println("mashy "+c.getChar());
                    break;
                } else {
                    charStart = c;
                }
                aliveIndex++;
            }
        }
      

        if (!itemsFound && aliveIndex >= index) {
            charEnd = this.chars.get(this.chars.size() - 1);
            itemsFound = true;
        }
       
        if (charStart != null && charEnd != null) {
            System.out.println("start Character"+charStart.getChar());
            result.add(charStart);
            result.add(charEnd);
            return result;
        } else {
            throw new IllegalArgumentException("Failed to find relative index");
        }
    }

    public int getCharRelativeIndex(CHAR charObj) {
        int aliveIndex = 0;
        boolean charFound = false;

        for (CHAR c : this.chars) {
            if (!c.isFlagDelete() && !"bof".equals(c.getChar()) && !"eof".equals(c.getChar())) {
                aliveIndex++;
            }
            if (c.getId().equals(charObj.getId())) {
                if (c.isFlagDelete()) {
                    aliveIndex++;
                }
                charFound = true;
                break;
            }
        }

        if (charFound) {
            return aliveIndex - 1;
        } else {
            throw new IllegalArgumentException("Failed to find relative index");
        }
    }

}
