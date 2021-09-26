package ru.pnapreenko.blogengine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.model.dto.CalendarDTO;
import ru.pnapreenko.blogengine.repositories.calendar.CalendarRepository;

import java.util.List;
import java.util.Map;

@Service
public class CalendarService {

    private final CalendarRepository calendarRepository;

    @Autowired
    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    public CalendarDTO getCalendar(String year) {
        List<Integer> years = calendarRepository.findAllYears(year);
        Map<String, Long> posts = calendarRepository.findAllPosts(year);
        return new CalendarDTO(years, posts);
    }
}
