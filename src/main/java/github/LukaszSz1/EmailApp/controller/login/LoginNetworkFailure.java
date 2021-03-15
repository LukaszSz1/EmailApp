package github.LukaszSz1.EmailApp.controller.login;

class LoginNetworkFailure implements LoginMessage {
    @Override
    public String message() {
        return "Network error";
    }
}
