package ru.pl.projects.service.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.pl.projects.service.ClassificationService;

@Service
@Profile("nlp")
public class SmartClassificationService implements ClassificationService {

    @Override
    public String getCategoryByText(String text) {
        return null;
    }
}
