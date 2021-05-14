package service;

import bean.Employee;
import bean.Menu;
import bean.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
import utility.dataHandler.UtilityMethod;
import utility.dbConnection.DBConnection;
import utility.popUp.AlertPopUp;
import utility.query.EmployeeQuery;
import utility.query.OrderQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderService {
    private Connection connection;
    public OrderService(){
        connection = DBConnection.getDBConnection();
    }

    public ObservableList<Order> loadAllOrderData() {
        ObservableList<Order> orderObservableList = FXCollections.observableArrayList();

        try {
            ResultSet resultSet = connection.createStatement().executeQuery(OrderQuery.LOAD_ALL_ORDER_DATA);
            MenuService menuService = new MenuService();
            while (resultSet.next()) {
                Menu menu = menuService.loadSpecificMenu(UtilityMethod.addPrefix("M", resultSet.getString(3)));
                orderObservableList.add(new Order(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), menu.getmName(), resultSet.getString(4), resultSet.getInt(5), resultSet.getString(6)));
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            AlertPopUp.sqlQueryError(sqlException);
        }
        return orderObservableList;
    }

    public Order loadSpecificOrder(String oID) {
        Order order = null;

        try {
            MenuService menuService = new MenuService();
            PreparedStatement preparedStatement = connection.prepareStatement(OrderQuery.LOAD_SPECIFIC_ORDER_DATA);
            preparedStatement.setInt(1, UtilityMethod.seperateID(oID));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Menu menu = menuService.loadSpecificMenu(UtilityMethod.addPrefix("M", resultSet.getString(3)));
                order = new Order(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), menu.getmName(), resultSet.getString(4), resultSet.getInt(5), resultSet.getString(6));
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return order;
    }

    public boolean insertOrderData(Order order) {
        PreparedStatement psOrder = null;
        try {

            psOrder = connection.prepareStatement(OrderQuery.INSERT_ORDER_DATA);

            psOrder.setString(1, order.getoCustomerName());
            psOrder.setInt(2, UtilityMethod.seperateID(order.getoMenuID()));
            psOrder.setString(3, order.getoNotes());
            psOrder.setInt(4, order.getoQuantity());
            psOrder.setString(5, order.getoStatus());
            psOrder.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public boolean updateOrderData(Order order) {
        PreparedStatement psOrder = null;
        try {

            psOrder = connection.prepareStatement(OrderQuery.UPDATE_ORDER_DATA);

            psOrder.setString(1, order.getoCustomerName());
            psOrder.setInt(2, UtilityMethod.seperateID(order.getoMenuID()));
            psOrder.setString(3, order.getoNotes());
            psOrder.setInt(4, order.getoQuantity());
            psOrder.setString(5, order.getoStatus());
            psOrder.setInt(6, UtilityMethod.seperateID(order.getoID()));
            psOrder.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public boolean deleteOrderData(String oID) {
        PreparedStatement psOrder = null;
        try {

            psOrder = connection.prepareStatement(OrderQuery.DELETE_ORDER_DATA);

            psOrder.setInt(1, UtilityMethod.seperateID(oID));
            psOrder.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public SortedList<Order> searchTable(TextField searchTextField) {
        //Retreiving all data from database
        ObservableList<Order> orderData = loadAllOrderData();
        //Wrap the ObservableList in a filtered List (initially display all data)
        FilteredList<Order> filteredData = new FilteredList<>(orderData, b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(order -> {
                //if filter text is empty display all data
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                //comparing search text with table columns one by one
                String lowerCaseFilter = newValue.toLowerCase();

                 if (order.getoCustomerName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                } else if (order.getoMenuName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                }else if (order.getoStatus().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     //return if filter matches data
                     return true;
                 }else if (UtilityMethod.addPrefix("OR",order.getoID()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     //return if filter matches data
                     return true;
                 } else {
                    //have no matchings
                    return false;
                }
            });
        });
        //wrapping the FilteredList in a SortedList
        SortedList<Order> sortedData = new SortedList<>(filteredData);
        return sortedData;
    }

}
