package org.univartois.service.impl;

import com.cloudinary.Cloudinary;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.Part;
import org.univartois.service.ImageService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class ImageServiceImpl implements ImageService {

    @Inject
    Cloudinary cloudinary;

    @Override
    public String uploadFile(byte[] file) throws IOException {
        System.out.println(Arrays.toString(file));
        return cloudinary.uploader()
                .upload(file,
                        Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();
    }
}
