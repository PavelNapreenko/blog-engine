package ru.pnapreenko.blogengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.api.utils.TagDTOConverter;
import ru.pnapreenko.blogengine.model.Tag;
import ru.pnapreenko.blogengine.model.dto.TagDTO;
import ru.pnapreenko.blogengine.repositories.PostsRepository;
import ru.pnapreenko.blogengine.repositories.TagsRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagsService {
    private final TagsRepository tagsRepository;
    private final PostsRepository postsRepository;

    public ResponseEntity<?> getWeightedTags(String query) {
        final long NUM_POSTS = postsRepository.countActivePosts(Instant.now());
        List<TagDTO> tags = getTagsDTOList(tagsRepository.findAllTags());
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

    public Tag saveTag(String tagName) {
        Tag tag = tagsRepository.findByNameIgnoreCase(tagName);
        return (tag != null) ? tag : tagsRepository.save(new Tag(tagName));
    }

    private List<TagDTO> getTagsDTOList(List<Tag> source) {
        return source.stream().map(TagDTOConverter::getConversion).collect(Collectors.toList());
    }
}
