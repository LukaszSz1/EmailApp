package github.LukaszSz1.EmailApp;


import github.LukaszSz1.EmailApp.fxconfig.ApplicationStart;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class EmailApp {

    public static void main(String[] args) {
        Application.launch(ApplicationStart.class, args);
    }

}
