package com.gallery;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gallery.mapper")
public class GalleryTourApplication {
    public static void main(String[] args) {
        SpringApplication.run(GalleryTourApplication.class, args);
    }
}
