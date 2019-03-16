package ru.pl.projects.service;

public interface GifService {

    byte[] loadGif(String text, String category, byte[] bytes);

    byte[] loadGif(String text, String category);

}
