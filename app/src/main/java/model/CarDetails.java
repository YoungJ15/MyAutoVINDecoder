package model;

import java.io.Serializable;

/**
 * Created by Josermando Peralta on 4/6/2016.
 */
@SuppressWarnings("serial")
public class CarDetails implements Serializable{

    /**Fields **/
    private static final long SERIAL_NO = 1L;

    private String make;
    private String model;
    private String drive;
    private String year;
    private String squishVin;
    private String doors;
    private String carName;
    private String styleId;
    private String vehicleStyle;
    private String vehicleType;

    public CarDetails() {
    }


    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDrive() {
        return drive;
    }

    public void setDrive(String drive) {
        this.drive = drive;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSquishVin() {
        return squishVin;
    }

    public void setSquishVin(String squishVin) {
        this.squishVin = squishVin;
    }

    public String getDoors() {
        return doors;
    }

    public void setDoors(String doors) {
        this.doors = doors;
    }

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getVehicleStyle() {
        return vehicleStyle;
    }

    public void setVehicleStyle(String vehicleStyle) {
        this.vehicleStyle = vehicleStyle;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
