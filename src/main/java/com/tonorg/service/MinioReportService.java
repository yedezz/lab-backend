package com.tonorg.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * Service for managing PDF reports stored in MinIO. This class
 * encapsulates the details of interacting with the MinIO client,
 * providing simple methods for uploading a report and generating a
 * pre‑signed URL for secure, time‑limited access.
 */
@Service
public class MinioReportService {

    private final MinioClient minioClient;
    private final String bucket;

    public MinioReportService(MinioClient minioClient, @Value("${minio.bucket}") String bucket) {
        this.minioClient = minioClient;
        this.bucket = bucket;
    }

    /**
     * Uploads a PDF report to the configured bucket in MinIO.
     *
     * @param objectName the object key under which the file will be stored
     * @param data       the input stream of the PDF content
     * @param size       the size of the PDF in bytes
     */
    public void uploadReport(String objectName, InputStream data, long size) throws Exception {
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucket)
                .object(objectName)
                .stream(data, size, -1)
                .contentType("application/pdf")
                .build();
        minioClient.putObject(args);
    }

    /**
     * Generates a pre‑signed URL granting temporary read access to a PDF
     * stored in MinIO. The URL expires after the specified number of
     * seconds.
     *
     * @param objectName     the object key of the report in MinIO
     * @param expiresSeconds the number of seconds the URL should remain valid
     * @return the generated pre‑signed URL
     */
    public String generatePresignedUrl(String objectName, int expiresSeconds) throws Exception {
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucket)
                .object(objectName)
                .expiry(expiresSeconds)
                .build();
        return minioClient.getPresignedObjectUrl(args);
    }
}