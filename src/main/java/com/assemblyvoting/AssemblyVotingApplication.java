package com.assemblyvoting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AssemblyVotingApplication {
  public static void main(String[] args) {
    SpringApplication.run(AssemblyVotingApplication.class, args);
  }
}
