package com.netflix.encodingservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VideoUploadedEvent {
    private String movieId;
    private String videoKey;
    private String bucketName;
    private String fileName;
    private long size;

}
