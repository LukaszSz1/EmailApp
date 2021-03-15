package github.LukaszSz1.EmailApp.controller.send;

import github.LukaszSz1.EmailApp.model.EmailAccount;
import github.LukaszSz1.EmailApp.model.EmailManager;
import github.LukaszSz1.EmailApp.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreateMessageController implements Initializable {

    @FXML
    private TextField recipientTextField;

    @FXML
    private TextField subjectTextField;

    @FXML
    private HTMLEditor htmlEditor;

    @FXML
    private Label errorLabel;

    @FXML
    private ChoiceBox<EmailAccount> emailAccountChoice;

    private final EmailManager emailManager;

    private final ViewFactory viewFactory;

    private final List<File> attachments = new ArrayList<>();

    public CreateMessageController(EmailManager emailManager, ViewFactory viewFactory) {
        this.emailManager = emailManager;
        this.viewFactory = viewFactory;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailAccountChoice.setItems(emailManager.getEmailAccounts());
        emailAccountChoice.setValue(emailManager.getEmailAccounts().get(0));
    }

    @FXML
    void sendButtonAction() {
        EmailSenderService emailSenderService = new EmailSenderService(
                emailAccountChoice.getValue(),
                subjectTextField.getText(),
                recipientTextField.getText(),
                htmlEditor.getHtmlText(),
                attachments);
        emailSenderService.start();

        emailSenderService.setOnSucceeded(e -> {
            SendingMessage emailSendingResult = emailSenderService.getValue();
            if (emailSendingResult instanceof EmailSendSuccess) {
                viewFactory.closeDialogWindow();
            } else {
                errorLabel.setText(emailSendingResult.message());
            }
        });
    }

    @FXML
    private void attacheButtonAction() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            attachments.add(selectedFile);
        }
    }
}
