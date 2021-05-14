package view.menuManagement;

import bean.Employee;
import bean.Menu;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import service.EmployeeService;
import service.MenuService;
import utility.dataHandler.DataValidation;
import utility.popUp.AlertPopUp;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuDetailController implements Initializable {
    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private TableView<Menu> menuTable;

    @FXML
    private TableColumn<Menu, String> IDColumn;

    @FXML
    private TableColumn<Menu, ImageView> imageColumn;

    @FXML
    private TableColumn<Menu, String> nameColumn;

    @FXML
    private TableColumn<Menu, String> infoColumn;

    @FXML
    private TableColumn<Menu, Double> priceColumn;

    @FXML
    private TableColumn<Menu, String> availabilityColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private Label nameLabel;

    @FXML
    private Label infoLabel;

    @FXML
    private Label menuImageLabel;

    @FXML
    private TextField priceTextField;

    @FXML
    private Label priceLabel;

    @FXML
    private ImageView menuImageView;

    @FXML
    private TextArea infoTextArea;

    @FXML
    private ChoiceBox<String> availabilityChoiceBox;

    @FXML
    private JFXButton updateButton;

    @FXML
    private JFXButton addButton;

    private static File staticFile;

    private static Menu selectedMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> choiceBoxList = FXCollections.observableArrayList("Available", "Not Available");
        availabilityChoiceBox.setValue("Available");
        availabilityChoiceBox.setItems(choiceBoxList);
        loadData();

    }

    private void loadData() {
        MenuService menuService = new MenuService();
        ObservableList<Menu> menuObservableList = menuService.loadAllMenuData();

        IDColumn.setCellValueFactory(new PropertyValueFactory<>("mID"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("mImage"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("mName"));
        infoColumn.setCellValueFactory(new PropertyValueFactory<>("mInfo"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("mPrice"));
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("mAvailability"));

        priceColumn.setCellFactory(tc -> new TableCell<Menu, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty) ;
                if (empty) {
                    setText(null);
                } else {
                    setText("Rs "+ new DecimalFormat("#####.00").format(value));
                    setAlignment(Pos.valueOf("TOP_RIGHT"));
                }
            }
        });

        imageColumn.setCellFactory(param -> {
            //Set up the ImageView
            final ImageView imageview = new ImageView();
            imageview.setFitHeight(80);
            imageview.setFitWidth(80);

            //Set up the Table
            TableCell<Menu, ImageView> cell = new TableCell<Menu, ImageView>() {
                public void updateItem(ImageView item, boolean empty) {
                    if(item != null){
                        if(!empty){
                            imageview.setImage(item.getImage());
                        }
                    }
                }
            };
            // Attach the imageview to the cell
            cell.setGraphic(null);
            cell.setGraphic(imageview);
            return cell;
        });
        menuTable.setItems(null);
        menuTable.setItems(menuObservableList);
        searchTable();
    }

    public void searchTable() {
        MenuService menuService = new MenuService();

        SortedList<Menu> sortedData = menuService.searchTable(searchTextField);

        //binding the SortedList to TableView
        sortedData.comparatorProperty().bind(menuTable.comparatorProperty());
        //adding sorted and filtered data to the table
        menuTable.setItems(sortedData);
    }

    @FXML
    void addMenu(ActionEvent event) {

        clearLabels();
        if (menuValidation()) {
            Menu menu = new Menu();
            MenuService menuService = new MenuService();


            menu.setmName(nameTextField.getText());
            menu.setmInfo(infoTextArea.getText());
            menu.setmPrice(Double.valueOf(priceTextField.getText()));
            menu.setmAvailability(availabilityChoiceBox.getValue());
            menu.setmImage(menuImageView);


            if (menuService.insertMenuData(menu)) {
                AlertPopUp.insertSuccesfully("Menu");
                loadData();
                searchTable();
                clearFields(event);
            } else
                AlertPopUp.insertionFailed("Menu");

        } else
            menuValidationMessage();
    }

    @FXML
    void updateMenu(ActionEvent event) {
        clearLabels();
        if (menuValidation()) {
            Menu menu = new Menu();
            MenuService menuService = new MenuService();

            menu.setmID(selectedMenu.getmID());
            menu.setmName(nameTextField.getText());
            menu.setmInfo(infoTextArea.getText());
            menu.setmPrice(Double.valueOf(priceTextField.getText()));
            menu.setmAvailability(availabilityChoiceBox.getValue());
            menu.setmImage(menuImageView);

            if (menuService.updateMenuData(menu)) {
                AlertPopUp.updateSuccesfully("Menu");
                loadData();
                searchTable();
                clearFields(event);
            } else
                AlertPopUp.updateFailed("Menu");
        } else
            menuValidationMessage();
    }

    @FXML
    private void deleteMenu(ActionEvent event) {
        if (selectedMenu != null) {
            MenuService menuService = new MenuService();
            Optional<ButtonType> action = AlertPopUp.deleteConfirmation("Menu");
            if (action.get() == ButtonType.OK) {
                if (menuService.deleteMenuData((selectedMenu.getmID()))) {
                    AlertPopUp.deleteSuccesfull("Menu");
                    loadData();
                    searchTable();
                    clearFields(event);
                } else
                    AlertPopUp.deleteFailed("Menu");
            } else
                loadData();
        } else {
            AlertPopUp.selectRowToDelete("Menu");
        }
    }

    @FXML
    void setSelectedMenuDataToFields(MouseEvent event) {
        try {
            clearLabels();
            addButton.setVisible(false);
            updateButton.setVisible(true);

            selectedMenu = menuTable.getSelectionModel().getSelectedItem();
            nameTextField.setText(selectedMenu.getmName());
            infoTextArea.setText(selectedMenu.getmInfo());
            priceTextField.setText(selectedMenu.getmPrice().toString());
            availabilityChoiceBox.setValue(selectedMenu.getmAvailability());
            menuImageView.setImage(selectedMenu.getmImage().getImage());
        } catch (NullPointerException exception) {

        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        nameTextField.setText("");
        infoTextArea.setText("");
        priceTextField.setText("");
        menuImageView.setImage(null);
        resetComponents();
        clearLabels();
        selectedMenu = null;
    }

    private void clearLabels() {
        nameLabel.setText("");
        infoLabel.setText("");
        priceLabel.setText("");
        menuImageLabel.setText("");
    }

    private boolean menuValidation() {


        return DataValidation.TextFieldNotEmpty(nameTextField.getText())
                && DataValidation.TextFieldNotEmpty(infoTextArea.getText())
                && DataValidation.TextFieldNotEmpty(priceTextField.getText())
                && DataValidation.ImageFieldNotEmpty(menuImageView)

                && DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(infoTextArea.getText(), 400)

                && DataValidation.isValidNumberFormat(priceTextField.getText());


    }

    private void menuValidationMessage() {

        if (!(DataValidation.TextFieldNotEmpty(nameTextField.getText())
                && DataValidation.TextFieldNotEmpty(infoTextArea.getText())
                && DataValidation.TextFieldNotEmpty(priceTextField.getText())
                && DataValidation.ImageFieldNotEmpty(menuImageView))) {
            DataValidation.TextFieldNotEmpty(nameTextField.getText(), nameLabel, "Menu Name Required!");
            DataValidation.TextFieldNotEmpty(infoTextArea.getText(), infoLabel, "Menu Information Required!");
            DataValidation.TextFieldNotEmpty(priceTextField.getText(), priceLabel, "Menu Price Required!");
            DataValidation.ImageFieldNotEmpty(menuImageView, menuImageLabel, "Select a Image!");

        }
        if (!(DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(infoTextArea.getText(), 400))) {

            DataValidation.isValidMaximumLength(nameTextField.getText(), 45, nameLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(infoTextArea.getText(), 400, infoLabel, "Field Limit 400 Exceeded!");
        }
        DataValidation.isValidNumberFormat(priceTextField.getText(), priceLabel, "Invalid Price Amount!");
    }
    private void resetComponents() {
        addButton.setVisible(true);
        updateButton.setVisible(false);
    }

    @FXML
    private void chooseImage(ActionEvent event){
        menuImageLabel.setText("");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.jpg"));
        File file = fileChooser.showOpenDialog(null);
        if(file != null){
            staticFile = file;
            Image image = new Image(file.toURI().toString());
            menuImageView.setImage(image);
        }
    }
}
