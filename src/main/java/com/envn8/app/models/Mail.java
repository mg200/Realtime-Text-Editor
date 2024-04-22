package com.envn8.app.models;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class Mail {
    @NotBlank
    @Email
    private String from;
    @NotBlank
    @Email
    private String to;
    @NotBlank
    private String subject;
    private String cc;
    private String body;
    private String ContentType = "text/plain";
    private List<Object> attachments;

    public Date getMailSendDate() {
        return new Date();
    }

    public String getMailFrom() {
        return from;
    }
    
    public String getMailTo() {
        return to;
    }

    public void setMailTo(String to) {
        this.to = to;
    }

    public String getMailSubject() {
        return subject;
    }

    public void setMailSubject(String subject) {
        this.subject = subject;
    }

    public String getMailCc() {
        return cc;
    }

    public void setMailCc(String cc) {
        this.cc = cc;
    }

    public String getMailContent() {
        return body;
    }

    public void setMailContent(String body) {
        this.body = body;
    }

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    public List<Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Object> attachments) {
        this.attachments = attachments;
    }

}
