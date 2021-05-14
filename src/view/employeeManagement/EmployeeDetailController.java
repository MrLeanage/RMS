package view.employeeManagement;

import bean.Employee;
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
import service.EmployeeService;
import service.UserService;
import utility.dataHandler.DataValidation;
import utility.dataHandler.UtilityMethod;
import utility.popUp.AlertPopUp;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeDetailController implements Initializable {
    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, String> IDColumn;

    @FXML
    private TableColumn<Employee, String> nameColumn;

    @FXML
    private TableColumn<Employee, String> NICColumn;

    @FXML
    private TableColumn<Employee, Integer> phoneColumn;

    @FXML
    private TableColumn<Employee, String> designationColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField designationTextField;

    @FXML
    private Label nameLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label designationLabel;

    @FXML
    private TextField nicTextField;

    @FXML
    private Label nicLabel;

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXButton updateButton;

    private static Employee selectedEmployee = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }

    private void loadData() {
        EmployeeService employeeService = new EmployeeService();
        ObservableList<Employee> employeeObservableList = employeeService.loadAllEmployeeData();

        IDColumn.setCellValueFactory(new PropertyValueFactory<>("eID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("eName"));
        NICColumn.setCellValueFactory(new PropertyValueFactory<>("eNIC"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("eContactNo"));
        designationColumn.setCellValueFactory(new PropertyValueFactory<>("eDesignation"));

        employeeTable.setItems(null);
        employeeTable.setItems(employeeObservableList);

        searchTable();
    }

    public void searchTable() {

        EmployeeService employeeService = new EmployeeService();

        SortedList<Employee> sortedData = employeeService.searchTable(searchTextField);

        //binding the SortedList to TableView
        sortedData.comparatorProperty().bind(employeeTable.comparatorProperty());
        //adding sorted and filtered data to the table
        employeeTable.setItems(sortedData);
    }

    @FXML
    void addEmployee(ActionEvent event) {

        clearLabels();
        if (employeeValidation()) {
            Employee employee = new Employee();
            EmployeeService employeeService = new EmployeeService();

            employee.seteName(nameTextField.getText());
            employee.seteNIC(nicTextField.getText());
            employee.seteContactNo(Integer.valueOf(phoneTextField.getText()));
            employee.seteDesignation(designationTextField.getText());


            if (employeeService.insertEmployeeData(employee)) {
                AlertPopUp.insertSuccesfully("Employee");
                loadData();
                searchTable();
                clearFields(event);
            } else
                AlertPopUp.insertionFailed("Employee");

        } else
            employeeValidationMessage();
    }

    @FXML
    void updateEmployee(ActionEvent event) {
        clearLabels();
        if (employeeValidation()) {
            Employee employee = new Employee();
            EmployeeService employeeService = new EmployeeService();

            employee.seteID(selectedEmployee.geteID());
            employee.seteName(nameTextField.getText());
            employee.seteNIC(nicTextField.getText());
            employee.seteContactNo(Integer.valueOf(phoneTextField.getText()));
            employee.seteDesignation(designationTextField.getText());

            if (employeeService.updateEmployeeData(employee)) {
                AlertPopUp.updateSuccesfully("Employee");
                loadData();
                searchTable();
                clearFields(event);
            } else
                AlertPopUp.updateFailed("Employee");
        } else
            employeeValidationMessage();
    }

    @FXML
    private void deleteEmployee(ActionEvent event) {
        if (selectedEmployee != null) {
            EmployeeService employeeService = new EmployeeService();
            Optional<ButtonType> action = AlertPopUp.deleteConfirmation("Employee");
            if (action.get() == ButtonType.OK) {
                if (employeeService.deleteEmployeeData((selectedEmployee.geteID()))) {
                    AlertPopUp.deleteSuccesfull("Employee");
                    loadData();
                    searchTable();
                    clearFields(event);
                } else
                    AlertPopUp.deleteFailed("Employee");
            } else
                loadData();
        } else {
            AlertPopUp.selectRowToDelete("Employee");
        }
    }

    @FXML
    void setSelectedEmployeeDataToFields(MouseEvent event) {
        try {
            clearLabels();
            addButton.setVisible(false);
            updateButton.setVisible(true);

            selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
            nameTextField.setText(selectedEmployee.geteName());
            nicTextField.setText(selectedEmployee.geteNIC());
            phoneTextField.setText("0"+selectedEmployee.geteContactNo());
            designationTextField.setText(selectedEmployee.geteDesignation());
        } catch (NullPointerException exception) {

        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        nameTextField.setText("");
        nicTextField.setText("");
        phoneTextField.setText("");
        designationTextField.setText("");
        resetComponents();
        clearLabels();
        selectedEmployee = null;
    }

    private void clearLabels() {
        nameLabel.setText("");
        nicLabel.setText("");
        phoneLabel.setText("");
        designationLabel.setText("");
    }

    private boolean employeeValidation() {


        return DataValidation.TextFieldNotEmpty(nameTextField.getText())
                && DataValidation.TextFieldNotEmpty(nicTextField.getText())
                && DataValidation.TextFieldNotEmpty(phoneTextField.getText())
                && DataValidation.TextFieldNotEmpty(designationTextField.getText())

                && DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(nicTextField.getText(), 12)
                && DataValidation.isValidMaximumLength(phoneTextField.getText(), 10)
                && DataValidation.isValidMaximumLength(designationTextField.getText(), 30)

                && DataValidation.isValidNIC(nicTextField)
                && DataValidation.isValidPhoneNo(phoneTextField.getText());


    }

    private void employeeValidationMessage() {

        if (!(DataValidation.TextFieldNotEmpty(nameTextField.getText())
                && DataValidation.TextFieldNotEmpty(nicTextField.getText())
                && DataValidation.TextFieldNotEmpty(phoneTextField.getText())
                && DataValidation.TextFieldNotEmpty(designationTextField.getText()))) {
            DataValidation.TextFieldNotEmpty(nameTextField.getText(), nameLabel, "Employee Name Required!");
            DataValidation.TextFieldNotEmpty(nicTextField.getText(), nicLabel, "Employee NIC Required!");
            DataValidation.TextFieldNotEmpty(phoneTextField.getText(), phoneLabel, "Employee Contact Number Required!");
            DataValidation.TextFieldNotEmpty(designationTextField.getText(), designationLabel, "Employee designation Required!");

        }
        if (!(DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(nicTextField.getText(), 12)
                && DataValidation.isValidMaximumLength(phoneTextField.getText(), 10)
                && DataValidation.isValidMaximumLength(designationTextField.getText(), 30))) {

            DataValidation.isValidMaximumLength(nameTextField.getText(), 45, nameLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(nicTextField.getText(), 12, nicLabel, "Field Limit 12 Exceeded!");
            DataValidation.isValidMaximumLength(phoneTextField.getText(), 10, phoneLabel, "Field Limit 10 Exceeded!");
            DataValidation.isValidMaximumLength(designationTextField.getText(), 30, designationLabel, "Field Limit 30 Exceeded!");
        }
        if (!(DataValidation.isValidNIC(nicTextField)
                && DataValidation.isValidPhoneNo(phoneTextField.getText()))) {
            DataValidation.isValidNIC(nicTextField, nicLabel, "Invalid NIC !!");
            DataValidation.isValidPhoneNo(phoneTextField.getText(), phoneLabel, "Invalid Contact Number!!");
        }
    }
    private void resetComponents() {
        addButton.setVisible(true);
        updateButton.setVisible(false);
    }

}
