package com.bravos2k5.bravosshop.service.interfaces;

import java.io.IOException;
import java.io.InputStream;

public interface BlobService {

    String uploadBlob(InputStream inputStream, String name, String container);

    String uploadImage(InputStream inputStream, String name, String container) throws IOException;

    void delete(String name, String container);

}
