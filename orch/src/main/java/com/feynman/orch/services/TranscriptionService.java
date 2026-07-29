package com.feynman.orch.services;

import com.feynman.orch.dto.TranscriptionReponseDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class TranscriptionService {
    private final RestClient restClient;

    public TranscriptionService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:2345")
                .build();
    }

    public TranscriptionReponseDto transcribeAudio(MultipartFile audio) throws IOException {
        ByteArrayResource fileResource = new ByteArrayResource(audio.getBytes()) {
            @Override
            public String getFilename() {
                return audio.getOriginalFilename() != null
                        ? audio.getOriginalFilename()
                        : "audio.wav";
            }
        };
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        return restClient.post()
                .uri("/transcribe")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(TranscriptionReponseDto.class);
    }
}
