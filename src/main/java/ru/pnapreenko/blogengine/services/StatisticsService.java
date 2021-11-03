package ru.pnapreenko.blogengine.services;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.api.responses.APIResponse;
import ru.pnapreenko.blogengine.model.User;
import ru.pnapreenko.blogengine.model.dto.StatsDTO;
import ru.pnapreenko.blogengine.repositories.PostsRepository;
import ru.pnapreenko.blogengine.repositories.VotesRepository;

import java.security.Principal;
import java.time.Instant;
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
        if (principal == null) {
            return ResponseEntity.ok(APIResponse.error());
        }

        User user = userAuthService.getUserFromDB(principal.getName());
        boolean isStatsPublic = settingsService.isStatsPublic();

        if ((statsType.equalsIgnoreCase("all") && isStatsPublic) || user.isModerator()) {
            return ResponseEntity.status(HttpStatus.OK).body(getStatsDTO(null));
        } else {
        return ResponseEntity.status(HttpStatus.OK).body(getStatsDTO(user));
        }
    }

    private StatsDTO getStatsDTO(User user) {
        StatsDTO stats = new StatsDTO();
        Instant firstPublication = LocalDateTime.parse(postsRepository.getFirstPostDateByUser(user),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.US))
                .atZone(ZoneId.of( "Europe/Moscow" )).toInstant();
        stats.setPostsCount(postsRepository.countByAuthor(user));
        stats.setLikesCount(votesRepository.countByUserAndValue(user, (byte) 1));
        stats.setDislikesCount(votesRepository.countByUserAndValue(user, (byte) -1));
        stats.setViewsCount(postsRepository.getViewsByUser(user));
        stats.setFirstPublication(firstPublication.getEpochSecond());
        return stats;
    }
}
