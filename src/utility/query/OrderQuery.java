package utility.query;

public class OrderQuery {
    public static final String LOAD_ALL_ORDER_DATA = "SELECT * FROM orderlist";
    public static final String LOAD_SPECIFIC_ORDER_DATA = "SELECT * FROM orderlist WHERE oID = ?";
    public static final String INSERT_ORDER_DATA = "INSERT INTO orderlist (oCustomerName, oMenuID, oNotes, oQuantity, oStatus) VALUES (?, ?, ?, ?, ?)";
    public static final String UPDATE_ORDER_DATA = "UPDATE orderlist SET oCustomerName = ?, oMenuID = ?, oNotes = ?, oQuantity = ?, oStatus = ? WHERE oID = ?";
    public static final String DELETE_ORDER_DATA = "DELETE FROM orderlist WHERE oID = ?";

}
