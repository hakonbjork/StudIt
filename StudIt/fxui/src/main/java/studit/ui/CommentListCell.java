package studit.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import studit.core.mainpage.Comment;

public class CommentListCell extends ListCell<Comment> {

  HBox hbox = new HBox();

  Button upBtn = new Button("UpVote");

  Button downBtn = new Button("DownVote");

  Label textView  = new Label("textView");

  @Override
  protected void updateItem(Comment item, boolean empty) {
    super.updateItem(item, empty);

    if(empty || item == null) {

            setText(null);
            setGraphic(null);

    } else {

      
    }

    }
      

  }



   