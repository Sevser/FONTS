package ru.pl.projects.service.photolab;

import java.util.List;

public interface PhotoLabClient {
    String uploadImg(String imgName);
    String makeTemplateOnPhotoByName(String imgUrl, String templateId);
    List<Long> getTemplatesIdsByUrlId(String urlId);
}
