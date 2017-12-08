package dk.tv2.camel.demo.services;

import java.time.LocalDate;

public class DateService {
  public LocalDate getDate() {
    return LocalDate.now();
  }
}
