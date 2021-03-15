package github.LukaszSz1.EmailApp.controller.send;

class SendingFailedByProvider implements SendingMessage{
    @Override
    public String message() {
        return "Provider error!";
    }
}
