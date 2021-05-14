package view.appHome;

import bean.User;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import utility.dataHandler.UserAuthentication;
import utility.navigation.MenuBarHandler;
import utility.navigation.Navigation;

import java.net.URL;
import java.util.ResourceBundle;

public class AppStageController implements Initializable {
    @FXML
    private AnchorPane baseAnchorPane;

    @FXML
    private JFXButton homeButton;

    @FXML
    private JFXButton menuButton;

    @FXML
    private JFXButton orderButton;

    @FXML
    private JFXButton employeeButton;

    @FXML
    private JFXButton userButton;

    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private Label userLabel;

    private Navigation navigation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = UserAuthentication.getAuthenticatedUser();
        userLabel.setText(user.getuFName() + " " +user.getuLName());

        navigation = new Navigation();
        loadHomeDetail();
    }

    private void loadHomeDetail() {
        navigation.loadHomeDetail(detailAnchorPane);
    }

    @FXML
    private void loadHomeDetail(ActionEvent actionEvent) {
        navigation.loadHomeDetail(detailAnchorPane);
    }

    @FXML
    private void logoutAccount(ActionEvent actionEvent) {
        navigation.logoutAccount(baseAnchorPane);
    }

    @FXML
    private void loadMenuDetail(ActionEvent actionEvent) {
        navigation.loadMenuDetail(detailAnchorPane);
    }
    @FXML
    private void loadOrderDetail(ActionEvent actionEvent) {
        navigation.loadOrderDetail(detailAnchorPane);
    }
    @FXML
    private void loadEmployeeDetail(ActionEvent actionEvent) {
        navigation.loadEmployeeDetail(detailAnchorPane);
    }
    @FXML
    private void loadUserDetail(ActionEvent actionEvent) {
        navigation.loadUserDetail(detailAnchorPane);
    }

    @FXML
    private void setSelection() {
        if (homeButton.isPressed())
            MenuBarHandler.setMenuNumber(0);
        else if (menuButton.isPressed())
            MenuBarHandler.setMenuNumber(1);
        else if (orderButton.isPressed())
            MenuBarHandler.setMenuNumber(2);
        else if (employeeButton.isPressed())
            MenuBarHandler.setMenuNumber(3);
        else if (userButton.isPressed())
            MenuBarHandler.setMenuNumber(4);
        else
            MenuBarHandler.setMenuNumber(0);
        setStyle();
    }

    public void setStyle() {
        String selectionColor = "-fx-background-color:   #6b4e00; ";
        resetButtons();
        switch (MenuBarHandler.getMenuNumber()) {
            case 0:
                homeButton.setStyle(selectionColor);
                break;
            case 1:
                menuButton.setStyle(selectionColor);
                break;
            case 2:
                orderButton.setStyle(selectionColor);
                break;
            case 3:
                employeeButton.setStyle(selectionColor);
                break;
            case 4:
                userButton.setStyle(selectionColor);
                break;
            default:
                homeButton.setStyle(selectionColor);

        }
    }

    private void resetButtons() {
        String defaultColor = "-fx-background-color:   #2e0000; ";
        homeButton.setStyle(defaultColor);
        menuButton.setStyle(defaultColor);
        orderButton.setStyle(defaultColor);
        employeeButton.setStyle(defaultColor);
        userButton.setStyle(defaultColor);
    }


}
