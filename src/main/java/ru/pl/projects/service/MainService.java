package ru.pl.projects.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class MainService {

    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private GifService gifService;

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
        return gifService.loadGif(text, category, multipartFile);
    }
}
