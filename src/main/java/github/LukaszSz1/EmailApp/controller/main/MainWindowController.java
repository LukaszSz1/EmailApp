package github.LukaszSz1.EmailApp.controller.main;

import github.LukaszSz1.EmailApp.controller.send.CreateMessageController;
import github.LukaszSz1.EmailApp.model.EmailManager;
import github.LukaszSz1.EmailApp.model.EmailMessage;
import github.LukaszSz1.EmailApp.model.EmailTreeItem;
import github.LukaszSz1.EmailApp.properties.FXMLPath;
import github.LukaszSz1.EmailApp.properties.SizeInteger;
import github.LukaszSz1.EmailApp.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.util.Callback;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    private TreeView<String> emailsTreeView;

    @FXML
    private TableView<EmailMessage> emailsTableView;

    @FXML
    private WebView emailWebView;

    @FXML
    private TableColumn<EmailMessage, String> senderCol;

    @FXML
    private TableColumn<EmailMessage, String> subjectCol;

    @FXML
    private TableColumn<EmailMessage, String> recipientCol;

    @FXML
    private TableColumn<EmailMessage, SizeInteger> sizeCol;

    @FXML
    private TableColumn<EmailMessage, Date> dataCol;

    private MessageRendererService messageRendererService;

    private final MenuItem markUnreadMenuItem = new MenuItem("Mark as unread");

    private final MenuItem deleteMessageMenuItem = new MenuItem("Delete Message");

    private final MenuItem shoeMessageDetailsMenuItem = new MenuItem("View details");

    private final EmailManager emailManager;

    private final ViewFactory viewFactory;

    public MainWindowController(EmailManager emailManager, ViewFactory viewFactory) {
        this.emailManager = emailManager;
        this.viewFactory = viewFactory;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpEmailsTreeView();
        setUpTableView();
        setUpFolderSelection();
        setUpBoldRows();
        setUpMessageRenderService();
        setUpMessageSelection();
        setUpContextMenus();
    }

    private void setUpEmailsTreeView() {
        emailsTreeView.setRoot(emailManager.getFoldersRoot());
        emailsTreeView.setShowRoot(false);
    }

    private void setUpTableView() {
        senderCol.setCellValueFactory(new PropertyValueFactory<>("sender"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        recipientCol.setCellValueFactory(new PropertyValueFactory<>("recipient"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        dataCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        emailsTableView.setContextMenu(new ContextMenu(markUnreadMenuItem, deleteMessageMenuItem, shoeMessageDetailsMenuItem));
    }

    private void setUpFolderSelection() {
        emailsTreeView.setOnMouseClicked(e -> {
            EmailTreeItem<String> item = (EmailTreeItem<String>) emailsTreeView.getSelectionModel().getSelectedItem();
            if (item != null) {
                emailManager.setSelectedFolder(item);
                emailsTableView.setItems(item.getEmailMessages());
            }
        });
    }

    private void setUpBoldRows() {
        emailsTableView.setRowFactory(new Callback<>() {
            @Override
            public TableRow<EmailMessage> call(TableView<EmailMessage> emailMessageTableView) {
                return new TableRow<>() {
                    @Override
                    protected void updateItem(EmailMessage item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            if (item.isRead()) {
                                setStyle("");
                            } else {
                                setStyle("-fx-font-weight: bold");
                            }
                        }
                    }
                };
            }
        });
    }

    private void setUpMessageRenderService() {
        messageRendererService = new MessageRendererService(emailWebView.getEngine());
    }

    private void setUpMessageSelection() {
        emailsTableView.setOnMouseClicked(event -> {
            EmailMessage emailMessage = emailsTableView.getSelectionModel().getSelectedItem();
            if (emailMessage != null) {
                emailManager.setSelectedMessage(emailMessage);
                if (!emailMessage.isRead()) {
                    emailManager.setMessageRead();
                }

                emailManager.setSelectedMessage(emailMessage);
                messageRendererService.setEmailMessage(emailMessage);
                messageRendererService.restart();
            }
        });

    }

    private void setUpContextMenus() {
        markUnreadMenuItem.setOnAction(event -> emailManager.setMessageUnRead());

        deleteMessageMenuItem.setOnAction(event -> {
            emailManager.deleteSelectedMessage();
            emailWebView.getEngine().loadContent("");
        });

        shoeMessageDetailsMenuItem.setOnAction(actionEvent -> viewFactory.displayDialog(new EmailDetailsController(emailManager),
                emailsTreeView.getScene().getWindow(),
                FXMLPath.EMAIL_DETAILS_WINDOW_FXML.toString()));
    }

    @FXML
    public void createNewMessageAction() {
        viewFactory.displayDialog(new CreateMessageController(emailManager, viewFactory),
                emailsTreeView.getScene().getWindow(),
                FXMLPath.CREATE_MESSAGE_WINDOW_FXML.toString());
    }

}
