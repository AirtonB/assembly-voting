package com.assemblyvoting.models.responses;

import lombok.Data;

@Data
/* CPF number (Cadastro de Pessoas Físicas); Portuguese for "Natural Persons Register") */
public class CPFResponse {
  private String taxNumber;
  private boolean valid;
}
