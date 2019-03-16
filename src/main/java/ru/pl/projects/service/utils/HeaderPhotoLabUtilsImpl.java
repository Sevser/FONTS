package ru.pl.projects.service.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class HeaderPhotoLabUtilsImpl implements HeaderUtils {

    @Value("${photolab.api.baseurl}")
    private String PHOTOLAB_API_BASE_URL;
    @Value("${photolab.api.uploadurl}")
    private String PHOTOLAB_API_UPLOAD_URL;

    @Override
    public String getBaseUrl() {
        return PHOTOLAB_API_BASE_URL;
    }

    @Override
    public String getUploadUrl() {
        return PHOTOLAB_API_UPLOAD_URL;
    }

    @Override
    public HttpHeaders getHttpHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return headers;
    }

}
