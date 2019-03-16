package ru.pl.projects.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.pl.projects.service.GifService;
import ru.pl.projects.service.photolab.PhotoLabService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Profile("dummy")
@Slf4j
public class DummyGifService implements GifService {

    @Value("${static.gif.dir}")
    private String dir;

    @Autowired
    PhotoLabService photoLabService;

    @Override
    public byte[] loadGif(String text, String category, String fileId) {

        //TODO здесь вызов photoLab
//        List<String> list = photoLabService.applyFilters(category, id);

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
            log.error("Gif loading failed. {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<String> filter(String category, String fileId) {
        return photoLabService.applyFilters(category, fileId);
    }
}
