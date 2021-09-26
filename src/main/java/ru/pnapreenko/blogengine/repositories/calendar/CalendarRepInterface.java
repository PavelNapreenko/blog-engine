package ru.pnapreenko.blogengine.repositories.calendar;

import java.util.List;
import java.util.Map;

public interface CalendarRepInterface {
    List<Integer> findAllYears(String year);
    Map<String, Long> findAllPosts(String year);
}
