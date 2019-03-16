package ru.pl.projects.db.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pl.projects.model.entity.Word;

@Repository
public interface DictionaryRepository extends JpaRepository<Word, Integer> {

    Word findByText(String text);

}
