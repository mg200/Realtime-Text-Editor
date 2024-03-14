package com.envn8.app.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
@Document
public class Documents {
    @Id
    private String id;
    @Field
    private String title;
    @Field
    private String content;
    public Documents(){}
    public Documents(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String newtitle)
    {
        this.title = newtitle;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String newcontent)
    {
        this.content = newcontent;
    }
    // @Override
    // public String toString() {
    //     return String.format("Document[id='%s',title='%s',content='%s']", id,title,content);
    // } 
}
