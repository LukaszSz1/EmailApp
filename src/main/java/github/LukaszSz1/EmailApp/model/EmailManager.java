package github.LukaszSz1.EmailApp.model;

import github.LukaszSz1.EmailApp.controller.main.FetchFolderService;
import github.LukaszSz1.EmailApp.properties.IconReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import org.springframework.stereotype.Component;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

@Component
public class EmailManager {
    private final EmailTreeItem<String> foldersRoot = new EmailTreeItem<>("");
    private final ObservableList<EmailAccount> emailAccounts = FXCollections.observableArrayList();
    private EmailTreeItem<String> selectedFolder;
    private EmailMessage selectedMessage;
    private final IconReader iconReader = new IconReader();
    private final List<Folder> folderList = new ArrayList<>();

    public EmailManager() {
    }

    public void addEmailAccount(EmailAccount emailAccount) {
        emailAccounts.add(emailAccount);
        EmailTreeItem<String> treeItem = createEmailTreeItem(emailAccount);
        fetchFolderServiceInitialization(emailAccount, treeItem);
        addItemToFolder(treeItem);
    }

    private EmailTreeItem<String> createEmailTreeItem(final EmailAccount emailAccount) {
        EmailTreeItem<String> treeItem = new EmailTreeItem<>(emailAccount.getAddress());
        treeItem.setGraphic(iconReader.getIconForFolder(emailAccount.getAddress()));
        return treeItem;
    }

    private void fetchFolderServiceInitialization(final EmailAccount emailAccount, final EmailTreeItem<String> treeItem) {
        FetchFolderService fetchFolderService = new FetchFolderService(emailAccount.getStore(), treeItem, folderList);
        fetchFolderService.start();
    }

    private void addItemToFolder(final EmailTreeItem<String> treeItem) {
        foldersRoot.getChildren().add(treeItem);
    }

    public void setMessageRead() {
        try {
            selectedMessage.setRead(true);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, true);
            selectedFolder.decrementMessagesCount();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void setMessageUnRead() {
        try {
            selectedMessage.setRead(false);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, false);
            selectedFolder.incrementMessagesCount();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void deleteSelectedMessage() {
        try {
            selectedMessage.getMessage().setFlag(Flags.Flag.DELETED, true);
            selectedFolder.getEmailMessages().remove(selectedMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public TreeItem<String> getFoldersRoot() {
        return foldersRoot;
    }

    public ObservableList<EmailAccount> getEmailAccounts() {
        return emailAccounts;
    }

    public EmailMessage getSelectedMessage() {
        return selectedMessage;
    }

    public void setSelectedMessage(EmailMessage selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public void setSelectedFolder(EmailTreeItem<String> selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public List<Folder> getFolderList() {
        return folderList;
    }
}
