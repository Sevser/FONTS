package ru.pl.projects.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "emotions")
public class Emotion {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private String name;
    private Integer weight;
}
