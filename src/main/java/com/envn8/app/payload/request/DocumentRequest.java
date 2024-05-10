package com.envn8.app.payload.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentRequest {  // only used for create and rename document so no need for content 
    // private String content;  
    private String title;
    private String type;
    private String description;
}