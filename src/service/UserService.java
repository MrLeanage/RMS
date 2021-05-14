package service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import bean.User;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
import utility.dataHandler.DataEncryption;
import utility.dbConnection.DBConnection;
import utility.popUp.AlertPopUp;
import utility.query.UserQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
    private Connection connection;
    public UserService(){
        connection = DBConnection.getDBConnection();
    }
    public ObservableList<User> loadAllUserData() {
        ObservableList<User> userObservableList = FXCollections.observableArrayList();

        try {
            ResultSet resultSet = connection.createStatement().executeQuery(UserQuery.LOAD_ALL_USER_DATA);
            while (resultSet.next()) {
                userObservableList.add(new User(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4)));
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return userObservableList;
    }

    public User loadSpecificUser(String email) {
        User user = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UserQuery.LOAD_SPECIFIC_USER_DATA);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = new User(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return user;
    }

    public boolean insertUserData(User user) {
        PreparedStatement psUser = null;
        try {

            psUser = connection.prepareStatement(UserQuery.INSERT_USER_DATA);

            psUser.setString(1, user.getuEmail());
            psUser.setString(2, user.getuFName());
            psUser.setString(3, user.getuLName());
            psUser.setString(4, DataEncryption.passwordEncryption(user.getuPassword()));
            psUser.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public boolean updateUserData(User user) {
        PreparedStatement psUser = null;
        try {

            psUser = connection.prepareStatement(UserQuery.UPDATE_USER_DATA);

            psUser.setString(1, user.getuFName());
            psUser.setString(2, user.getuLName());
            psUser.setString(3, DataEncryption.passwordEncryption(user.getuPassword()));
            psUser.setString(4, user.getuEmail());
            psUser.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public boolean deleteUserData(String email) {
        PreparedStatement psUser = null;
        try {

            psUser = connection.prepareStatement(UserQuery.DELETE_USER_DATA);

            psUser.setString(1, email);
            psUser.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public SortedList<User> searchTable(TextField searchTextField) {
        //Retreiving all data from database
        ObservableList<User> userData = loadAllUserData();
        //Wrap the ObservableList in a filtered List (initially display all data)
        FilteredList<User> filteredData = new FilteredList<>(userData, b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                //if filter text is empty display all data
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                //comparing search text with table columns one by one
                String lowerCaseFilter = newValue.toLowerCase();

                 if (user.getuFName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                } else if (user.getuLName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                } else {
                    //have no matchings
                    return false;
                }
            });
        });
        //wrapping the FilteredList in a SortedList
        SortedList<User> sortedData = new SortedList<>(filteredData);
        return sortedData;
    }

}
