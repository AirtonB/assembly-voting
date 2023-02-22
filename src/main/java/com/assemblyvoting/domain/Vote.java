package com.assemblyvoting.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author leandro-bezerra
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vote {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Seu voto não pode ser nulo!")
  private Boolean registeredVote;

  @OneToOne
  @NotNull(message = "A pauta de votação não pode ser nula!")
  private Schedule schedule;

  @NotBlank(message = "Utilize seu CPF. Campo nulo ou inválido!")
  private String userIdentification;
}
