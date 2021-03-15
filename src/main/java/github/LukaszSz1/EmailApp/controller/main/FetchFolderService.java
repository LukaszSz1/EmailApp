package github.LukaszSz1.EmailApp.controller.main;

import github.LukaszSz1.EmailApp.model.EmailTreeItem;
import github.LukaszSz1.EmailApp.properties.IconReader;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import java.util.List;

public class FetchFolderService extends Service<Void> {

    private final Store store;
    private final EmailTreeItem<String> foldersRoot;
    private final List<Folder> folderList;
    private final IconReader iconReader = new IconReader();

    public FetchFolderService(Store store, EmailTreeItem<String> foldersRoot, List<Folder> folderList) {
        this.store = store;
        this.foldersRoot = foldersRoot;
        this.folderList = folderList;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                fetchFolders();
                return null;
            }
        };
    }

    private void fetchFolders() throws MessagingException {
        Folder[] folders = store.getDefaultFolder().list();
        handleFolders(folders, foldersRoot);
    }

    /**
     * Recursive method!
     * Iterates through folders add them to EmailTreeItems and add new messages to each folder
     */
    private void handleFolders(Folder[] folders, EmailTreeItem<String> foldersRoot) throws MessagingException {
        for (Folder folder : folders) {

            folderList.add(folder);

            EmailTreeItem<String> emailTreeItem = new EmailTreeItem<>(folder.getName());
            emailTreeItem.setGraphic(iconReader.getIconForFolder(folder.getName()));
            foldersRoot.getChildren().add(emailTreeItem);
            foldersRoot.setExpanded(true);

            fetchMessagesOnFolder(folder, emailTreeItem);

            addMessageListenerToFolder(folder, emailTreeItem);

            if (folder.getType() == Folder.HOLDS_FOLDERS) {
                Folder[] subFolders = folder.list();
                //recursion
                handleFolders(subFolders, emailTreeItem);
            }
        }
    }

    private void fetchMessagesOnFolder(Folder folder, EmailTreeItem<String> emailTreeItem) {
        Service<Object> fetchMessagesService = new Service<>() {
            @Override
            protected Task<Object> createTask() {
                return new Task<>() {
                    @Override
                    protected Object call() throws MessagingException {
                        if (folder.getType() != Folder.HOLDS_FOLDERS) {
                            folder.open(Folder.READ_WRITE);
                            int folderSize = folder.getMessageCount();
                            for (int i = folderSize; i > 0; i--) {
                                emailTreeItem.addEmail(folder.getMessage(i));
                            }
                        }
                        return null;
                    }
                };
            }
        };
        fetchMessagesService.start();
    }

    private void addMessageListenerToFolder(Folder folder, EmailTreeItem<String> emailTreeItem) {
        folder.addMessageCountListener(new MessageCountListener() {
            @Override
            public void messagesAdded(MessageCountEvent e) {
                for (int i = 0; i < e.getMessages().length; i++) {
                    try {
                        Message message = folder.getMessage(folder.getMessageCount() - i);
                        emailTreeItem.addEmailToTop(message);
                    } catch (MessagingException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            @Override
            public void messagesRemoved(MessageCountEvent e) {
            }
        });
    }
}


