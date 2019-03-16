package ru.pl.projects;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.pl.projects.db.repository.DictionaryRepository;
import ru.pl.projects.model.entity.Word;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Slf4j
public class MigrationService {

    public static void main(String[] args) {
        int wordIndex = 1;
        int valenceIndex = 2;
//        final String fileName = "dictionary.csv";
        final String fileName = "Ratings_Warriner_et_al.csv";
        ConfigurableApplicationContext context = SpringApplication.run(MigrationService.class, args);
        DictionaryRepository repository = context.getBean(DictionaryRepository.class);
        try {
            Path path = Paths.get(ClassLoader.getSystemResource(fileName).toURI());
            List<String> list = Files.readAllLines(path);
            log.info("Data size = {}", list.size());
            List<Word> result = new ArrayList<>(1000);
            for (String str : list) {
                String[] data = StringUtils.split(str, ",");
                String frequency = null;
                BigDecimal value = new BigDecimal(data[valenceIndex]).add(BigDecimal.valueOf(5).negate());
                if (repository.findByText(data[wordIndex]) == null) {
                    Word word = Word.builder()
                            .text(data[wordIndex])
                            .valence(value.doubleValue())
//                        .frequency(frequency.equals(".") ? 0 : Integer.valueOf(frequency))
                            .build();
                    result.add(word);
                }
            }
            repository.saveAll(result);
            log.info("Success!");
        } catch (Exception e) {
            log.info("Failed!");
            e.printStackTrace();
        }
        context.close();
    }

//    https://www.uvm.edu/pdodds/teaching/courses/2009-08UVM-300/docs/others/everything/bradley1999a.pdf
//    http://crr.ugent.be/archives/1003
}
