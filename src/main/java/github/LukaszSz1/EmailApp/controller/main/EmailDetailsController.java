package github.LukaszSz1.EmailApp.controller.main;

import github.LukaszSz1.EmailApp.model.EmailManager;
import github.LukaszSz1.EmailApp.model.EmailMessage;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.net.URL;
import java.util.ResourceBundle;

class EmailDetailsController implements Initializable {

    @FXML
    private WebView webView;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label senderLabel;

    @FXML
    private HBox hBoxDownloads;

    @FXML
    private Label attachmentsLabel;

    private final String LOCATION_OF_DOWNLOAD = System.getProperty("user.home") + "/Downloads/";

    private final EmailManager emailManager;

    EmailDetailsController(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EmailMessage emailMessage = getEmailMessage();
        loadAttachments(emailMessage);
        MessageRendererService messageRendererService = new MessageRendererService(webView.getEngine());
        messageRendererService.setEmailMessage(emailMessage);
        messageRendererService.restart();
    }

    private EmailMessage getEmailMessage() {
        EmailMessage emailMessage = emailManager.getSelectedMessage();
        subjectLabel.setText(emailMessage.getSubject());
        senderLabel.setText(emailMessage.getSender());
        return emailMessage;
    }

    private void loadAttachments(EmailMessage emailMessage) {
        if (emailMessage.isHasAttachment()) {
            for (MimeBodyPart mimeBodyPart : emailMessage.getAttachmentList()) {
                AttachmentButton button = new AttachmentButton(mimeBodyPart);
                hBoxDownloads.getChildren().add(button);
            }
        } else {
            attachmentsLabel.setText("");
        }
    }

    private class AttachmentButton extends Button {

        private final MimeBodyPart mimeBodyPart;
        private String downloadedFilePath;

        public AttachmentButton(MimeBodyPart mimeBodyPart) {
            this.mimeBodyPart = mimeBodyPart;

            try {
                setText(mimeBodyPart.getFileName());
                downloadedFilePath = LOCATION_OF_DOWNLOAD + mimeBodyPart.getFileName();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            setOnAction(e -> downloadAttachment());
        }

        private void downloadAttachment() {
            colorBlue();
            Service<Void> service = new Service<>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<>() {
                        @Override
                        protected Void call() throws Exception {
                            mimeBodyPart.saveFile(downloadedFilePath);
                            return null;
                        }
                    };
                }
            };
            service.restart();

            service.setOnSucceeded(e -> colorGreen());
        }

        private void colorBlue() {
            setStyle("-fx-background-color: Blue");
        }

        private void colorGreen() {
            setStyle("-fx-background-color: Green");
        }
    }
}
