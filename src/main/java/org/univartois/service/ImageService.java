package org.univartois.service;

import jakarta.servlet.http.Part;

import java.io.IOException;

public interface ImageService {
    String uploadFile(byte[] file) throws IOException;
}
