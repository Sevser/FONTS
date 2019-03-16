package ru.pl.projects.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MainService {

    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private GifService gifService;

    @Value("${upload.dir}")
    private String uploadDir;

    public String getCategoryByText(final String text){
        return classificationService.getCategoryByText(text);
    }

    public byte[] getGifByText(final String text) {
        log.info("Creating gif by text...");
        String category = classificationService.getCategoryByText(text);
        return gifService.loadGif(text, category);
    }

    public byte[] getGifByTextAndImage(final String text, final MultipartFile multipartFile) {
        String category = classificationService.getCategoryByText(text);
        return gifService.loadGif(text, category, saveFile(multipartFile));
    }

    public List<String> getImageUrls(final String text, final MultipartFile multipartFile) {
        String category = classificationService.getCategoryByText(text);
        return gifService.filter(category, saveFile(multipartFile));
    }

    private String saveFile(MultipartFile multipartFile) {
        String id = UUID.randomUUID().toString();
        Path path = Paths.get(uploadDir + id);
        try {
            Files.createFile(path);
            multipartFile.transferTo(path);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return id;
    }
}
