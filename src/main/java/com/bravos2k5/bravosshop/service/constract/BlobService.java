package com.bravos2k5.bravosshop.service.constract;

import java.io.InputStream;

public interface BlobService {

    String uploadBlob(InputStream inputStream, String name, String container);

    void delete(String name, String container);

}
