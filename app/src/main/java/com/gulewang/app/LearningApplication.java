package com.gulewang.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

/**
 * Created by Thomas on 17/4/5.
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = { LearningApplication.class})
public class LearningApplication implements ApplicationRunner {

  private static final Logger logger = LoggerFactory.getLogger(LearningApplication.class);

  @Autowired
  private ConfigurableApplicationContext context;

  public static void main(String[] args) {
    logger.info("LearningApplication starting ...");
    SpringApplication app = new SpringApplication(LearningApplication.class);

    app.run(args);
  }


  private void exitApp() {
    context.close();
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    List<String> subCmds = args.getNonOptionArgs();
    String cmd = subCmds.size() == 0 ? "web" : subCmds.get(0);
    boolean exit = true;
    switch (cmd) {
      case "user":
        logger.info("Start user call ...");
        break;
      case "web":
        exit = false;
        logger.info("Start listening as a web-service to serve API call ...");
        break;
      default:
        logger.error("A command argument is required to run this app.");
        break;
    }
    if (exit) {
      exitApp();
    }
  }
}
