package ru.pl.projects.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "emotion_to_template")
public class EmotionTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long template_table_id;

    private Long templateId;

    public EmotionTemplate() {
    }
}
