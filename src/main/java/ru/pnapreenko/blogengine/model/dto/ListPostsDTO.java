package ru.pnapreenko.blogengine.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import ru.pnapreenko.blogengine.api.utils.JsonViews;

import java.util.List;

public class ListPostsDTO {
    @Getter
    @JsonView(JsonViews.IdName.class)
    private final long count;

    @Getter @Setter
    @JsonView(JsonViews.IdName.class)
    private List<?> posts;

    public ListPostsDTO(Page<?> posts) {
        this.posts = posts.getContent();
        this.count = posts.getTotalElements();
    }
}
