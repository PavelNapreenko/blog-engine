package ru.pnapreenko.blogengine.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pnapreenko.blogengine.api.utils.JsonViews;
import ru.pnapreenko.blogengine.services.TagsService;

@RestController
@RequestMapping(value = "/api/tag", produces = MediaType.APPLICATION_JSON_VALUE)

@RequiredArgsConstructor
public class ApiTagController {

    private final TagsService tagsService;

    @GetMapping()
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> getTags(
            @RequestParam(name="query", required = false) String query) {
        return tagsService.getWeightedTags(query);
    }
}
