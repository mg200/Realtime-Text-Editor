package com.envn8.app.payload.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentRequest {
    private String content;
    private String title;
    private String type;
    private String description;
}