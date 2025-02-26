package com.bravos2k5.bravosshop.service.impls;

import com.azure.core.http.rest.Response;
import com.azure.core.util.BinaryData;
import com.azure.storage.blob.*;
import com.azure.storage.blob.models.AccessTier;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlockBlobItem;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import com.bravos2k5.bravosshop.service.interfaces.BlobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

@Slf4j
@Service
public class BlobServiceImpl implements BlobService {

    private final BlobServiceClient blobServiceClient;
    private final BlobServiceAsyncClient blobServiceAsyncClient;
    private final Tika tika;

    @Autowired
    public BlobServiceImpl(BlobServiceClient blobServiceClient, BlobServiceAsyncClient blobServiceAsyncClient, Tika tika) {
        this.blobServiceClient = blobServiceClient;
        this.blobServiceAsyncClient = blobServiceAsyncClient;
        this.tika = tika;
    }

    @Override
    public String uploadBlob(InputStream inputStream, String name, String container) {
        BlobContainerAsyncClient blobContainerClient = blobServiceAsyncClient.getBlobContainerAsyncClient(container);
        BlobAsyncClient blobClient = blobContainerClient.getBlobAsyncClient(name);
        Mono<BlockBlobItem> blobItemMono = blobClient.upload(BinaryData.fromStream(inputStream));
        blobItemMono.subscribe(
                success -> log.info("{} file uploaded",name),
                   failed -> log.error("Fail to upload file {}",name));
        return blobClient.getBlobUrl();
    }

    @Override
    public String uploadImage(InputStream inputStream, String name, String container) throws IOException {
        String contentType;

        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        inputStream.transferTo(byteArrayOS);
        byte[] binaryData = byteArrayOS.toByteArray();
        byteArrayOS.close();

        contentType = tika.detect(binaryData,name);

        if(contentType == null || !contentType.contains("image")) {
            throw new IllegalArgumentException(name + " file is not an image, upload will be ignored!");
        }

        BlobContainerAsyncClient containerAsyncClient = blobServiceAsyncClient.getBlobContainerAsyncClient(container);
        BlobAsyncClient blobClient = containerAsyncClient.getBlobAsyncClient(name);
        BlobParallelUploadOptions uploadOptions = new BlobParallelUploadOptions(BinaryData.fromBytes(binaryData));
        BlobHttpHeaders headers = new BlobHttpHeaders();
        headers.setContentType(contentType);
        headers.setContentDisposition("inline");
        headers.setCacheControl("public, max-age=86400");
        uploadOptions.setHeaders(headers);
        Mono<Response<BlockBlobItem>> blockBlobItem = blobClient.uploadWithResponse(uploadOptions);
        blockBlobItem.subscribe(
                success -> log.info("Image {} was uploaded",name),
                failed -> log.error("Failed to upload image {}",name)
        );
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
