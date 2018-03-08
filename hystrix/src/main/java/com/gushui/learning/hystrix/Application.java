package com.gushui.learning.hystrix;

import com.netflix.hystrix.contrib.sample.stream.HystrixConfigSseServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;

/**
 * Created by Thomas on 2018/3/7.
 */
@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  public ServletRegistrationBean adminServletRegistrationBean() {
    return new ServletRegistrationBean(new HystrixConfigSseServlet(), "/hystrix.stream");
  }
}
