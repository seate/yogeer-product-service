package com.yoger.productserviceorganization.product.domain.port;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {
    String uploadImage(MultipartFile image);

    void deleteImage(String imageUrl);
}
