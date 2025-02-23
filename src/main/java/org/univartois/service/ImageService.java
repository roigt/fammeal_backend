package org.univartois.service;

import java.io.IOException;

public interface ImageService {
    String uploadFile(byte[] file) throws IOException;
}
