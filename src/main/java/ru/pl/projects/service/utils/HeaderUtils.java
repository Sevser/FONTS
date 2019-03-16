package ru.pl.projects.service.utils;

import org.springframework.http.HttpHeaders;

public interface HeaderUtils {
    String getBaseUrl();
    String getUploadUrl();
    HttpHeaders getHttpHeader();
}
