package github.LukaszSz1.EmailApp.properties;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IconReader {

    public Node getIconForFolder(String folderName) {
        String lowerCaseFolderName = folderName.toLowerCase();
        ImageView imageView;
        try {
            if (lowerCaseFolderName.contains("@")) {
                imageView = new ImageView(new Image(getClass().getResource("/icons/email.png").toExternalForm()));
            } else if (lowerCaseFolderName.contains("inbox")) {
                imageView = new ImageView(new Image(getClass().getResource("/icons/inbox.png").toExternalForm()));
            } else if (lowerCaseFolderName.contains("sent")) {
                imageView = new ImageView(new Image(getClass().getResource("/icons/sent2.png").toExternalForm()));
            } else if (lowerCaseFolderName.contains("spam")) {
                imageView = new ImageView(new Image(getClass().getResource("/icons/spam.png").toExternalForm()));
            } else {
                imageView = new ImageView(new Image(getClass().getResource("/icons/folder.png").toExternalForm()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        return imageView;
    }
}
