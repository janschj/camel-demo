package dk.tv2.camel.demo.services;

import static org.junit.Assert.assertEquals;

import java.time.LocalTime;

import org.junit.Test;

import dk.tv2.camel.demo.services.TimeService;

public class TimeServiceTest {

  @Test
  public void canGetTime() {
    Object time = new TimeService().getTime();
    assertEquals(LocalTime.class, time.getClass());
  }

}
