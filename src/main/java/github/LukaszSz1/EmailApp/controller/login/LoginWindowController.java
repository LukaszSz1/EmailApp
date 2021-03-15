package github.LukaszSz1.EmailApp.controller.login;

import github.LukaszSz1.EmailApp.controller.main.MainWindowController;
import github.LukaszSz1.EmailApp.controller.main.FolderUpdaterService;
import github.LukaszSz1.EmailApp.model.EmailAccount;
import github.LukaszSz1.EmailApp.model.EmailManager;
import github.LukaszSz1.EmailApp.properties.FXMLPath;
import github.LukaszSz1.EmailApp.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class LoginWindowController implements Initializable {

    @FXML
    private TextField emailAddressField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label errorLabel;

    private final EmailManager emailManager;

    private final ViewFactory viewFactory;

    private final LoginWindowFieldsValidator validator = new LoginWindowFieldsValidator();

    LoginWindowController(EmailManager emailManager, ViewFactory viewFactory) {
        this.emailManager = emailManager;
        this.viewFactory = viewFactory;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailAddressField.setText("bogdan.kosecki6@gmail.com");
        passwordField.setText("1qaz2wsx.");
    }

    @FXML
    void loginButtonAction() {
        if (validator.isEmailAddressAndPasswordEmpty(emailAddressField.getText(), passwordField.getText())) {
            errorLabel.setText("Please fill email and password");
        } else if (validator.isEmailAddressEmpty(emailAddressField.getText())) {
            errorLabel.setText("Please fill email");
        } else if (validator.isPasswordEmpty(passwordField.getText())) {
            errorLabel.setText("Please fill password");
        } else {
            EmailAccount emailAccount = createEmailAccountWithGivenCredentials(emailAddressField.getText(), passwordField.getText());
            LoginService loginService = loginServiceInitialization(emailAccount);
            getLoginActionResult(loginService);
            folderUpdateServiceInitialization();
        }
    }

    private LoginService loginServiceInitialization(final EmailAccount emailAccount) {
        LoginService loginService = new LoginService(emailAccount, emailManager);
        loginService.start();
        return loginService;
    }

    private void folderUpdateServiceInitialization() {
        FolderUpdaterService folderUpdaterService = new FolderUpdaterService(emailManager.getFolderList());
        folderUpdaterService.start();
    }

    private EmailAccount createEmailAccountWithGivenCredentials(String address, String password) {
        return new EmailAccount(address, password);
    }

    private void getLoginActionResult(final LoginService loginService) {
        loginService.setOnSucceeded(e -> {
            LoginMessage emailLoginResult = loginService.getValue();
            if (emailLoginResult instanceof LoginSuccessful) {
                Stage stage = (Stage) errorLabel.getScene().getWindow();
                viewFactory.sceneInitializer(new MainWindowController(emailManager, viewFactory), FXMLPath.MAIN_WINDOW_FXML, stage);
            } else {
                errorLabel.setText(emailLoginResult.message());
            }
        });
    }
}
