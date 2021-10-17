package ru.pnapreenko.blogengine.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pnapreenko.blogengine.api.utils.JsonViews;
import ru.pnapreenko.blogengine.model.dto.CalendarDTO;
import ru.pnapreenko.blogengine.services.CalendarService;

@RestController
@RequestMapping("/api/calendar")
public class ApiCalendarController {

    private final CalendarService calendarService;

    public ApiCalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping(value="", produces = "application/json")
    @JsonView(JsonViews.IdName.class)
    public CalendarDTO getCalendar(
            @RequestParam(name="year") String year) {
        return calendarService.getCalendar(year);
    }
}
