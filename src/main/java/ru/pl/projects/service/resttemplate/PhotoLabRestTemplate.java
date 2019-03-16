package ru.pl.projects.service.resttemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.pl.projects.service.utils.HeaderUtils;
import ru.pl.projects.service.utils.HeaderUtilsImpl;

@Service
public class PhotoLabRestTemplate implements CustomRestTemplate {
    private RestTemplate restTemplate;
    private HeaderUtils headerUtils;

    @Autowired
    public PhotoLabRestTemplate(HeaderUtils headerUtils) {
        this.headerUtils = headerUtils;
        restTemplate = new RestTemplate();
    }

    @Override
    public  <T> T doGet(String url, Class<T> requestType)
    {
        ResponseEntity<T> responseEntity;
        responseEntity = restTemplate.getForEntity(headerUtils.getBaseUrl() + url, requestType);
        return responseEntity.getBody();
    }

    @Override
    public <T> String doPost(String url, T requestBody) {
        HttpHeaders headers = headerUtils.getHttpHeader();
        HttpEntity<T> entity = new HttpEntity<>(requestBody, headers);
        return restTemplate.postForEntity(headerUtils.getBaseUrl()+url ,entity, String.class).getBody();
    }

    @Override
    public <T> String doUploadFile(String url, T requestBody) {
        HttpHeaders headers = headerUtils.getHttpHeader();
        HttpEntity<T> entity = new HttpEntity<>(requestBody, headers);
        String uploadedPhotoUrl = restTemplate.postForEntity(
                headerUtils.getUploadUrl()+url,
                entity, String.class).getBody();
        return uploadedPhotoUrl;
    }
}
