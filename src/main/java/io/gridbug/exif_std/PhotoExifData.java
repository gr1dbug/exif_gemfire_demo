package io.gridbug.exif_std;

import java.io.Serializable;

public class PhotoExifData implements Serializable {
    private String id;
    private String make;
    private String model;
    private Double xResolution;
    private Double yResolution;
    private String dateTime;
    private Double gpsLatitude;
    private String gpsLatitudeRef;
    private Double gpsLongitude;
    private String gpsLongitudeRef;
    private Double gpsAltitude;
    private String gpsAltitudeRef;
    private String geoLocation;

    public PhotoExifData() {
    }

    public PhotoExifData(String id, String make, String model, Double xResolution, Double yResolution, String dateTime,
            Double gpsLatitude, String gpsLatitudeRef, Double gpsLongitude, String gpsLongitudeRef,
            Double gpsAltitude, String gpsAltitudeRef, String geoLocation) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.xResolution = xResolution;
        this.yResolution = yResolution;
        this.dateTime = dateTime;
        this.gpsLatitude = gpsLatitude;
        this.gpsLatitudeRef = gpsLatitudeRef;
        this.gpsLongitude = gpsLongitude;
        this.gpsLongitudeRef = gpsLongitudeRef;
        this.gpsAltitude = gpsAltitude;
        this.gpsAltitudeRef = gpsAltitudeRef;
        this.geoLocation = geoLocation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Double getxResolution() {
        return xResolution;
    }

    public void setxResolution(Double xResolution) {
        this.xResolution = xResolution;
    }

    public Double getyResolution() {
        return yResolution;
    }

    public void setyResolution(Double yResolution) {
        this.yResolution = yResolution;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Double getGpsLatitude() {
        return gpsLatitude;
    }

    public void setGpsLatitude(Double gpsLatitude) {
        this.gpsLatitude = gpsLatitude;
    }

    public String getGpsLatitudeRef() {
        return gpsLatitudeRef;
    }

    public void setGpsLatitudeRef(String gpsLatitudeRef) {
        this.gpsLatitudeRef = gpsLatitudeRef;
    }

    public Double getGpsLongitude() {
        return gpsLongitude;
    }

    public void setGpsLongitude(Double gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
    }

    public String getGpsLongitudeRef() {
        return gpsLongitudeRef;
    }

    public void setGpsLongitudeRef(String gpsLongitudeRef) {
        this.gpsLongitudeRef = gpsLongitudeRef;
    }

    public Double getGpsAltitude() {
        return gpsAltitude;
    }

    public void setGpsAltitude(Double gpsAltitude) {
        this.gpsAltitude = gpsAltitude;
    }

    public String getGpsAltitudeRef() {
        return gpsAltitudeRef;
    }

    public void setGpsAltitudeRef(String gpsAltitudeRef) {
        this.gpsAltitudeRef = gpsAltitudeRef;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }

    @Override
    public String toString() {
        return "PhotoExifData{" +
                "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", xResolution=" + xResolution +
                ", yResolution=" + yResolution +
                ", dateTime='" + dateTime + '\'' +
                ", gpsLatitude=" + gpsLatitude +
                ", gpsLatitudeRef='" + gpsLatitudeRef + '\'' +
                ", gpsLongitude=" + gpsLongitude +
                ", gpsLongitudeRef='" + gpsLongitudeRef + '\'' +
                ", gpsAltitude=" + gpsAltitude +
                ", gpsAltitudeRef='" + gpsAltitudeRef + '\'' +
                ", geoLocation='" + geoLocation + '\'' +
                '}';
    }
}
