package view.userManagement;

import bean.User;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import service.UserService;
import utility.dataHandler.DataValidation;
import utility.dataHandler.UtilityMethod;
import utility.popUp.AlertPopUp;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserDetailController implements Initializable {
    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> fNameColumn;

    @FXML
    private TableColumn<User, String> lNameColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField fNameTextField;

    @FXML
    private PasswordField pwPasswordField;

    @FXML
    private PasswordField confirmPWPasswordField;

    @FXML
    private TextField lNameTextField;

    @FXML
    private Label emailLabel;

    @FXML
    private Label fNameLabel;

    @FXML
    private Label lNameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXButton updateButton;

    private static User selectedUser = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        searchTable();
    }

    private void loadData() {
        UserService userService = new UserService();
        ObservableList<User> userObservableList = userService.loadAllUserData();

        emailColumn.setCellValueFactory(new PropertyValueFactory<>("uEmail"));
        fNameColumn.setCellValueFactory(new PropertyValueFactory<>("uFName"));
        lNameColumn.setCellValueFactory(new PropertyValueFactory<>("uLName"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("uPassword"));


        userTable.setItems(null);
        userTable.setItems(userObservableList);
    }

    public void searchTable() {

        UserService userService = new UserService();

        SortedList<User> sortedData = userService.searchTable(searchTextField);

        //binding the SortedList to TableView
        sortedData.comparatorProperty().bind(userTable.comparatorProperty());
        //adding sorted and filtered data to the table
        userTable.setItems(sortedData);
    }

    @FXML
    void addUser(ActionEvent event) {
        ObservableList<User> modelList = userTable.getItems();
        ArrayList<String> userList = new ArrayList<>();
        for (User user : modelList) {
            userList.add(user.getuEmail().toLowerCase());
        }

        clearLabels();
        if (userValidation() && !UtilityMethod.checkDataAvailability(userList, emailTextField.getText().toLowerCase())) {
            User user = new User();
            UserService userService = new UserService();

            user.setuEmail(emailTextField.getText());
            user.setuFName(fNameTextField.getText());
            user.setuLName(lNameTextField.getText());
            user.setuPassword(pwPasswordField.getText());


            if (userService.insertUserData(user)) {
                AlertPopUp.insertSuccesfully("User");
                loadData();
                searchTable();
                clearFields(event);
            } else
                AlertPopUp.insertionFailed("User");

        } else
            userValidationMessage();
    }

    @FXML
    void updateUser(ActionEvent event) {
        clearLabels();
        if (userValidation()) {
            User user = new User();
            UserService userService = new UserService();

            user.setuEmail(selectedUser.getuEmail());
            user.setuFName(fNameTextField.getText());
            user.setuLName(lNameTextField.getText());
            user.setuPassword(pwPasswordField.getText());

            if (userService.updateUserData(user)) {
                AlertPopUp.updateSuccesfully("User");
                loadData();
                searchTable();
                clearFields(event);
            } else
                AlertPopUp.updateFailed("User");
        } else
            userValidationMessage();
    }

    @FXML
    private void deleteUser(ActionEvent event) {
        if (selectedUser != null) {
            UserService userService = new UserService();
            Optional<ButtonType> action = AlertPopUp.deleteConfirmation("User");
            if (action.get() == ButtonType.OK) {
                if (userService.deleteUserData((selectedUser.getuEmail()))) {
                    AlertPopUp.deleteSuccesfull("User");
                    loadData();
                    searchTable();
                    clearFields(event);
                } else
                    AlertPopUp.deleteFailed("User");
            } else
                loadData();
        } else {
            AlertPopUp.selectRowToDelete("User");
        }
    }

    @FXML
    void setSelectedUserDataToFields(MouseEvent event) {
        try {
            clearLabels();
            addButton.setVisible(false);
            updateButton.setVisible(true);
            emailTextField.setEditable(false);
            selectedUser = userTable.getSelectionModel().getSelectedItem();
            emailTextField.setText(selectedUser.getuEmail());
            fNameTextField.setText(selectedUser.getuFName());
            lNameTextField.setText(selectedUser.getuLName());
        } catch (NullPointerException exception) {

        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        emailTextField.setText("");
        fNameTextField.setText("");
        lNameTextField.setText("");
        pwPasswordField.setText("");
        confirmPWPasswordField.setText("");
        resetComponents();
        clearLabels();
        selectedUser = null;
    }

    private void clearLabels() {
        emailLabel.setText("");
        passwordLabel.setText("");
        fNameLabel.setText("");
        lNameLabel.setText("");
    }

    private boolean userValidation() {


        return DataValidation.TextFieldNotEmpty(emailTextField.getText())
                && DataValidation.TextFieldNotEmpty(fNameTextField.getText())
                && DataValidation.TextFieldNotEmpty(lNameTextField.getText())
                && DataValidation.TextFieldNotEmpty(pwPasswordField.getText())
                && DataValidation.TextFieldNotEmpty(pwPasswordField.getText())

                && DataValidation.isValidMaximumLength(emailTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(fNameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(lNameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(pwPasswordField.getText(), 20)
                && DataValidation.isValidMaximumLength(confirmPWPasswordField.getText(), 20)

                && DataValidation.isValidEmail(emailTextField.getText())
                && DataValidation.PasswordFieldMatch(pwPasswordField, confirmPWPasswordField);


    }

    private void userValidationMessage() {
        ObservableList<User> modelList = userTable.getItems();
        ArrayList<String> userList = new ArrayList<>();
        for (User user : modelList) {
            userList.add(user.getuEmail().toLowerCase());
        }

        if (!(DataValidation.TextFieldNotEmpty(emailTextField.getText())
                && DataValidation.TextFieldNotEmpty(fNameTextField.getText())
                && DataValidation.TextFieldNotEmpty(lNameTextField.getText())
                && DataValidation.TextFieldNotEmpty(pwPasswordField.getText())
                && DataValidation.TextFieldNotEmpty(confirmPWPasswordField.getText()))) {
            DataValidation.TextFieldNotEmpty(emailTextField.getText(), emailLabel, "User Email Required!");
            DataValidation.TextFieldNotEmpty(fNameTextField.getText(), fNameLabel, "User First Name Required!");
            DataValidation.TextFieldNotEmpty(lNameTextField.getText(), lNameLabel, "User Last Name Required!");
            DataValidation.TextFieldNotEmpty(confirmPWPasswordField.getText(), passwordLabel, "User Confirm Password Required!");
            DataValidation.TextFieldNotEmpty(pwPasswordField.getText(), passwordLabel, "User Password Required!");

        }
        if (!(DataValidation.isValidMaximumLength(emailTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(fNameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(lNameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(pwPasswordField.getText(), 20)
                && DataValidation.isValidMaximumLength(confirmPWPasswordField.getText(), 20))) {

            DataValidation.isValidMaximumLength(emailTextField.getText(), 45, emailLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(fNameTextField.getText(), 45, fNameLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(lNameTextField.getText(), 45, lNameLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(pwPasswordField.getText(), 20, passwordLabel, "Field Limit 20 Exceeded!");
            DataValidation.isValidMaximumLength(confirmPWPasswordField.getText(), 20, passwordLabel, "Field Limit 20 Exceeded!");
        }
        if (!(DataValidation.isValidEmail(emailTextField.getText())
                && DataValidation.PasswordFieldMatch(pwPasswordField, confirmPWPasswordField))) {
            DataValidation.isValidEmail(emailTextField.getText(), emailLabel, "Invalid Email Address");
            DataValidation.PasswordFieldMatch(pwPasswordField, confirmPWPasswordField, passwordLabel, passwordLabel, "Password doesn't match");

        }
        if (!UtilityMethod.checkDataAvailability(userList, emailTextField.getText().toLowerCase())) {
            checkUserEmailAvailability();
        }
    }

    @FXML
    private void checkUserEmailAvailability() {
        ObservableList<User> modelList = userTable.getItems();
        ArrayList<String> userList = new ArrayList<>();
        for (User user : modelList) {
            userList.add(user.getuEmail().toLowerCase());
        }
        if (emailTextField.getText().isEmpty()) {
            emailLabel.setStyle("-fx-text-fill: #ff0000 ");
            emailLabel.setText("Email Cannot be empty");
        } else if (UtilityMethod.checkDataAvailability(userList, emailTextField.getText().toLowerCase()) && DataValidation.isValidEmail(emailTextField.getText())) {
            emailLabel.setStyle("-fx-text-fill: #ff0000 ");
            emailLabel.setText("Email Already Taken!!");
        } else {
            if (DataValidation.isValidEmail(emailTextField.getText())) {
                emailLabel.setStyle("-fx-text-fill: #00B605 ");
                emailLabel.setText("Email Available");
            } else {
                emailLabel.setStyle("-fx-text-fill: #ff0000 ");
                emailLabel.setText("Invalid Email Address!!");
            }

        }
    }

    private void resetComponents() {
        addButton.setVisible(true);
        updateButton.setVisible(false);
        emailTextField.setEditable(true);
    }

}
