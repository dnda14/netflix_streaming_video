package com.netflix.videoservice.event;

import lombok.AllArgsConstructor;

@AllArgsConstructor 
public class VideoUploadedEvent {
    
    private String movieId;
    private String videoKey;
    private String bucketName;
    private String fileName;
    private long   size;
       
}
