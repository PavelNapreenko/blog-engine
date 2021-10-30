package ru.pnapreenko.blogengine.api.utils;

import ru.pnapreenko.blogengine.model.Tag;
import ru.pnapreenko.blogengine.model.dto.TagDTO;

public class TagDTOConverter {
    private TagDTOConverter() {
    }
    public static TagDTO getConversion(Tag tag) {
        return new TagDTO(tag, tag.getPosts().size());
    }
}
