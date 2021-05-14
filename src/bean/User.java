package bean;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    private String uEmail = null;
    private String uFName = null;
    private String uLName = null;
    private String uPassword = null;

    public User() {
    }

    public User(String uEmail, String uFName, String uLName, String uPassword) {
        this.uEmail = uEmail;
        this.uFName = uFName;
        this.uLName = uLName;
        this.uPassword = uPassword;
    }

    public String getuEmail() {
        return uEmail;
    }

    public StringProperty uEmailProperty(){
        return new SimpleStringProperty(uEmail);
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuFName() {
        return uFName;
    }

    public StringProperty uFNameProperty(){
        return new SimpleStringProperty(uFName);
    }

    public void setuFName(String uFName) {
        this.uFName = uFName;
    }

    public String getuLName() {
        return uLName;
    }

    public StringProperty uLNameProperty(){
        return new SimpleStringProperty(uLName);
    }

    public void setuLName(String uLName) {
        this.uLName = uLName;
    }

    public String getuPassword() {
        return uPassword;
    }

    public StringProperty uPasswordProperty(){
        return new SimpleStringProperty(uPassword);
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }
}
