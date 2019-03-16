package ru.pl.projects.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.pl.projects.service.MainService;

@Controller
public class PWAController {

    @Autowired
    private MainService mainService;


    @PostMapping("/rest/category")
    public ResponseEntity<String> getCategory(@RequestBody String text) {
        if (StringUtils.isEmpty(text)) {
            return ResponseEntity.badRequest().build();
        }
        String category = mainService.getCategoryByText(text);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }

    @PostMapping(value = "/rest/gif/from/text",
            produces = MediaType.IMAGE_GIF_VALUE)
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getGifByText(@RequestBody String text) {
        if (StringUtils.isEmpty(text)) {
            return ResponseEntity.badRequest().build();
        }
        byte[] result = mainService.getGifByText(text);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + text.hashCode() + ".gif")
                .contentType(MediaType.IMAGE_GIF)
                .contentLength(result.length)
                .body(new ByteArrayResource(result));

    }

    @PostMapping(value = "/rest/gif/from/image",
            consumes = MediaType.IMAGE_JPEG_VALUE,
            produces = MediaType.IMAGE_GIF_VALUE)
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getGifByText(@RequestParam(value = "text") String text,
                                                          @RequestBody byte[] image) {
        if (StringUtils.isEmpty(text)) {
            return ResponseEntity.badRequest().build();
        }
        byte[] result = mainService.getGifByTextAndImage(text, image);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + text.hashCode() + ".gif")
                .contentType(MediaType.IMAGE_GIF)
                .contentLength(result.length)
                .body(new ByteArrayResource(result));
    }

}
