package github.LukaszSz1.EmailApp.controller.main;

import github.LukaszSz1.EmailApp.model.EmailMessage;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;


import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import java.io.IOException;

class MessageRendererService extends Service<Void> {

    private EmailMessage emailMessage;
    private final WebEngine webEngine;
    private final StringBuffer stringBuffer;

    MessageRendererService(WebEngine webEngine) {
        this.webEngine = webEngine;
        stringBuffer = new StringBuffer();
        this.setOnSucceeded(event -> displayMessage());
    }


    public void setEmailMessage(EmailMessage emailMessage) {
        this.emailMessage = emailMessage;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() {
                try {
                    loadMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }


    private void loadMessage() throws MessagingException, IOException {
        stringBuffer.setLength(0);
        Message message = emailMessage.getMessage();
        String contentType = message.getContentType();

        if (isSimpleType(contentType)) {
            stringBuffer.append(message.getContent().toString());
        } else if (isMultipartType(contentType)) {
            Multipart multipart = (Multipart) message.getContent();
            loadMultipart(multipart, stringBuffer);
        }
    }

    private void loadMultipart(Multipart multipart, StringBuffer stringBuffer) throws MessagingException, IOException {
        for (int i = multipart.getCount() - 1; i >= 0; i--) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            String contentType = bodyPart.getContentType();

            if (isSimpleType(contentType)) {
                stringBuffer.append(bodyPart.getContent().toString());
            } else if (isMultipartType(contentType)) {
                Multipart multipart2 = (Multipart) bodyPart.getContent();
                //recursion
                loadMultipart(multipart2, stringBuffer);
            } else if (!isTextPlain(contentType)) {
                MimeBodyPart mbp = (MimeBodyPart) bodyPart;
                emailMessage.addAttachment(mbp);
            }
        }
    }

    private void displayMessage() {
        webEngine.loadContent(stringBuffer.toString());
    }

    private boolean isSimpleType(String contentType) {
        return contentType.contains("TEXT/HTML") || contentType.contains("mixed") || contentType.contains("text");
    }

    private boolean isMultipartType(String contentType) {
        return contentType.contains("multipart");
    }

    private boolean isTextPlain(String contentType) {
        return contentType.contains("TEXT/PLAIN");
    }
}
