package model;

import java.io.Serializable;

/**
 * Created by Josermando Peralta on 4/7/2016.
 */
public class PerformanceDetails implements Serializable {

    private String mpgHighway;
    private String mpgCity;
    private String configuration;
    private String fuelType;

    public PerformanceDetails() {
    }

    public String getMpgHighway() {
        return mpgHighway;
    }

    public void setMpgHighway(String mpgHighway) {
        this.mpgHighway = mpgHighway;
    }

    public String getMpgCity() {
        return mpgCity;
    }

    public void setMpgCity(String mpgCity) {
        this.mpgCity = mpgCity;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
}
