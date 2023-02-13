package com.assemblyvoting.controllers;

import com.assemblyvoting.domain.Vote;
import com.assemblyvoting.models.responses.UserResponseStatus;
import com.assemblyvoting.services.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
/**
 * @author leandro-bezerra
 */
@RestController
@RequestMapping(value = "/vote", produces = "application/json")
public class VoteController {
  final VoteService voteService;

  public VoteController(VoteService voteService) {
    this.voteService = voteService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Vote> getVote(@PathVariable Long id) {
    Optional<Vote> vote = voteService.findVoteById(id);
    return vote.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping
  public ResponseEntity<UserResponseStatus> saveVote(@RequestBody Vote vote) {
    Optional<Vote> _vote = voteService.saveVote(vote);

    return _vote
        .map(it -> new ResponseEntity<>(UserResponseStatus.ABLE_TO_VOTE, HttpStatus.CREATED))
        .orElseGet(
            () ->
                new ResponseEntity<>(
                    UserResponseStatus.UNABLE_TO_VOTE, HttpStatus.INTERNAL_SERVER_ERROR));
  }
}
