package com.tonorg.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for integrating with a MinIO object storage service. The
 * MinioClient bean constructed here will be used throughout the
 * application wherever file storage or retrieval is required. The
 * connection parameters are externalized in the application.yml so that
 * they can easily be overridden for different environments.
 */
@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }
}