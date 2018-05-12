package com.photogallery.photogallerywebservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@SpringBootApplication
public class PhotoGalleryWebServiceApplication {

    @Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    public static void main(String[] args) {
        SpringApplication.run(PhotoGalleryWebServiceApplication.class, args);
    }
}
