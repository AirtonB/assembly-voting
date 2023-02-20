package com.assemblyvoting.controllers;

import com.assemblyvoting.exceptions.ExceptionMessages;
import com.assemblyvoting.models.requests.VoteRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Random;

import static com.assemblyvoting.utils.CPFUtils.genInvalidCPF;
import static com.assemblyvoting.utils.CPFUtils.genValidCPF;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class VoteControllerIT {

  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper objectMapper;

  // scheduleId = 1 # sessionIsOpen
  // scheduleId = 2 # sessionClosed
  @Test
  @SneakyThrows
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Deve realizar um voto {verdadeiro} válido")
  void shouldCreateAndReturnValidTrueVote() {
    var voteRequest =
        VoteRequest.builder()
            .registeredVote(true)
            .scheduleId(1L)
            .userIdentification(genValidCPF())
            .build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/v1/vote")
                .content(objectMapper.writeValueAsString(voteRequest))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isNotEmpty());
  }

  @Test
  @SneakyThrows
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Deve realizar um voto {falso} válido")
  void shouldCreateAndReturnValidFalseVote() {
    var voteRequest =
        VoteRequest.builder()
            .registeredVote(false)
            .scheduleId(1L)
            .userIdentification(genValidCPF())
            .build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/v1/vote")
                .content(objectMapper.writeValueAsString(voteRequest))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isNotEmpty());
  }

  @Test
  @SneakyThrows
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Deve retornar 403 pois o documento do usuário é inválido")
  void shouldReturn403UserIdentificationIsInvalid() {
    var voteRequest =
        VoteRequest.builder()
            .registeredVote(new Random().nextBoolean())
            .scheduleId(1L)
            .userIdentification(genInvalidCPF())
            .build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/v1/vote")
                .content(objectMapper.writeValueAsString(voteRequest))
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().reason(ExceptionMessages.INVALID_CPF))
        .andExpect(status().isForbidden());
  }

  @Test
  @SneakyThrows
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Deve retornar 403 devido a sessão de votação estar encerrada")
  void shouldReturnForbiddenSessionIsClosed() {
    var voteRequest =
        VoteRequest.builder()
            .registeredVote(new Random().nextBoolean())
            .scheduleId(2L)
            .userIdentification(genValidCPF())
            .build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/v1/vote")
                .content(objectMapper.writeValueAsString(voteRequest))
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().reason(ExceptionMessages.CLOSSED_SESSION))
        .andExpect(status().isForbidden());
  }
}
