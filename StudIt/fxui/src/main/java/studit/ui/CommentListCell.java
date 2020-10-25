package studit.ui;

import java.io.IOException;
import java.lang.System.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import studit.core.mainpage.Comment;


 public class CommentListCell extends ListCell<Comment> {

    @FXML
    private Label titleLabel;

    @FXML
    private Label commentLabel;

    @FXML
    private Label descriptionLabel;

    public CommentListCell() {
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CommentCell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Comment item, boolean empty) {
        super.updateItem(item, empty);

        if(empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {
            titleLabel.setText(item.getBrukernavn());
            commentLabel.setText(item.getKommentar());
            descriptionLabel.setText(item.getKommentar());

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}