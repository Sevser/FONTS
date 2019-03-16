package ru.pl.projects.service.photolab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pl.projects.db.repository.EmotionRepository;
import ru.pl.projects.model.entity.Emotion;

import java.util.ArrayList;
import java.util.List;

@Service
public class PhotoLabServiceImpl implements PhotoLabService {

    private final PhotoLabClient photoLabClient;
    private final EmotionRepository emotionRepository;

    @Autowired
    public PhotoLabServiceImpl(PhotoLabClient photoLabClient, EmotionRepository emotionRepository) {
        this.photoLabClient = photoLabClient;
        this.emotionRepository = emotionRepository;
    }

    @Override
    public List<String> applyFilters(String category, String imgName) {
        List<String> resulrUrls = new ArrayList<>();
        List<List<Long>> templatesUrlIdListForCategory = new ArrayList<>(new ArrayList<>());

        Emotion emotion = emotionRepository.findByName(category);
        emotion.getTemplates().forEach(template -> {
            String templId = String.valueOf(template.getTemplateId());
            List<Long> templatesIdsByUrlId = photoLabClient.getTemplatesIdsByUrlId(templId);
            templatesUrlIdListForCategory.add(templatesIdsByUrlId);
        });

        for (List<Long> list : templatesUrlIdListForCategory) {
            resulrUrls.add(getFileUrlWithAllFilters(list, imgName));
        }
        return resulrUrls;
    }

    private String getFileUrlWithAllFilters(List<Long> filtersIds, String imgName) {
        String resultUrl = photoLabClient.uploadImg(imgName);
        for (int i = 0; i < filtersIds.size(); i++) {
            String tmplId = String.valueOf(filtersIds.get(i));
            resultUrl = photoLabClient.makeTemplateOnPhotoByName(resultUrl, tmplId);
        }
        return resultUrl;
    }
}
