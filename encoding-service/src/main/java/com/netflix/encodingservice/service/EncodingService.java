package com.netflix.encodingservice.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.netflix.encodingservice.event.VideoEncodedEvent;
import com.netflix.encodingservice.event.VideoUploadedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectAclRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class EncodingService {
    private final S3Client s3Client;

    private final KafkaTemplate<String, VideoEncodedEvent> kafkaTemplate;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${ffmpeg.path}")
    private String ffmpegPath;

    @Value("${encod.path}")
    private String basePath;

    private static final String VIDEO_ENCODED_TOPIC = "video.encoded";
    // format, bitrate, resolution
    private static final List<int[]> VIDEO_QUALITIES = Arrays.asList(
            new int[] { 1920, 5000, 1080 },
            new int[] { 1280, 2000, 720 },
            new int[] { 854, 1200, 480 },
            new int[] { 640, 800, 360 });

    public void encodeVideo(VideoUploadedEvent event) {
        log.info("starting platfform for movie {}", event.getMovieId());
        // create unique path
        String jobPath = basePath + "/" + event.getMovieId();

        try {
            Files.createDirectories(Paths.get(jobPath));
            Files.createDirectories(Paths.get(jobPath + "/encoded"));

            String localVideoPath = jobPath + "/raw_video.mp4";
            downloadFromS3(event.getVideoKey(), localVideoPath);
            log.info("video downded in: {}", localVideoPath);

            for (int[] qualities : VIDEO_QUALITIES) {
                int width = qualities[0];
                int bitrate = qualities[1];
                int height = qualities[2];

                String qualityDir = jobPath + "/encoded" + height + "p";
                Files.createDirectories(Paths.get(qualityDir));

                encodeToHLS(localVideoPath, qualityDir, width, height, bitrate);

                log.info("encoded [{}p succesfully", height);
            }
            String masterPlaylistPath = jobPath + "/encoded/master.m3u8";
            generateMasterPlaylist(masterPlaylistPath);
            log.info("created master playlist");

            // upload to S3
            String encodedPrefix = "encoded/" + event.getMovieId() + "/";
            uploadEncondedFilesToS3(jobPath + "/encoded", encodedPrefix);
            log.info("uploaded files to S3");

            String masterPlaylistKey = encodedPrefix + "master.m3u8";
            String hlsURL = "https://" + bucketName + ".s3.amazonaws.com/" + masterPlaylistKey;

            VideoEncodedEvent encodedEvent = new VideoEncodedEvent(
                    event.getMovieId(),
                    hlsURL,
                    masterPlaylistKey,
                    true,
                    null);
            kafkaTemplate.send(VIDEO_ENCODED_TOPIC, event.getMovieId(), encodedEvent);
            log.info("encoded event published");
        } catch (Exception e) {
            log.info("encoding failed for movie {} --{}", event.getMovieId(), e.getMessage());

            VideoEncodedEvent failEvent = new VideoEncodedEvent(
                    event.getMovieId(),
                    null,
                    null,
                    false,
                    e.getMessage());
            kafkaTemplate.send(VIDEO_ENCODED_TOPIC, event.getMovieId(), failEvent);

        } finally {
            cleanupTempfiles(jobPath);
        }

    }

    private void downloadFromS3(String videokey, String videoPath) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(videokey)
                .build();
        s3Client.getObject(objectRequest, Paths.get(videoPath));

    }

    private void encodeToHLS(String localPath, String qualityDir, int width, int height,int bitrate){
        String playlistPath = qualityDir + "/playlist.m3u8";
        String segmentPattern = qualityDir + "segment_%03d.ts";

        List<String> command = Arrays.asList(
            ffmpegPath,
            "-i", localPath,
            "-vf", "scale=" + width + ":" + height,
            "-c:v", "libx264",
            "-b:v", bitrate + "k",
            "-c:a", "aac",
            "-b:a", "128k",
            "-hls_time", "10",
            "-hls_list_size", "0",
            "-hls_segment_filename", segmentPattern,
            "-f", "hls",
            playlistPath
        );
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process=processBuilder.start();
        int exitCode = process.waitFor()   ;

        if (Exitcode!=0) {
            throw text RuntimeException("ffmpeg failed" + exitCode);
        }
    }

}
