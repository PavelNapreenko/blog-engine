package ru.pnapreenko.blogengine.services;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.model.User;
import ru.pnapreenko.blogengine.model.dto.StatsDTO;
import ru.pnapreenko.blogengine.repositories.PostsRepository;
import ru.pnapreenko.blogengine.repositories.VotesRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final PostsRepository postsRepository;
    private final VotesRepository votesRepository;
    private final UserAuthService userAuthService;
    private final SettingsService settingsService;

    public ResponseEntity<?> getStats(String statsType, Principal principal) {
        boolean isStatsPublic = settingsService.isStatsPublic();
        User user = userAuthService.getUserFromDB(principal.getName());

        if (statsType.equalsIgnoreCase("all") && (isStatsPublic || user.isModerator())) {
            return ResponseEntity.status(HttpStatus.OK).body(getStatsDTO(null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(getStatsDTO(user));
    }

    private StatsDTO getStatsDTO(User user) {
        long firstPublication;
        StatsDTO stats = new StatsDTO();
        stats.setPostsCount(getValue((postsRepository.countByAuthor(user))));
        stats.setLikesCount(getValue(votesRepository.countByUserAndValue(user, (byte) 1)));
        stats.setDislikesCount(getValue(votesRepository.countByUserAndValue(user, (byte) -1)));
        stats.setViewsCount(getValue(postsRepository.getViewsByUser(user)));

        String firstPostDate = postsRepository.getFirstPostDateByUser(user);
        if (firstPostDate == null) {
            firstPublication = 0;
        } else {
            firstPublication = LocalDateTime.parse(firstPostDate,
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.US))
                    .atZone(ZoneId.of("Europe/Moscow")).toInstant().getEpochSecond();
        }
        stats.setFirstPublication(firstPublication);
        return stats;
    }

    private int getValue(Integer value) {
        if (value == null) {
            return 0;
        }
        return value;
    }
}
