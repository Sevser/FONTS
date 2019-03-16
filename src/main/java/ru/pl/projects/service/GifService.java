package ru.pl.projects.service;

import org.springframework.web.multipart.MultipartFile;

public interface GifService {

    byte[] loadGif(String text, String category, MultipartFile file);

    byte[] loadGif(String text, String category);

}
