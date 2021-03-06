package ru.pnapreenko.blogengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.api.responses.APIResponse;
import ru.pnapreenko.blogengine.model.Post;
import ru.pnapreenko.blogengine.model.PostVote;
import ru.pnapreenko.blogengine.model.User;
import ru.pnapreenko.blogengine.repositories.PostsRepository;
import ru.pnapreenko.blogengine.repositories.VotesRepository;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class PostVotesService {

    private final VotesRepository votesRepository;
    private final UserAuthService userAuthService;
    private final PostsRepository postsRepository;

    public ResponseEntity<?> vote(String voteType, Integer postId, Principal principal) {
        User user = userAuthService.getUserFromDB(principal.getName());

        if (postId <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error());
        }

        Post post = postsRepository.findById(postId).orElse(null);

        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error());
        }

        byte voteRequested = voteType.equalsIgnoreCase("like") ? (byte) 1 : (byte) -1;
        PostVote vote = votesRepository.findByUserAndPost(user, post);

        if (user.isModerator() || user.getId() == post.getAuthor().getId()) {
            return ResponseEntity.ok(APIResponse.error());
        }
        if (vote == null) {
            PostVote newVote = new PostVote(user, post);
            newVote.setValue(voteRequested);
            votesRepository.save(newVote);
            return ResponseEntity.ok(APIResponse.ok());
        }

        if (voteRequested == vote.getValue()) {
            return ResponseEntity.ok(APIResponse.error());
        }

        votesRepository.delete(vote);

        PostVote newVote = new PostVote(user, post);
        newVote.setValue(voteRequested);
        votesRepository.save(newVote);

        return ResponseEntity.ok(APIResponse.ok());
    }
}
