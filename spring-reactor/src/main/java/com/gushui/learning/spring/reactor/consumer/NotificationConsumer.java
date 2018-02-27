package com.gushui.learning.spring.reactor.consumer;

import com.gushui.learning.spring.reactor.domain.Notification;
import com.gushui.learning.spring.reactor.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.fn.Consumer;


/**
 * Created by Thomas on 2018/2/27.
 */
@Service
public class NotificationConsumer implements Consumer<Event<Notification>> {

  @Autowired
  NotificationService notificationService;

  @Override
  public void accept(Event<Notification> notificationEvent) {
    try {
      notificationService.immediatelyNotificate(notificationEvent.getData());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
