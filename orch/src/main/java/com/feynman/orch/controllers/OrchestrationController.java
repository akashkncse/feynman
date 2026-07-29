package com.feynman.orch.controllers;

import com.feynman.orch.dto.OrchRequestDto;
import com.feynman.orch.dto.TranscriptionReponseDto;
import com.feynman.orch.services.TranscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrchestrationController {

    private final TranscriptionService transcriptionService;

    @PostMapping(value = "/orch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> orch(
            @RequestPart("data") OrchRequestDto metadata,
            @RequestPart("file") MultipartFile audioFile,
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles
    ) {
        System.out.println(metadata);
        try {
            TranscriptionReponseDto transcript = transcriptionService.transcribeAudio(audioFile);
            System.out.println(transcript);
        } catch (IOException io) {
            return ResponseEntity.internalServerError().body("");
        }
        return ResponseEntity.ok("OK!");
    }
}
