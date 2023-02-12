package com.assemblyvoting.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * @author leandro-bezerra
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Session {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne private Schedule schedule;

  private LocalDateTime startSession;
  private LocalDateTime endSession;

  private Long expirationTimeInMinutes;
}
