package org.claimsystem.g24fp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.claimsystem.g24fp.model.UserDB;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button closeBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private AnchorPane mainForm;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    public void close() {
        System.exit(0);
    }

    public void loginAccount() throws SQLException, IOException {
        Alert alert;
        // get userinfo result set
        // if empty
        // if not match
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields!");
            alert.showAndWait();
        } else {

            UserDB userDB = new UserDB();
            ResultSet rs = userDB.login(username.getText(), password.getText());
            if (rs.next()) {
                UserDB.setCurrentUser(new String[]{rs.getString("id"), rs.getString("password")});
                // if user is admin then go to admin page
                // if user is customer then go to customer page
                // if user is provider then go to provider page
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Login Successful!");
                alert.showAndWait();

                loginBtn.getScene().getWindow().hide();
                String userType = rs.getString("user_type");
                Parent root = null;
                if (userType.equals("admin")) {
                    // go to admin page
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/admin/Admin.fxml")));
                } else if (userType.equals("customer")) {
                    // go to customer page
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/customer/Customer.fxml")));
                } else if (userType.equals("provider")) {
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/provider/Provider.fxml")));
                    // go to provider page
                }
                Stage stage =  new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } else {
                // display the error message
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password!");
                alert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
