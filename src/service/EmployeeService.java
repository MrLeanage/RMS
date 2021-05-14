package service;

import bean.Employee;
import bean.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
import utility.dataHandler.DataEncryption;
import utility.dataHandler.UtilityMethod;
import utility.dbConnection.DBConnection;
import utility.popUp.AlertPopUp;
import utility.query.EmployeeQuery;
import utility.query.UserQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeService {
    private Connection connection;
    public EmployeeService(){
        connection = DBConnection.getDBConnection();
    }
    public ObservableList<Employee> loadAllEmployeeData() {
        ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList();

        try {
            ResultSet resultSet = connection.createStatement().executeQuery(EmployeeQuery.LOAD_ALL_EMPLOYEE_DATA);
            while (resultSet.next()) {
                employeeObservableList.add(new Employee(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4), resultSet.getString(5)));
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return employeeObservableList;
    }

    public Employee loadSpecificEmployee(String eID) {
        Employee employee = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(EmployeeQuery.LOAD_SPECIFIC_EMPLOYEE_DATA);
            preparedStatement.setInt(1, UtilityMethod.seperateID(eID));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                employee =new Employee(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4), resultSet.getString(5));
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return employee;
    }

    public boolean insertEmployeeData(Employee employee) {
        PreparedStatement psEmployee = null;
        try {

            psEmployee = connection.prepareStatement(EmployeeQuery.INSERT_EMPLOYEE_DATA);

            psEmployee.setString(1, employee.geteName());
            psEmployee.setString(2, employee.geteNIC());
            psEmployee.setInt(3, employee.geteContactNo());
            psEmployee.setString(4, employee.geteDesignation());
            psEmployee.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public boolean updateEmployeeData(Employee employee) {
        PreparedStatement psEmployee = null;
        try {

            psEmployee = connection.prepareStatement(EmployeeQuery.UPDATE_EMPLOYEE_DATA);

            psEmployee.setString(1, employee.geteName());
            psEmployee.setString(2, employee.geteNIC());
            psEmployee.setInt(3, employee.geteContactNo());
            psEmployee.setString(4, employee.geteDesignation());
            psEmployee.setInt(5, UtilityMethod.seperateID(employee.geteID()));
            psEmployee.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public boolean deleteEmployeeData(String eID) {
        PreparedStatement psEmployee = null;
        try {

            psEmployee = connection.prepareStatement(EmployeeQuery.DELETE_EMPLOYEE_DATA);

            psEmployee.setInt(1, UtilityMethod.seperateID(eID));
            psEmployee.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public SortedList<Employee> searchTable(TextField searchTextField) {
        //Retreiving all data from database
        ObservableList<Employee> employeeData = loadAllEmployeeData();
        //Wrap the ObservableList in a filtered List (initially display all data)
        FilteredList<Employee> filteredData = new FilteredList<>(employeeData, b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(employee -> {
                //if filter text is empty display all data
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                //comparing search text with table columns one by one
                String lowerCaseFilter = newValue.toLowerCase();

                 if (employee.geteName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                } else if (employee.geteNIC().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                }else if (employee.geteContactNo().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     //return if filter matches data
                     return true;
                 }else if (employee.geteDesignation().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     //return if filter matches data
                     return true;
                 } else {
                    //have no matchings
                    return false;
                }
            });
        });
        //wrapping the FilteredList in a SortedList
        SortedList<Employee> sortedData = new SortedList<>(filteredData);
        return sortedData;
    }

}
