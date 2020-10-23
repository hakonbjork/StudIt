package studit.ui;

import java.io.IOException;
import java.lang.System.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import studit.core.mainpage.Comment;


 public class CommentListCell extends ListCell<Comment> { 

    @FXML
    private Label lbTitle;

    @FXML
    private Label lbDescription;

    @FXML
    private AnchorPane anchorPane;  

    private FXMLLoader mLLoader;

 
    @Override
    public void updateItem(Comment pos,boolean empty){
    super.updateItem(pos, empty);

      if(pos == null){
        setText(null);
        setGraphic(null);
        }else{

      if (mLLoader == null) {
            mLLoader = new FXMLLoader(getClass().getResource("CommentCell.fxml"));
            mLLoader.setController(this);

            try {
                mLLoader.load();
            } catch (IOException e) {
            }
      } 

      this.lbTitle.setText(pos.getBrukernavn());
      this.lbDescription.setText(pos.getKommentar());

     setText("kommentar");
     setGraphic(anchorPane);


}

}
}