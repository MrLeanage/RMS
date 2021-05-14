package view.orderManagement;

import bean.Menu;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.MenuService;
import utility.dataHandler.DataValidation;
import utility.popUp.AlertPopUp;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuPopUpController implements Initializable {
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
    private Label menuImageLabel;

    @FXML
    private TextField priceTextField;

    @FXML
    private ImageView menuImageView;

    @FXML
    private TextArea infoTextArea;

    @FXML
    private TextField availabilityTextField;

    @FXML
    private JFXButton cancelButton;

    private static Menu selectedMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();

    }

    private void loadData() {
        MenuService menuService = new MenuService();
        ObservableList<Menu> menuObservableList = FXCollections.observableArrayList();
        for(Menu menu : menuService.loadAllMenuData()){
            if(menu.getmAvailability().equals("Available"))
                menuObservableList.add(menu);
        }


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

        SortedList<Menu> sortedData = menuService.searchTable(searchTextField) ;

        //binding the SortedList to TableView
        sortedData.comparatorProperty().bind(menuTable.comparatorProperty());
        //adding sorted and filtered data to the table
        ObservableList<Menu> list = FXCollections.observableArrayList();
        for(Menu menu :sortedData){
            if(menu.getmAvailability().equals("Available"))
                list.add(menu);
        }
        menuTable.setItems(list);
    }

    @FXML
    void addSelectedMenu(ActionEvent event) {
        if (selectedMenu != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("orderDetail.fxml"));
            try {
                Parent root = (Parent) loader.load();
                OrderDetailController orderDetailController = loader.getController();

                orderDetailController.setMenu(selectedMenu);
                selectedMenu = null;
                closeStage();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else
            AlertPopUp.selectRow("Menu to Add");
    }

    @FXML
    void setSelectedMenuDataToFields(MouseEvent event) {
        try {

            selectedMenu = menuTable.getSelectionModel().getSelectedItem();
            nameTextField.setText(selectedMenu.getmName());
            infoTextArea.setText(selectedMenu.getmInfo());
            priceTextField.setText(selectedMenu.getmPrice().toString());
            availabilityTextField.setText(selectedMenu.getmAvailability());
            menuImageView.setImage(selectedMenu.getmImage().getImage());
        } catch (NullPointerException exception) {

        }
    }
    private void closeStage(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void closeStage(ActionEvent actionEvent){
        // get a handle to the stage
        closeStage();
    }
}
