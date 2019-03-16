package ru.pl.projects.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GifService {

    byte[] loadGif(String text, String category, String fileId);

    byte[] loadGif(String text, String category);

    List<String> filter(String category, String fileId);

}
