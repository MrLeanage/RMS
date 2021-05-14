package utility.query;

public class EmployeeQuery {
    public static final String LOAD_ALL_EMPLOYEE_DATA = "SELECT * FROM employee";
    public static final String LOAD_SPECIFIC_EMPLOYEE_DATA = "SELECT * FROM employee WHERE eID = ?";
    public static final String INSERT_EMPLOYEE_DATA = "INSERT INTO employee (eName, eNIC, eContactNo, eDesignation) VALUES (?, ?, ?, ?)";
    public static final String UPDATE_EMPLOYEE_DATA = "UPDATE employee SET eName = ?, eNIC = ?, eContactNo = ?, eDesignation = ? WHERE eID = ?";
    public static final String DELETE_EMPLOYEE_DATA = "DELETE FROM employee WHERE eID = ?";
}
