package com.bravos2k5.bravosshop.config;

import com.azure.core.http.netty.NettyAsyncHttpClientBuilder;
import com.azure.storage.blob.BlobServiceAsyncClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class BlobConfig {

    @Bean
    public StorageSharedKeyCredential tokenCredential() {
        return new StorageSharedKeyCredential(
                "bravosstorage", System.getenv("ABS_KEY"));
    }

    @Bean
    public BlobServiceClient blobServiceClient() {
        return new BlobServiceClientBuilder()
                .endpoint("https://bravosstorage.blob.core.windows.net/")
                .credential(tokenCredential())
                .httpClient(new NettyAsyncHttpClientBuilder().connectTimeout(Duration.of(10, ChronoUnit.SECONDS)).build())
                .buildClient();
    }

    @Bean
    public BlobServiceAsyncClient blobServiceAsyncClient() {
        return new BlobServiceClientBuilder()
                .endpoint("https://bravosstorage.blob.core.windows.net/")
                .credential(tokenCredential())
                .httpClient(new NettyAsyncHttpClientBuilder().connectTimeout(Duration.of(10, ChronoUnit.SECONDS)).build())
                .buildAsyncClient();
    }

}
