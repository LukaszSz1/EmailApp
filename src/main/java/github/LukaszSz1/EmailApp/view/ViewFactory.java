package github.LukaszSz1.EmailApp.view;

import github.LukaszSz1.EmailApp.properties.FXMLPath;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ViewFactory {

    private final List<Dialog<ButtonType>> dialogList = new ArrayList<>();

    public void sceneInitializer(Initializable initializable, FXMLPath fxmlPath, Stage currentStage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath.toString()));
        Parent parent;
        // inject fx:controller
        fxmlLoader.setControllerFactory(aClass -> initializable);
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(parent);
        currentStage.setScene(scene);
        currentStage.show();
    }


    public void displayDialog(final Initializable initializable, final Window window, final String fxml) {

        // Dialog and FXMLLoader Object init
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(window);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        Parent parent;

        // inject fx:controller
        fxmlLoader.setControllerFactory(aClass -> initializable);
        try {
            parent = fxmlLoader.load();
            dialog.getDialogPane().setContent(parent);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        dialog.setResizable(true);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);
        dialog.show();

        addDialogToDialogList(dialog);
    }

    public void closeDialogWindow() {
        dialogList.get(0).close();
        dialogList.remove(0);
    }

    private void addDialogToDialogList(Dialog<ButtonType> dialog) {
        dialogList.add(dialog);
    }
}
