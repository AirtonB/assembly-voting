package com.assemblyvoting.controllers;

import com.assemblyvoting.domain.Vote;
import com.assemblyvoting.models.requests.VoteRequest;
import com.assemblyvoting.models.responses.UserResponseStatus;
import com.assemblyvoting.models.responses.VoteReponse;
import com.assemblyvoting.services.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
/**
 * @author leandro-bezerra
 */
@RestController
@RequestMapping(value = "/v1", produces = "application/json")
public class VoteController {
  private final VoteService voteService;

  public VoteController(VoteService voteService) {
    this.voteService = voteService;
  }

  @RequestMapping(value = "/vote/schedule/result/{id}", method = RequestMethod.GET)
  public ResponseEntity<VoteReponse> getResult(@PathVariable Long id) {
    Optional<VoteReponse> vote = voteService.findResultByScheduleId(id);

    return vote.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @RequestMapping(value = "/vote", method = RequestMethod.POST)
  public ResponseEntity<UserResponseStatus> saveVote(@RequestBody VoteRequest voteRequest) {
    Optional<Vote> _vote = voteService.saveVote(voteRequest);

    return _vote
        .map(it -> new ResponseEntity<>(UserResponseStatus.ABLE_TO_VOTE, HttpStatus.OK))
        .orElseGet(
            () ->
                new ResponseEntity<>(
                    UserResponseStatus.UNABLE_TO_VOTE, HttpStatus.INTERNAL_SERVER_ERROR));
  }
}
