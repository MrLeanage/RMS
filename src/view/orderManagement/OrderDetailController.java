package view.orderManagement;

import bean.Employee;
import bean.Menu;
import bean.Order;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.EmployeeService;
import service.OrderService;
import utility.dataHandler.DataValidation;
import utility.popUp.AlertPopUp;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDetailController implements Initializable {
    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private TableView<Order> orderTable;

    @FXML
    private TableColumn<Order, String> IDColumn;

    @FXML
    private TableColumn<Order, String> customerNameColumn;

    @FXML
    private TableColumn<Order, String> menuNameColumn;

    @FXML
    private TableColumn<Order, String> notesColumn;

    @FXML
    private TableColumn<Order, Integer> quantityColumn;

    @FXML
    private TableColumn<Order, String> statusColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField menuIDTextField;

    @FXML
    private JFXButton updateButton;

    @FXML
    private JFXButton addButton;

    @FXML
    private Label menuIDLabel;

    @FXML
    private TextArea noteTextArea;

    @FXML
    private Label quantityLabel;

    @FXML
    private Spinner<Integer> quantitySpinner;

    @FXML
    private TextField customerNameTextField;

    @FXML
    private Label customerNameLabel;

    @FXML
    private Label noteLabel;

    @FXML
    private ChoiceBox<String> statusChoiceBox;

    public static Menu selectedMenu = null;

    private static Order selectedOrder = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();

        SpinnerValueFactory<Integer> quantityValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        quantitySpinner.setValueFactory(quantityValueFactory);

        ObservableList<String> statusList = FXCollections.observableArrayList("Pending", "Processing", "Delivered");
        statusChoiceBox.setValue("Pending");
        statusChoiceBox.setItems(statusList);


    }

    private void loadData() {
        OrderService orderService = new OrderService();
        ObservableList<Order> orderObservableList = orderService.loadAllOrderData();

        IDColumn.setCellValueFactory(new PropertyValueFactory<>("oID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("oCustomerName"));
        menuNameColumn.setCellValueFactory(new PropertyValueFactory<>("oMenuName"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("oNotes"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("oQuantity"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("oStatus"));

        orderTable.setItems(null);
        orderTable.setItems(orderObservableList);

        searchTable();
    }

    public void searchTable() {

        OrderService orderService = new OrderService();

        SortedList<Order> sortedData = orderService.searchTable(searchTextField);

        //binding the SortedList to TableView
        sortedData.comparatorProperty().bind(orderTable.comparatorProperty());
        //adding sorted and filtered data to the table
        orderTable.setItems(sortedData);
    }

    @FXML
    void addOrder(ActionEvent event) {

        clearLabels();
        if (orderValidation()) {
            Order order = new Order();
            OrderService orderService = new OrderService();

            order.setoMenuID(menuIDTextField.getText());
            order.setoCustomerName(customerNameTextField.getText());
            order.setoQuantity(quantitySpinner.getValue());
            order.setoNotes(noteTextArea.getText());
            order.setoStatus(statusChoiceBox.getValue());


            if (orderService.insertOrderData(order)) {
                AlertPopUp.insertSuccesfully("Order");
                loadData();
                searchTable();
                clearFields(event);
            } else
                AlertPopUp.insertionFailed("Order");

        } else
            orderValidationMessage();
    }

    @FXML
    void updateOrder(ActionEvent event) {
        clearLabels();
        if (orderValidation()) {
            Order order = new Order();
            OrderService orderService = new OrderService();

            order.setoID(selectedOrder.getoID());
            order.setoMenuID(menuIDTextField.getText());
            order.setoCustomerName(customerNameTextField.getText());
            order.setoQuantity(quantitySpinner.getValue());
            order.setoNotes(noteTextArea.getText());
            order.setoStatus(statusChoiceBox.getValue());

            if (orderService.updateOrderData(order)) {
                AlertPopUp.updateSuccesfully("Order");
                loadData();
                searchTable();
                clearFields(event);
            } else
                AlertPopUp.updateFailed("Order");
        } else
            orderValidationMessage();
    }

    @FXML
    private void deleteOrder(ActionEvent event) {
        if (selectedOrder != null) {
            OrderService orderService = new OrderService();
            Optional<ButtonType> action = AlertPopUp.deleteConfirmation("Order");
            if (action.get() == ButtonType.OK) {
                if (orderService.deleteOrderData((selectedOrder.getoID()))) {
                    AlertPopUp.deleteSuccesfull("Order");
                    loadData();
                    searchTable();
                    clearFields(event);
                } else
                    AlertPopUp.deleteFailed("Order");
            } else
                loadData();
        } else {
            AlertPopUp.selectRowToDelete("Order");
        }
    }

    @FXML
    void setSelectedOrderDataToFields(MouseEvent event) {
        try {
            clearLabels();
            addButton.setVisible(false);
            updateButton.setVisible(true);

            selectedOrder = orderTable.getSelectionModel().getSelectedItem();
            menuIDTextField.setText(selectedOrder.getoMenuID());
            customerNameTextField.setText(selectedOrder.getoCustomerName());
            quantitySpinner.getValueFactory().setValue(selectedOrder.getoQuantity());
            noteTextArea.setText(selectedOrder.getoNotes());
            statusChoiceBox.setValue(selectedOrder.getoStatus());
        } catch (NullPointerException exception) {

        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        menuIDTextField.setText("");
        customerNameTextField.setText("");
        noteTextArea.setText("");
        resetComponents();
        clearLabels();
        selectedOrder = null;
    }

    private void clearLabels() {
        menuIDLabel.setText("");
        customerNameLabel.setText("");
        noteLabel.setText("");
    }

    private boolean orderValidation() {
        return DataValidation.TextFieldNotEmpty(menuIDTextField.getText())
                && DataValidation.TextFieldNotEmpty(customerNameTextField.getText())

                && DataValidation.isValidMaximumLength(customerNameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(noteTextArea.getText(), 400);
    }

    private void orderValidationMessage() {

        if (!(DataValidation.TextFieldNotEmpty(menuIDTextField.getText())
                && DataValidation.TextFieldNotEmpty(customerNameTextField.getText()))) {
            DataValidation.TextFieldNotEmpty(menuIDTextField.getText(), menuIDLabel, "Please Select a Menu!");
            DataValidation.TextFieldNotEmpty(customerNameTextField.getText(), customerNameLabel, "Customer Name Required!");

        }
        if (!(DataValidation.isValidMaximumLength(customerNameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(noteTextArea.getText(), 400))) {

            DataValidation.isValidMaximumLength(customerNameTextField.getText(), 45, customerNameLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(noteTextArea.getText(), 400, noteLabel, "Field Limit 400 Exceeded!");
        }
    }
    private void resetComponents() {
        quantitySpinner.getValueFactory().setValue(1);
        addButton.setVisible(true);
        updateButton.setVisible(false);
    }

    @FXML
    private void browseMenu(ActionEvent actionEvent){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("menuPopUp.fxml"));
        try{
            loader.load();
        }catch (IOException ex){
            Logger.getLogger(MenuPopUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Parent p = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(p));
        stage.setResizable(false);
        stage.sizeToScene();

        stage.showAndWait();

        if(selectedMenu != null)
            menuIDTextField.setText(selectedMenu.getmID());
    }

    public void setMenu(Menu menu){
        selectedMenu = menu;
        menuIDTextField.setText(selectedMenu.getmID());
    }
}
