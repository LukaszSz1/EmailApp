package github.LukaszSz1.EmailApp.model;

import github.LukaszSz1.EmailApp.properties.SizeInteger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.mail.Message;
import javax.mail.internet.MimeBodyPart;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EmailMessage {

    private final StringProperty subject;
    private final StringProperty sender;
    private final StringProperty recipient;
    private final ObjectProperty<SizeInteger> size;
    private final ObjectProperty<Date> date;
    private boolean isRead;
    private final Message message;
    private final List<MimeBodyPart> attachmentList = new ArrayList<>();
    private boolean hasAttachment = false;

    public EmailMessage(String subject, String sender, String recipient, int size, Date date, boolean isRead, Message message) {
        this.subject = new SimpleStringProperty(subject);
        this.sender = new SimpleStringProperty(sender);
        this.recipient = new SimpleStringProperty(recipient);
        this.size = new SimpleObjectProperty<>(new SizeInteger(size));
        this.date = new SimpleObjectProperty<>(date);
        this.isRead = isRead;
        this.message = message;
    }

    public void addAttachment(MimeBodyPart mbp) {
        hasAttachment = true;
        attachmentList.add(mbp);
    }

    public String getSubject() {
        return subject.get();
    }

    public String getSender() {
        return sender.get();
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Message getMessage() {
        return message;
    }

    public boolean isHasAttachment() {
        return hasAttachment;
    }

    public List<MimeBodyPart> getAttachmentList() {
        return attachmentList;
    }

    SizeInteger getSize() {
        return size.get();
    }

    public ObjectProperty<SizeInteger> sizeProperty() {
        return size;
    }

    Date getDate() {
        return date.get();
    }

    public ObjectProperty<Date> dateProperty() {
        return date;
    }

    public StringProperty subjectProperty() {
        return subject;
    }

    public StringProperty senderProperty() {
        return sender;
    }

    String getRecipient() {
        return recipient.get();
    }

    public StringProperty recipientProperty() {
        return recipient;
    }
}
