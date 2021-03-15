package github.LukaszSz1.EmailApp.properties;

public enum FXMLPath {

    LOGIN_WINDOW_FXML("/LoginWindow.fxml"),
    MAIN_WINDOW_FXML("/MainWindow.fxml"),
    CREATE_MESSAGE_WINDOW_FXML("/CreateMessageWindow.fxml"),
    EMAIL_DETAILS_WINDOW_FXML("/EmailDetailsWindow.fxml");

    private final String path;

    FXMLPath(String path) {
        this.path = path;
    }

    public java.lang.String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return getPath();
    }
}
