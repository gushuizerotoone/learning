package com.gushui.learning.spring.reactor.controller;

import com.gushui.learning.spring.reactor.domain.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.bus.Event;
import reactor.bus.EventBus;

/**
 * Created by Thomas on 2018/2/27.
 */
@RestController
public class NotificationController {

  @Autowired
  private EventBus eventBus;

  @GetMapping("/startNotification/{param}")
  public String startNotification(@PathVariable Integer param) {
    for (int i = 0; i < param; i++) {
      Notification notification = new Notification();
      notification.setId(new Long(i));

      eventBus.notify("notificationConsumer", Event.wrap(notification));
      System.out.println("Notification " + i + ": notification task submitted successfully");
    }
    return "notification complete";
  }

}
