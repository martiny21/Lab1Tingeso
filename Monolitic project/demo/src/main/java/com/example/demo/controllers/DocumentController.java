package com.example.demo.controllers;

import com.example.demo.entities.DocumentEntity;
import com.example.demo.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/document")
@CrossOrigin("*")
public class DocumentController {

    @Autowired
    DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<DocumentEntity> uploadDocument(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId,
                                                         @RequestParam("name") String name){
        try {
            documentService.saveDocument(file, userId, name);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
