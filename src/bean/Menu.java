package bean;

import javafx.beans.property.*;
import javafx.scene.image.ImageView;
import utility.dataHandler.UtilityMethod;

public class Menu {
    private String mID = null;
    private ImageView mImage = null;
    private String mName = null;
    private String mInfo = null;
    private Double mPrice = null;
    private String mAvailability = null;

    public Menu() {
    }

    public Menu(String mID, ImageView mImage, String mName, String mInfo, Double mPrice, String mAvailability) {
        this.mID = UtilityMethod.addPrefix("M", mID);
        this.mImage = mImage;
        this.mName = mName;
        this.mInfo = mInfo;
        this.mPrice = mPrice;
        this.mAvailability = mAvailability;
    }

    public String getmID() {
        return mID;
    }

    public StringProperty mIDProperty(){
        return new SimpleStringProperty(mID);
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public ImageView getmImage() {
        return mImage;
    }

    public ObjectProperty<ImageView> mImageProperty() {
        return new SimpleObjectProperty<>(mImage);
    }

    public void setmImage(ImageView mImage) {
        this.mImage = mImage;
    }

    public String getmName() {
        return mName;
    }

    public StringProperty mNameProperty(){
        return new SimpleStringProperty(mName);
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmInfo() {
        return mInfo;
    }

    public StringProperty mInfoProperty(){
        return new SimpleStringProperty(mInfo);
    }

    public void setmInfo(String mInfo) {
        this.mInfo = mInfo;
    }

    public Double getmPrice() {
        return mPrice;
    }

    public DoubleProperty mPriceProperty(){
        return new SimpleDoubleProperty(mPrice);
    }

    public void setmPrice(Double mPrice) {
        this.mPrice = mPrice;
    }

    public String getmAvailability() {
        return mAvailability;
    }

    public StringProperty mAvailabilityProperty(){
        return new SimpleStringProperty(mAvailability);
    }

    public void setmAvailability(String mAvailability) {
        this.mAvailability = mAvailability;
    }
}
