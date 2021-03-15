package github.LukaszSz1.EmailApp.controller.login;

class LoginSuccessful implements LoginMessage {
    @Override
    public String message() {
        return "Login successful";
    }
}
