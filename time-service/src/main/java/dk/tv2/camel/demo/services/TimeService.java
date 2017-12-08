package dk.tv2.camel.demo.services;

import java.time.LocalTime;

public class TimeService {
  public LocalTime getTime() {
    return LocalTime.now();
  }
}