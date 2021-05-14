package bean;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utility.dataHandler.UtilityMethod;

public class Employee {
    private String eID = null;
    private String eName = null;
    private String eNIC = null;
    private Integer eContactNo = null;
    private String eDesignation = null;

    public Employee() {
    }

    public Employee(String eID, String eName, String eNIC, Integer eContactNo, String eDesignation) {
        this.eID = UtilityMethod.addPrefix("EM", eID);
        this.eName = eName;
        this.eNIC = eNIC;
        this.eContactNo = eContactNo;
        this.eDesignation = eDesignation;
    }

    public String geteID() {
        return eID;
    }

    public StringProperty eIDProperty(){
        return new SimpleStringProperty(eID);
    }

    public void seteID(String eID) {
        this.eID = eID;
    }

    public String geteName() {
        return eName;
    }

    public StringProperty eNameProperty(){
        return new SimpleStringProperty(eName);
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String geteNIC() {
        return eNIC;
    }

    public StringProperty eNICProperty(){
        return new SimpleStringProperty(eNIC);
    }

    public void seteNIC(String eNIC) {
        this.eNIC = eNIC;
    }

    public Integer geteContactNo() {
        return eContactNo;
    }

    public IntegerProperty eContactNoProperty(){
        return new SimpleIntegerProperty(eContactNo);
    }

    public void seteContactNo(Integer eContactNo) {
        this.eContactNo = eContactNo;
    }

    public String geteDesignation() {
        return eDesignation;
    }

    public StringProperty eDesignationProperty(){
        return new SimpleStringProperty(eDesignation);
    }

    public void seteDesignation(String eDesignation) {
        this.eDesignation = eDesignation;
    }
}
