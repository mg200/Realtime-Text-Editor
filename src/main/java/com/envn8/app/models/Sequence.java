// package com.envn8.app.models;

// import java.util.ArrayList;
// import java.util.Comparator;
// import java.util.List;
// import java.util.UUID;

// public class Sequence {
//     private List<CHAR> chars;
//     private String siteID;
//     private int count;

//     public Sequence() {
//         this.chars = new ArrayList<>();
//         this.chars.add(new CHAR(0, "bof", this.siteID, new Object()));
//         this.chars.add(new CHAR(10000, "eof", this.siteID, new Object()));
//         this.siteID = UUID.randomUUID().toString();
//         this.count = 100;
//     }

//     public double generateIndex(int indexStart, int indexEnd) {
//         double diff = (indexEnd - indexStart);
//         double index;
//         if (diff <= 10) {
//             index = indexStart + diff / 100;
//         } else if (diff <= 1000) {
//             index = Math.round(indexStart + diff / 10);
//         } else if (diff <= 5000) {
//             index = Math.round(indexStart + diff / 100);
//         } else {
//             index = Math.round(indexStart + diff / 1000);
//         }
//         return index;
//     }

//     public int compareIdentifier(Char c1, Char c2) {
//         if (c1.getIndex() < c2.getIndex()) {
//             return -1;
//         } else if (c1.getIndex() > c2.getIndex()) {
//             return 1;
//         } else {
//             return c1.getSiteID().compareTo(c2.getSiteID());
//         }
//     }

//     public CHAR insert(int indexStart, int indexEnd, String charValue, Object attributes, String id) {
//         double index = generateIndex(indexStart, indexEnd);
//         System.out.println("Insert index: " + index);
//         CHAR charObj = (id != null) ? new Char((int) index, charValue, this.siteID, attributes, id) :
//                 new CHAR((int) index, charValue, this.siteID, attributes);

//         this.chars.add((int) index, charObj);
//         this.chars.sort(Comparator.comparingInt(Char::getIndex));
//         return charObj;
//     }

//     public void remoteInsert(CHAR charObj) {
//         this.chars.add(charObj);
//         this.chars.sort(Comparator.comparingInt(CHAR::getIndex)
//                 .thenComparing(CHAR::getSiteID));
//     }

//     public void delete(String id) {
//         for (CHAR c : this.chars) {
//             if (c.getId().equals(id)) {
//                 c.setTombstone(true);
//                 break;
//             }
//         }
//     }

//     public void remoteRetain(CHAR charCopy) {
//         for (CHAR c : this.chars) {
//             if (c.getId().equals(charCopy.getId())) {
//                 c.update(charCopy);
//                 break;
//             }
//         }
//     }

//     public List<CHAR> getRelativeIndex(int index) {
//         List<CHAR> result = new ArrayList<>();
//         int aliveIndex = 0;
//         boolean itemsFound = false;
//         CHAR charStart = null;
//         CHAR charEnd = null;

//         for (CHAR c : this.chars) {
//             if (!c.isTombstone()) {
//                 if (aliveIndex > index) {
//                     charEnd = c;
//                     itemsFound = true;
//                 } else {
//                     charStart = c;
//                 }
//                 aliveIndex++;
//             }
//         }

//         if (!itemsFound && aliveIndex >= index) {
//             charEnd = this.chars.get(this.chars.size() - 1);
//             itemsFound = true;
//         }

//         if (charStart != null && charEnd != null) {
//             result.add(charStart);
//             result.add(charEnd);
//             return result;
//         } else {
//             throw new IllegalArgumentException("Failed to find relative index");
//         }
//     }

//     public int getCharRelativeIndex(CHAR charObj) {
//         int aliveIndex = 0;
//         boolean charFound = false;

//         for (CHAR c : this.chars) {
//             if (!c.isTombstone() && !"bof".equals(c.getChar()) && !"eof".equals(c.getChar())) {
//                 aliveIndex++;
//             }
//             if (c.getId().equals(charObj.getId())) {
//                 if (c.isTombstone()) {
//                     aliveIndex++;
//                 }
//                 charFound = true;
//                 break;
//             }
//         }

//         if (charFound) {
//             return aliveIndex - 1;
//         } else {
//             throw new IllegalArgumentException("Failed to find relative index");
//         }
//     }

//     public String getSequence() {
//         StringBuilder seq = new StringBuilder();
//         for (CHAR c : this.chars) {
//             if (!c.isTombstone() && !"bof".equals(c.getChar()) && !"eof".equals(c.getChar())) {
//                 seq.append(c.getChar());
//             }
//         }
//         return seq.toString();
//     }

//     public void pretty() {
//         for (CHAR c : this.chars) {
//             System.out.println(c.getIndex() + " " + c.getChar() + " " + c.getSiteID() + " " + c.isTombstone());
//         }
//     }
// }