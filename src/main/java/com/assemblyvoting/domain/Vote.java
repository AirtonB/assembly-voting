package com.assemblyvoting.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author leandro-bezerra
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Vote {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Boolean registeredVote;

  @OneToOne private Schedule schedule;

  private String userIdentification;
}
