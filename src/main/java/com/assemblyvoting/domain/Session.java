package com.assemblyvoting.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

  @OneToOne
  @NotNull(message = "O identificador da pauta não pode ser nulo!")
  @JoinColumn(name = "schedule_id")
  private Schedule schedule;

  private LocalDateTime startSession = LocalDateTime.now();
  private LocalDateTime endSession;
}
