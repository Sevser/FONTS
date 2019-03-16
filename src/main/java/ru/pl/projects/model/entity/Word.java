package ru.pl.projects.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "words")
@NoArgsConstructor
@AllArgsConstructor
public class Word {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private String text;
    private Integer frequency;
    private Double valence;

}
