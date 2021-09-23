package ru.pnapreenko.blogengine.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.model.Tag;
import ru.pnapreenko.blogengine.model.dto.TagDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class TagsService {

    public ResponseEntity<?> getWeightedTags(String query) {
        final long NUM_POSTS = 20;
        List<TagDTO> tags = new ArrayList<>();

        for (int i = 0; i < 6; i++ ) {
            Random r = new Random();
            String name = "TAG" + i;
            Tag tag = new Tag();
            tag.setName(name);
            TagDTO tagForPage = new TagDTO(tag, r.nextInt(10));
            tags.add(tagForPage);
        }

        tags.forEach(tag -> tag.setBaseWeight(NUM_POSTS));
        tags.forEach(tag -> tag.setWeight(tags.get(0).getBaseWeight()));

        final List<TagDTO> filteredTags = (query == null) ? tags : tags.stream()
                .filter(tag ->
                        tag.getName().toLowerCase()
                                .contains(query.toLowerCase()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new HashMap<>() {{
            put("tags", filteredTags);
        }});
    }
}
