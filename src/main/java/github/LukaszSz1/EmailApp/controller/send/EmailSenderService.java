package github.LukaszSz1.EmailApp.controller.send;

import github.LukaszSz1.EmailApp.model.EmailAccount;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.List;

class EmailSenderService extends Service<SendingMessage> {

    private final EmailAccount emailAccount;
    private final String subject;
    private final String recipient;
    private final String content;
    private final List<File> attachments;

    EmailSenderService(EmailAccount emailAccount, String subject, String recipient, String content, List<File> attachments) {
        this.emailAccount = emailAccount;
        this.subject = subject;
        this.recipient = recipient;
        this.content = content;
        this.attachments = attachments;
    }

    @Override
    protected Task<SendingMessage> createTask() {
        return new Task<>() {
            @Override
            protected SendingMessage call() {
                try {
                    MimeMessage mimeMessage = new MimeMessage(emailAccount.getSession());
                    Multipart multipart = new MimeMultipart();
                    createMessage(mimeMessage);
                    setMessageContent(mimeMessage, multipart);
                    addAttachment(multipart);
                    sendMessage(mimeMessage);
                    return new EmailSendSuccess();
                } catch (MessagingException e) {
                    e.printStackTrace();
                    return new SendingFailedByProvider();
                } catch (Exception e) {
                    e.printStackTrace();
                    return new SendingFailedByUnexpectedError();
                }
            }
        };
    }

    private void sendMessage(final MimeMessage mimeMessage) throws MessagingException {
        Transport transport = emailAccount.getSession().getTransport();
        transport.connect(emailAccount.getProperties().getProperty("outgoingHost"), emailAccount.getAddress(), emailAccount.getPassword());
        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        transport.close();
    }

    private void createMessage(MimeMessage mimeMessage) throws MessagingException {
        mimeMessage.setFrom(emailAccount.getAddress());
        mimeMessage.addRecipients(Message.RecipientType.TO, recipient);
        mimeMessage.setSubject(subject);
    }

    private void setMessageContent(MimeMessage mimeMessage, Multipart multipart) throws MessagingException {
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(content, "text/html");
        multipart.addBodyPart(messageBodyPart);
        mimeMessage.setContent(multipart);
    }

    private void addAttachment(Multipart multipart) throws MessagingException {
        if (attachments.size() > 0) {
            for (File file : attachments) {
                BodyPart mimeBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file.getAbsolutePath());
                mimeBodyPart.setDataHandler(new DataHandler(source));
                mimeBodyPart.setFileName(file.getName());
                multipart.addBodyPart(mimeBodyPart);
            }
        }
    }
}
