package ru.pl.projects.service.giphy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.pl.projects.model.entity.giphy.Datum;
import ru.pl.projects.model.entity.giphy.GifResponseDto;
import ru.pl.projects.service.utils.HeaderGiphyUtils;
import ru.pl.projects.service.utils.HeaderUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class GiphyService {

    @Value("${giphy.api.apikey}")
    private String API_KEY;

    private RestTemplate restTemplate;
    private HeaderUtils headerUtils;

    @Autowired
    public GiphyService(HeaderGiphyUtils headerUtils) {
        this.headerUtils = headerUtils;
        this.restTemplate = new RestTemplate();
    }

    public List<String> gifUrlListByText(String text) {
        List<String> gifListUrl = new ArrayList<>();
        HttpEntity<?> entity = new HttpEntity<>(headerUtils.getHttpHeader());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(headerUtils.getBaseUrl())
                .queryParam("q", text)
                .queryParam("apikey", API_KEY);

        ResponseEntity<GifResponseDto> responseEntity =
                restTemplate.exchange(
                        builder.toUriString(),
                        HttpMethod.GET,
                        entity,
                        GifResponseDto.class);
        List<Datum> data = responseEntity.getBody().getData();
        data.forEach(dataItem -> gifListUrl.add(dataItem.getEmbedUrl()));
        return gifListUrl;
    }
}
