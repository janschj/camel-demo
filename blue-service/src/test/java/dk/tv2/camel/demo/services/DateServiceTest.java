package dk.tv2.camel.demo.services;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

import dk.tv2.camel.demo.services.LoginService;

public class DateServiceTest {

  @Test
  public void test() {
    LocalDate date = new LoginService().getDate();
    assertEquals(LocalDate.class, date.getClass());
  }

}
