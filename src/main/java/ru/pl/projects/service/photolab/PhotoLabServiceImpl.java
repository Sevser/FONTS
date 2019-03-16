package ru.pl.projects.service.photolab;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.pl.projects.db.repository.EmotionRepository;
import ru.pl.projects.model.entity.Emotion;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PhotoLabServiceImpl implements PhotoLabService {

    private final PhotoLabClient photoLabClient;
    private final EmotionRepository emotionRepository;

    @Value("${filter.max.count}")
    private int maxCountFilter;

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
        long start = System.nanoTime();
        String resultUrl = photoLabClient.uploadImg(imgName);
        for (int i = 0; i < (filtersIds.size() < maxCountFilter ? filtersIds.size() : maxCountFilter); i++) {
            String tmplId = String.valueOf(filtersIds.get(i));
            try {
                resultUrl = photoLabClient.makeTemplateOnPhotoByName(resultUrl, tmplId);
            } catch (RuntimeException e) {
                log.error(e.getMessage());
                continue;
            }
        }
        log.info("TIME:{}", System.nanoTime() - start);
        return resultUrl;
    }
}
