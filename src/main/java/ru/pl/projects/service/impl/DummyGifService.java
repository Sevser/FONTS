package ru.pl.projects.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.pl.projects.service.GifService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Profile("dummy")
@Slf4j
public class DummyGifService implements GifService {

    @Value("${static.gif.dir}")
    private String dir;

    @Override
    public byte[] loadGif(String text, String category, byte[] bytes) {
        return new byte[0];
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
