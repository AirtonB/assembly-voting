package com.assemblyvoting.utils;

import java.util.Random;

public class CPFUtils {

  public static String genValidCPF() {
    return generateCPF(true);
  }

  public static String genInvalidCPF() {
    return generateCPF(false);
  }

  private static String generateCPF(boolean valid) {
    StringBuilder partial = new StringBuilder();
    String cpf;
    int number;

    for (int i = 0; i < 9; i++) {
      number = (int) (Math.random() * 10);
      partial.append(number);
    }

    if (valid) {
      cpf = partial + CPFUtils.calculateVerificationDigits(partial.toString());
    } else {
      Random random = new Random();
      long elevenDigits = (random.nextInt(1000000000) + (random.nextInt(90) + 10) * 1000000000L);
      cpf = String.valueOf(elevenDigits);
    }

    return cpf;
  }

  private static boolean isValid(String cpf) {
    if (cpf.length() == 11) {
      if (cpf.equals("00000000000")
          || cpf.equals("11111111111")
          || cpf.equals("22222222222")
          || cpf.equals("33333333333")
          || cpf.equals("44444444444")
          || cpf.equals("55555555555")
          || cpf.equals("66666666666")
          || cpf.equals("77777777777")
          || cpf.equals("88888888888")
          || cpf.equals("99999999999")) {
        return false;
      }

      return calculateVerificationDigits(cpf).equals("00");
    }
    return false;
  }

  private static String calculateVerificationDigits(String num) {

    int firstDigit, secondDigit;
    int sum = 0, weight = 10;
    for (int i = 0; i < num.length(); i++) {
      sum += Integer.parseInt(num.substring(i, i + 1)) * weight--;
    }

    if (sum % 11 == 0 | sum % 11 == 1) {
      firstDigit = 0;
    } else {
      firstDigit = 11 - (sum % 11);
    }

    sum = 0;
    weight = 11;
    for (int i = 0; i < num.length(); i++) {

      sum += Integer.parseInt(num.substring(i, i + 1)) * weight--;
    }

    sum += firstDigit * 2;
    if (sum % 11 == 0 | sum % 11 == 1) {
      secondDigit = 0;
    } else {
      secondDigit = 11 - (sum % 11);
    }

    return String.valueOf(firstDigit) + String.valueOf(secondDigit);
  }
}
