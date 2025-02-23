package com.bravos2k5.bravosshop.service.impls;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlockBlobItem;
import com.bravos2k5.bravosshop.service.interfaces.BlobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.InputStream;

@Slf4j
@Service
public class BlobServiceImpl implements BlobService {

    private final BlobServiceClient blobServiceClient;
    private final BlobServiceAsyncClient blobServiceAsyncClient;

    @Autowired
    public BlobServiceImpl(BlobServiceClient blobServiceClient, BlobServiceAsyncClient blobServiceAsyncClient) {
        this.blobServiceClient = blobServiceClient;
        this.blobServiceAsyncClient = blobServiceAsyncClient;
    }

    @Override
    public String uploadBlob(InputStream inputStream, String name, String container) {
        this.createIfNotExistContainer(container);
        BlobContainerAsyncClient blobContainerClient = blobServiceAsyncClient.getBlobContainerAsyncClient(container);
        BlobAsyncClient blobClient = blobContainerClient.getBlobAsyncClient(name);
        Mono<BlockBlobItem> blobItemMono = blobClient.upload(BinaryData.fromStream(inputStream),true);
        blobItemMono.subscribe(
                success -> log.info("{} file uploaded",name),
                   failed -> failed.printStackTrace());
        return blobClient.getBlobUrl();
    }

    @Override
    public void delete(String name, String container) {
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(container);
        BlobClient blobClient = blobContainerClient.getBlobClient(name);
        blobClient.delete();
    }

    private void createIfNotExistContainer(String container) {
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(container);
        blobContainerClient.createIfNotExists();
    }

}
