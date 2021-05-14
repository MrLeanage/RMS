package utility.query;

public class MenuQuery {
    public static final String LOAD_ALL_MENU_DATA = "SELECT * FROM menu";
    public static final String LOAD_SPECIFIC_MENU_DATA = "SELECT * FROM menu WHERE mID = ?";
    public static final String INSERT_MENU_DATA = "INSERT INTO menu (mImage, mName, mInfo, mPrice, mAvailability) VALUES (?, ?, ?, ?, ?)";
    public static final String UPDATE_MENU_DATA = "UPDATE menu SET mImage = ?, mName = ?, mInfo = ?, mPrice = ?, mAvailability = ? WHERE mID = ?";
    public static final String DELETE_MENU_DATA = "DELETE FROM menu WHERE mID = ?";

}
