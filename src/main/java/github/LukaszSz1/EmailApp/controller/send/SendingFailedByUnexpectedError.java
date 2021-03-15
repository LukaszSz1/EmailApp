package github.LukaszSz1.EmailApp.controller.send;

class SendingFailedByUnexpectedError implements SendingMessage {
    @Override
    public String message() {
        return "Unexpected error!";
    }
}
