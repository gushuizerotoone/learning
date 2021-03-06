package com.gushui.learning.spring.drools.model;

/**
 * Created by Thomas on 2018/2/26.
 */
public class Fare {
  private Long nightSurcharge;
  private Long rideFare;

  public Fare() {
    this.nightSurcharge = 0L;
    this.rideFare = 0L;
  }

  public Long getNightSurcharge() {
    return nightSurcharge;
  }

  public void setNightSurcharge(Long nightSurcharge) {
    this.nightSurcharge = nightSurcharge;
  }

  public Long getRideFare() {
    return rideFare;
  }

  public void setRideFare(Long rideFare) {
    this.rideFare = rideFare;
  }

  public Long getTotalFare() {
    return nightSurcharge + rideFare;
  }
}
