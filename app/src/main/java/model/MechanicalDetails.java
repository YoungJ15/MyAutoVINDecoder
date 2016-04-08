package model;

import java.io.Serializable;

/**
 * Created by Josermando Peralta on 4/7/2016.
 */
public class MechanicalDetails implements Serializable{

    private String cylinders;
    private String horsePower;
    private String litters;
    private String manufacturerEngineCode;
    private String totalValves;
    private String transmissionType;
    private String numberOfSpeed;
    private String engineCode;

    public MechanicalDetails() {
    }



    public String getCylinders() {
        return cylinders;
    }

    public void setCylinders(String cylinders) {
        this.cylinders = cylinders;
    }

    public String getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(String horsePower) {
        this.horsePower = horsePower;
    }

    public String getLitters() {
        return litters;
    }

    public void setLitters(String litters) {
        this.litters = litters;
    }

    public String getManufacturerEngineCode() {
        return manufacturerEngineCode;
    }

    public void setManufacturerEngineCode(String manufacturerEngineCode) {
        this.manufacturerEngineCode = manufacturerEngineCode;
    }

    public String getTotalValves() {
        return totalValves;
    }

    public void setTotalValves(String totalValves) {
        this.totalValves = totalValves;
    }

    public String getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public String getNumberOfSpeed() {
        return numberOfSpeed;
    }

    public void setNumberOfSpeed(String numberOfSpeed) {
        this.numberOfSpeed = numberOfSpeed;
    }

    public String getEngineCode() {
        return engineCode;
    }

    public void setEngineCode(String engineCode) {
        this.engineCode = engineCode;
    }
}
