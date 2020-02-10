package com.nextsol.digitalcompass.model;

public class Forecast {

    String timeStamp;
    double temp;
    String iconCode;
    String status;
    double speedWind;
    double dirWind;
    double humidity;
    double pressure;

    public Forecast(String timeStamp, float temp, String iconCode, String status, float speedWind, float dirWind, float humidity, float pressure) {
        this.timeStamp = timeStamp;
        this.temp = temp;
        this.iconCode = iconCode;
        this.status = status;
        this.speedWind = speedWind;
        this.dirWind = dirWind;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    public Forecast() {
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getIconCode() {
        return iconCode;
    }

    public void setIconCode(String iconCode) {
        this.iconCode = iconCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getSpeedWind() {
        return speedWind;
    }

    public void setSpeedWind(double speedWind) {
        this.speedWind = speedWind;
    }

    public double getDirWind() {
        return dirWind;
    }

    public void setDirWind(double dirWind) {
        this.dirWind = dirWind;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
}
