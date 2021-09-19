package ru.pnapreenko.blogengine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.api.utils.DateUtils;
import ru.pnapreenko.blogengine.model.dto.CalendarDTO;
import ru.pnapreenko.blogengine.model.dto.PostDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalendarService {

    private final PostsService postsService;

    @Autowired
    public CalendarService(PostsService postsService) {
        this.postsService = postsService;
    }

    public CalendarDTO getCalendar(String year) {
        List<Integer> years = List.of(2021);
        Map<String, Long> posts = new HashMap<>();

        for(PostDTO p: postsService.getPostList()) {
            String postYear = DateUtils.formatDate(p.getDate(),"yyyy-MM-dd");
            if (postYear.contains(year)) {
                long l = postsService.getPostList().size();
                posts.put(postYear, l);
            }
        }
        return new CalendarDTO(years, posts);
    }
}
