package ru.pnapreenko.blogengine.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pnapreenko.blogengine.api.utils.JsonViews;

@Data
@NoArgsConstructor
public class StatsDTO {
    @JsonView(JsonViews.Name.class)
    private int postsCount;

    @JsonView(JsonViews.Name.class)
    private int likesCount;

    @JsonView(JsonViews.Name.class)
    private int dislikesCount;

    @JsonView(JsonViews.Name.class)
    private int viewsCount;

    @JsonView(JsonViews.Name.class)
    private long firstPublication;
}
