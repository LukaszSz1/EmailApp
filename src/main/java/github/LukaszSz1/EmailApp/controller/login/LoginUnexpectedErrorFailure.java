package github.LukaszSz1.EmailApp.controller.login;

class LoginUnexpectedErrorFailure implements LoginMessage {

    @Override
    public String message() {
        return "Unexpected error";
    }
}
