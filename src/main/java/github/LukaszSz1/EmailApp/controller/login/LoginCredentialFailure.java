package github.LukaszSz1.EmailApp.controller.login;

class LoginCredentialFailure implements LoginMessage {
    @Override
    public String message() {
        return "Invalid credentials";
    }
}
