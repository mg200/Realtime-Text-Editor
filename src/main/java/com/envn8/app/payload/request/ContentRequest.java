package com.envn8.app.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentRequest {
    private Double index;
    private String siteID;
    private Boolean bold;
    private String id;
    @JsonProperty("char")
    private String character;
    private Boolean isItalic;
    private Boolean flagDelete;

}