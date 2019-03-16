package ru.pl.projects.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pl.projects.model.entity.Emotion;

@Repository
public interface EmotionRepository extends JpaRepository<Emotion, Integer> {

    Emotion getOneByWeight(Integer weight);
}
