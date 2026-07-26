package com.netflix.contentservice.model;

public enum VideoStatus {
    PENDING,    // ADDED NOT UPD
    UPLOADED,   // VIDEO UPD TO S3
    ENCODING,   // FFMPEG IS ENCODIGN
    ENCODED,  
    READY,       // HLS READY
    FAILED       // ENCODING FAILED
}
