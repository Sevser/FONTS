package ru.pl.projects.service.photolab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.pl.projects.service.resttemplate.PhotoLabRestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PhotoLabClientImpl implements PhotoLabClient {

    private final PhotoLabRestTemplate photoLabRestTemplate;

    @Value("${upload.dir}")
    private String path;

    @Autowired
    public PhotoLabClientImpl(PhotoLabRestTemplate photoLabRestTemplate) {
        this.photoLabRestTemplate = photoLabRestTemplate;
    }

    @Override
    public String uploadImg(String imgName) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        File fileForUpload = new File(path + imgName);
        body.add("file1", new FileSystemResource(fileForUpload));
        body.add("no_resize", "1");
        return photoLabRestTemplate.doUploadFile("/upload.php", body);
    }

    @Override
    public String makeTemplateOnPhotoByName(String imgUrl, String templateId) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image_url[1]", imgUrl);
        body.add("template_name",templateId);
        return photoLabRestTemplate.doPost("/template_process.php",body);
    }

    @Override
    public List<Long> getTemplatesIdsByUrlId(String urlId) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("combo_id", urlId);
        String result = photoLabRestTemplate.doPost("/photolab_steps.php",body);
        result = result.substring(1);
        result = result.substring(0, result.length() - 1);
        String[] strArr = result.split(",");
        List<String> strIds = Arrays.asList(strArr);
        List<Long> ids = new ArrayList<>();
        strIds.forEach(i -> ids.add(Long.parseLong(i)));
        return ids;
    }
}
