package service;


import bean.Menu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
import utility.dataHandler.UtilityMethod;
import utility.dbConnection.DBConnection;
import utility.popUp.AlertPopUp;
import utility.query.MenuQuery;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuService {
    private Connection connection;
    public MenuService(){
        connection = DBConnection.getDBConnection();
    }

    public ObservableList<Menu> loadAllMenuData() {
        ObservableList<Menu> menuObservableList = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(MenuQuery.LOAD_ALL_MENU_DATA);
            while (resultSet.next()) {
                InputStream inputStream = resultSet.getBinaryStream(2);
                menuObservableList.add(new Menu(resultSet.getString(1), UtilityMethod.convertInputStreamToImage(inputStream), resultSet.getString(3), resultSet.getString(4), resultSet.getDouble(5), resultSet.getString(6)));
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return menuObservableList;
    }

    public Menu loadSpecificMenu(String mID) {
        Menu menu = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(MenuQuery.LOAD_SPECIFIC_MENU_DATA);
            preparedStatement.setInt(1, UtilityMethod.seperateID(mID));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                InputStream inputStream = resultSet.getBinaryStream(2);
                menu = new Menu(resultSet.getString(1), UtilityMethod.convertInputStreamToImage(inputStream), resultSet.getString(3), resultSet.getString(4), resultSet.getDouble(5), resultSet.getString(6));
            }
        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return menu;
    }

    public boolean insertMenuData(Menu menu) {
        PreparedStatement psMenu = null;
        try {
            psMenu = connection.prepareStatement(MenuQuery.INSERT_MENU_DATA);

            psMenu.setBinaryStream(1, UtilityMethod.convertImageToInputStream(menu.getmImage()));
            psMenu.setString(2, menu.getmName());
            psMenu.setString(3, menu.getmInfo());
            psMenu.setDouble(4, menu.getmPrice());
            psMenu.setString(5, menu.getmAvailability());
            psMenu.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public boolean updateMenuData(Menu menu) {
        PreparedStatement psMenu = null;
        try {

            psMenu = connection.prepareStatement(MenuQuery.UPDATE_MENU_DATA);

            psMenu.setBinaryStream(1, UtilityMethod.convertImageToInputStream(menu.getmImage()));
            psMenu.setString(2, menu.getmName());
            psMenu.setString(3, menu.getmInfo());
            psMenu.setDouble(4, menu.getmPrice());
            psMenu.setString(5, menu.getmAvailability());
            psMenu.setInt(6, UtilityMethod.seperateID(menu.getmID()));
            psMenu.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public boolean deleteMenuData(String mID) {
        PreparedStatement psMenu = null;
        try {

            psMenu = connection.prepareStatement(MenuQuery.DELETE_MENU_DATA);

            psMenu.setInt(1, UtilityMethod.seperateID(mID));
            psMenu.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public SortedList<Menu> searchTable(TextField searchTextField) {
        //Retreiving all data from database
        ObservableList<Menu> menuData = loadAllMenuData();
        //Wrap the ObservableList in a filtered List (initially display all data)
        FilteredList<Menu> filteredData = new FilteredList<>(menuData, b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(menu -> {
                //if filter text is empty display all data
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                //comparing search text with table columns one by one
                String lowerCaseFilter = newValue.toLowerCase();

                 if (menu.getmName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                } else if (menu.getmPrice().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                }else if (menu.getmAvailability().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     //return if filter matches data
                     return true;
                 } else {
                    //have no matchings
                    return false;
                }
            });
        });
        //wrapping the FilteredList in a SortedList
        SortedList<Menu> sortedData = new SortedList<>(filteredData);
        return sortedData;
    }

}
