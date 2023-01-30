package ru.stepanov.pharmacymanager;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class PharmacyManagerApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(PharmacyManagerApplication.class).headless(false).run(args);
  }
}
