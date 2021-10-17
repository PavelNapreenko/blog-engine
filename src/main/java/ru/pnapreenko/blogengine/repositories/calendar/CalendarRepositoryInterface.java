package ru.pnapreenko.blogengine.repositories.calendar;

import java.util.List;
import java.util.Map;

public interface CalendarRepositoryInterface {
    List<Integer> findAllYears();
    Map<String, Long> findAllPosts(String year);
}
