package com.tissenza.tissenza_backend.modules.boutique.dto;

import com.tissenza.tissenza_backend.modules.boutique.entity.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {
    private Long id;
    private Long personneId;
    private Document.Type type;
    private String url;
    private Boolean validated;
    private LocalDateTime createdAt;
}
