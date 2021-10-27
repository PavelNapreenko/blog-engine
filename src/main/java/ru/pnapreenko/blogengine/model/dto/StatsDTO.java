package ru.pnapreenko.blogengine.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pnapreenko.blogengine.api.utils.JsonViews;

@Data
@NoArgsConstructor
public class StatsDTO {
    @JsonView(JsonViews.Name.class)
    private long postsCount;

    @JsonView(JsonViews.Name.class)
    private long likesCount;

    @JsonView(JsonViews.Name.class)
    private long dislikesCount;

    @JsonView(JsonViews.Name.class)
    private long viewsCount;

    @JsonView(JsonViews.Name.class)
    private long firstPublication;
}
