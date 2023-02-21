package com.assemblyvoting.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * @author leandro-bezerra
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "schedule_id")
  @NotNull(message = "O identificador da pauta não pode ser nulo!")
  private Schedule schedule;

  private LocalDateTime startSession;
  private LocalDateTime endSession;
}
