package ru.pl.projects.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.pl.projects.service.GifService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Profile("dummy")
@Slf4j
public class DummyGifService implements GifService {

    @Value("${static.gif.dir}")
    private String dir;

    @Value("${upload.dir}")
    private String uploadDir;

    @Override
    public byte[] loadGif(String text, String category, MultipartFile image) {
        String id = UUID.randomUUID().toString();
        try {
            image.transferTo(new File(uploadDir + id));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        //TODO здесь вызов photoLab
        return loadGif(text,category);
    }

    @Override
    public byte[] loadGif(String text, String category) {
        String resourceName;
        if (category == null) {
            resourceName = "default.gif";
        } else resourceName = category + ".gif";

        try {
//            Path path = Paths.get(ClassLoader.getSystemResource("gif/" + resourceName).toURI());
            Path path = Paths.get(dir + resourceName);
            return Files.readAllBytes(path);
        } catch (Exception e) {
            log.error("Gif loading failed");
            return null;
        }
    }
}
