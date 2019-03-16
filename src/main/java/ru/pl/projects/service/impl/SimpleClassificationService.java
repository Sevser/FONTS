package ru.pl.projects.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.pl.projects.db.repository.EmotionRepository;
import ru.pl.projects.model.entity.Emotion;
import ru.pl.projects.model.entity.Word;
import ru.pl.projects.service.ClassificationService;
import ru.pl.projects.db.repository.DictionaryRepository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Profile("!nlp")
public class SimpleClassificationService implements ClassificationService {

    @Autowired
    private DictionaryRepository repository;

    @Autowired
    private EmotionRepository emotionRepository;

    private Map<Integer, Emotion> emotionMap;

    @PostConstruct
    public void init() {
        emotionMap = new HashMap<>();
        emotionMap.putAll(emotionRepository.findAll().stream().collect(Collectors.toMap(Emotion::getWeight, o -> o)));
    }


    @Override
    public String getCategoryByText(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        String[] data = prepareData(text);

        if (data.length == 1) {
            return getCategoryByWord(data[0]);
        }
        Double avgValence = getAvgValence(data);
        Emotion emotion = emotionMap.get(avgValence.intValue());
        return emotion.getName();
    }

    private String getCategoryByWord(String word) {
        Word result = repository.findByText(word);
        if (result == null) return null;
        Integer value = result.getValence().intValue();
        return emotionMap.get(value).getName();
    }

    private double getAvgValence(String[] data) {
        Word first = repository.findByText(data[0]);
        double avgValence;
        if (first == null) {
            avgValence = 0;
        } else {
            avgValence = first.getValence();
        }

        for (int i = 1; i < data.length; i++) {
            Word word = repository.findByText(data[i]);
            if (data[i-1].equals("not") || data[i-1].equals("no")) {
                avgValence-=word.getValence();
            } else {
                avgValence+=word.getValence();
            }
        }
        if (avgValence > 0) {
            avgValence += 0.5;
        } else if (avgValence < 0) {
            avgValence -= 0.5;
        }
        return avgValence;
    }

    private String[] prepareData(String plain) {
        String s = plain.replaceAll(",", " ").replaceAll(";", " ").replaceAll("\\.", " ");
        return StringUtils.split(s.replaceAll("\\s++", " "), " ");
    }
}
