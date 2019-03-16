package ru.pl.projects.service.resttemplate;

public interface CustomRestTemplate {
    <T> T doGet(String url, Class<T> responseType);
    <T> String doPost(String url, T requestBody);
    <T> String doUploadFile(String url, T requestBody);
}
