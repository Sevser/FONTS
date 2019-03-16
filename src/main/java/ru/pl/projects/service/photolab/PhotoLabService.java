package ru.pl.projects.service.photolab;

import java.util.List;

public interface PhotoLabService {
    List<String> applyFilters(String category, String imgName);
}
