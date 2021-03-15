package github.LukaszSz1.EmailApp.fxconfig;

import github.LukaszSz1.EmailApp.EmailApp;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

public class ApplicationStart extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(EmailApp.class).run();
    }

    @Override
    public void start(Stage primaryStage) {
        applicationContext.publishEvent(new StageReadyEvent(primaryStage));
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }

   static class StageReadyEvent extends ApplicationEvent {

        public Stage getStage() {
            return (Stage) getSource();
        }

        public StageReadyEvent(Object source) {
            super(source);

        }
    }
}
