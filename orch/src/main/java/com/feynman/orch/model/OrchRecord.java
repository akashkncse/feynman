package com.feynman.orch.model;

import com.feynman.orch.dto.OrchRequestDto;
import com.feynman.orch.dto.TranscriptionReponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transcriptions")
public class OrchRecord {
    @Id
    private String id;
    private OrchRequestDto metadata;
    private TranscriptionReponseDto transcription;
    private List<ImageRecord> images;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageRecord {
        private String filename;
        private String contentType;
        private byte[] data;
    }

}
