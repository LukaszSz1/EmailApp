package github.LukaszSz1.EmailApp.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class EmailTreeItem<String> extends TreeItem<String> {

    private final String name;
    private final ObservableList<EmailMessage> emailMessages;
    private int unreadMessageCounter;

    public EmailTreeItem(String name) {
        super(name);
        this.name = name;
        this.emailMessages = FXCollections.observableArrayList();
        this.unreadMessageCounter = 0;
    }

    public void addEmail(Message message) throws MessagingException {
        EmailMessage emailMessage = fetchMessage(message);
        emailMessages.add(emailMessage);
    }

    private EmailMessage fetchMessage(Message message) throws MessagingException {
        boolean messageIsRead = message.getFlags().contains(Flags.Flag.SEEN);

        EmailMessage emailMessage = new EmailMessage(
                message.getSubject(),
                message.getFrom()[0].toString(),
                message.getRecipients(MimeMessage.RecipientType.TO)[0].toString(),
                message.getSize(),
                message.getSentDate(),
                messageIsRead,
                message
        );
        incrementUnreadMessages(messageIsRead);

        return emailMessage;
    }

    private void incrementUnreadMessages(final boolean messageIsRead) {
        if (messageIsRead) {
            incrementMessagesCount();
        }
    }

    public void incrementMessagesCount() {
        unreadMessageCounter++;
        updateName();
    }

    public void decrementMessagesCount() {
        unreadMessageCounter--;
        updateName();
    }

    private void updateName() {
        if (unreadMessageCounter > 0) {
            this.setValue((String) (name + "[" + unreadMessageCounter + "]"));
        } else {
            this.setValue(name);
        }
    }

    public void addEmailToTop(Message message) throws MessagingException {
        EmailMessage emailMessage = fetchMessage(message);
        emailMessages.add(0, emailMessage);
    }

    public ObservableList<EmailMessage> getEmailMessages() {
        return emailMessages;
    }

}
