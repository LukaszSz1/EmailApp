package github.LukaszSz1.EmailApp.controller.login;

class LoginWindowFieldsValidator {

    boolean isEmailAddressEmpty(String address) {
        return address.isEmpty();
    }

    boolean isPasswordEmpty(String password) {
        return password.isEmpty();
    }

    boolean isEmailAddressAndPasswordEmpty(String address, String password) {
        return address.isEmpty() && password.isEmpty();

    }

}
