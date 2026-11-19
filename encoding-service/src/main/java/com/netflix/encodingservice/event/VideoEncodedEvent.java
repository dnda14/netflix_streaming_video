package com.netflix.encodingservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class VideoEncodedEvent {
    private String movieId;
    private String hlsURL; // url for streaming
    private String masterPlaylistKey; // S3 key
    private String success;
    private String errorMessage;
}
