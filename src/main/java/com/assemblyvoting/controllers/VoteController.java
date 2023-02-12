package com.assemblyvoting.controllers;

import com.assemblyvoting.domain.Vote;
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
    Optional<Vote> vote = voteService.getVote(id);

    return vote.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping
  public ResponseEntity<Vote> createVote(@RequestBody Vote vote) {
    Optional<Vote> _vote = voteService.saveVote(vote);

    return _vote
        .map(it -> new ResponseEntity<>(it, HttpStatus.CREATED))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
  }
}
