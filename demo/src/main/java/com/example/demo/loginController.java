package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.*;

public class loginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox residentCheckBox;

    @FXML
    private CheckBox managerCheckBox;

    private Connection connection;

    public void initialize() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlydancu", "root", "theanh2911");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            String query = "SELECT * FROM taikhoan WHERE TenDangNhap=? AND MatKhau=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String roles = resultSet.getString("VaiTro");

                String[] userRoles = roles.split(",");

                boolean isAdmin = false;
                boolean isUser = false;

                for (String role : userRoles) {
                    if (role.trim().equalsIgnoreCase("admin")) {
                        isAdmin = true;
                    } else if (role.trim().equalsIgnoreCase("user")) {
                        isUser = true;
                    }
                }

                if (isAdmin == managerCheckBox.isSelected() && isUser == residentCheckBox.isSelected()) {
                    showAlert("Login successful");
                    // Logic for successful login
                } else {
                    showAlert("Invalid roles selected!");
                }
            } else {
                showAlert("Invalid username or password!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void onClose() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
