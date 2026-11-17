package com.netflix.encodingservice.event;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VideoEncodedEvent {
    private String movieId;
    private String hlsURL; // url for streaming
    private String masterPlaylistKey; // S3 key
    private String success;
    private String errorMessage;
}
