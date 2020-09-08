package studit;

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
        LoginManager.initialize();
    }

    ///Gjør en handling når man trykker på login
    public void loginButtonAction(){
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (LoginManager.match(username, password)){
            System.out.print("Success");
        }
        else{
            System.out.print("Failure, "+username+ ", "+password + " ");
        }
    }





}