package ru.pl.projects.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
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
