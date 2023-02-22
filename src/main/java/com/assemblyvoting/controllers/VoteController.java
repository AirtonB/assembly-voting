package com.assemblyvoting.controllers;

import com.assemblyvoting.models.requests.VoteRequest;
import com.assemblyvoting.models.responses.UserResponseStatus;
import com.assemblyvoting.models.responses.VoteReponse;
import com.assemblyvoting.services.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

  @GetMapping("/vote/schedule/result/{id}")
  public ResponseEntity<VoteReponse> getResult(@PathVariable Long id) {
    final var vote = voteService.findResultByScheduleId(id);
    return vote.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping("/vote")
  public ResponseEntity<UserResponseStatus> saveVote(@RequestBody VoteRequest voteRequest) {
    final var vote = voteService.saveVote(voteRequest);

    return vote.map(it -> new ResponseEntity<>(UserResponseStatus.ABLE_TO_VOTE, HttpStatus.OK))
        .orElseGet(
            () ->
                new ResponseEntity<>(
                    UserResponseStatus.UNABLE_TO_VOTE, HttpStatus.INTERNAL_SERVER_ERROR));
  }
}
