package com.assemblyvoting.services;

import com.assemblyvoting.models.responses.CPFResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cpfValidator", url = "https://api.nfse.io")
public interface CPFValidatorService {

  @GetMapping("/validate/NaturalPeople/taxNumber/{cpf}")
  CPFResponse checkCpf(@PathVariable("cpf") String cpf);
}
