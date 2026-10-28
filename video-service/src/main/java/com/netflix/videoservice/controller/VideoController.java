package com.netflix.videoservice.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.function.EntityResponse;

import com.netflix.videoservice.service.VideoService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1/video")
public class VideoController {

    private final VideoService videoService;

    @PostMapping("/upload/{movieID}")
    public ResponseEntity<String> upldVideo(
            @RequestParam("file") MultipartFile file,
            @PathVariable String movieID ) throws IOException{

        log.info("video uploading in request: {} and size {}", movieID, file.getSize() / (1024 * 1024));

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("file is empty");

        }   

        String videoKey = videoService.upldVideo(file, movieID);

        return ResponseEntity.ok("video uploaded. video key: "+videoKey+". Encodign started automtclly.")
    }
}
