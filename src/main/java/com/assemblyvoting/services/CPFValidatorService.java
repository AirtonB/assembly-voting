package com.assemblyvoting.services;

import com.assemblyvoting.models.responses.CPFResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cpfValidator", url = "https://api.nfse.io/validate/NaturalPeople/taxNumber/")
public interface CPFValidatorService {

  @GetMapping("/{cpf}")
  CPFResponse checkCpf(@PathVariable("cpf") String cpf);
}
