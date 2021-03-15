package github.LukaszSz1.EmailApp.controller.send;

class EmailSendSuccess implements SendingMessage {
    @Override
    public String message() {
        return "Message sent successfully";
    }
}
