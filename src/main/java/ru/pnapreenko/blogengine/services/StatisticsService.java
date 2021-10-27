package ru.pnapreenko.blogengine.services;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.api.responses.APIResponse;
import ru.pnapreenko.blogengine.model.User;
import ru.pnapreenko.blogengine.model.dto.StatsDTO;
import ru.pnapreenko.blogengine.repositories.PostsRepository;
import ru.pnapreenko.blogengine.repositories.VotesRepository;

import java.security.Principal;

@Service
public class StatisticsService {

    private final PostsRepository postsRepository;
    private final VotesRepository votesRepository;
    private final UserAuthService userAuthService;
    private final SettingsService settingsService;

    public StatisticsService(PostsRepository postsRepository, VotesRepository votesRepository, UserAuthService userAuthService,
                             SettingsService settingsService) {
        this.postsRepository = postsRepository;
        this.votesRepository = votesRepository;
        this.userAuthService = userAuthService;
        this.settingsService = settingsService;
    }

    public ResponseEntity<?> getStats(String statsType, Principal principal) {
        User user = userAuthService.getUserFromDB(principal.getName());
        boolean isStatsPublic = settingsService.isStatsPublic();

        if (user.isModerator()) {
            if (statsType.equalsIgnoreCase("all") && isStatsPublic) {
                return ResponseEntity.status(HttpStatus.OK).body(getStatsDTO(null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(getStatsDTO(user));
        }

        if (isStatsPublic) {
            return ResponseEntity.status(HttpStatus.OK).body(getStatsDTO(null));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());
    }

    private StatsDTO getStatsDTO(User user) {
        StatsDTO stats = new StatsDTO();
        stats.setPostsCount(postsRepository.countByAuthor(user));
        stats.setLikesCount(votesRepository.countByUserAndValue(user, (byte) 1));
        stats.setDislikesCount(votesRepository.countByUserAndValue(user, (byte) -1));
        stats.setViewsCount(postsRepository.getViewsByUser(user));
        stats.setFirstPublication(postsRepository.getFirstPostDateByUser(user).getEpochSecond());
        return stats;
    }
}
