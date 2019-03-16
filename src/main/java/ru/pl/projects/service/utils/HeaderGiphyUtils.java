package ru.pl.projects.service.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class HeaderGiphyUtils implements HeaderUtils {

    @Value("${giphy.api.baseurl}")
    private String GIPHY_API_BASEURL;

    @Override
    public String getBaseUrl() {
        return GIPHY_API_BASEURL;
    }

    @Override
    public String getUploadUrl() {
        return GIPHY_API_BASEURL;
    }


    @Override
    public HttpHeaders getHttpHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
