package studit.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController{

    @FXML  PasswordField passwordField;
    @FXML TextField usernameField;
    @FXML Button loginButton;

    public LoginController(){
    }

    public void initialize(){
        studit.core.LoginManager.initialize();
    }

    ///Checks if login creditentials are correct, and does an action corresponding
    public void loginButtonAction(){
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (studit.core.LoginManager.match(username, password)){
            System.out.print("Success ");
        }
        else{
            System.out.print("Failure, "+username+ ", "+password + " ");
        }
    }





}