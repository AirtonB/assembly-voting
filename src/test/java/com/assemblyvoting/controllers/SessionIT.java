package com.assemblyvoting.controllers;

import com.assemblyvoting.models.requests.SessionRequest;
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

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SessionIT {

  @Autowired MockMvc mockMvc;

  @Autowired ObjectMapper objectMapper;

  @Test
  @SneakyThrows
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Deve abrir uma sessão com o tempo padrão: 1min")
  void shouldOpenAndReturnSessionWithDefaultSesssionTime() {
    var sessionRequest = SessionRequest.builder().scheduleId(1L).sessionEndTime(null).build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/v1/session")
                .content(objectMapper.writeValueAsString(sessionRequest))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$").isNotEmpty());
  }

  @Test
  @SneakyThrows
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Deve abrir uma sessão com o tempo definido")
  void shouldOpenAndReturnSessionWithSesssionTime() {
    var sessionRequest =
        SessionRequest.builder().scheduleId(1L).sessionEndTime(LocalDateTime.now()).build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/v1/session")
                .content(objectMapper.writeValueAsString(sessionRequest))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$").isNotEmpty());
  }
}
