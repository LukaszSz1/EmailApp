package github.LukaszSz1.EmailApp.controller.login;

import github.LukaszSz1.EmailApp.model.EmailAccount;
import github.LukaszSz1.EmailApp.model.EmailManager;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

class LoginService extends Service<LoginMessage> {

    private final EmailAccount emailAccount;
    private final EmailManager emailManager;

    LoginService(EmailAccount emailAccount, EmailManager emailManager) {
        this.emailAccount = emailAccount;
        this.emailManager = emailManager;
    }

    @Override
    protected Task<LoginMessage> createTask() {
        return new Task<>() {
            @Override
            protected LoginMessage call() {
                return login();
            }
        };
    }

    private LoginMessage login() {
        Authenticator authenticator = getAuthenticator();

        try {
            Session session = setSession(authenticator);
            setStore(session);
            emailManager.addEmailAccount(emailAccount);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            return new LoginNetworkFailure();
        } catch (AuthenticationFailedException e) {
            e.printStackTrace();
            return new LoginCredentialFailure();
        } catch (Exception e) {
            e.printStackTrace();
            return new LoginUnexpectedErrorFailure();
        }
        return new LoginSuccessful();
    }

    private Authenticator getAuthenticator() {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount.getAddress(), emailAccount.getPassword());
            }
        };
    }

    private Session setSession(final Authenticator authenticator) {
        Session session = Session.getInstance(emailAccount.getProperties(), authenticator);
        emailAccount.setSession(session);
        return session;
    }

    private void setStore(final Session session) throws MessagingException {
        Store store = session.getStore("imaps");
        store.connect(emailAccount.getProperties().getProperty("incomingHost"), emailAccount.getAddress(), emailAccount.getPassword());
        emailAccount.setStore(store);
    }

}
