package org.codeforiraq.safety;

/**
 * Created by Ameer on 8/23/2018.
 */

public class FirebaseMarker {
    private String title;
    private String numberPhone;
    private double latitude;
    private double longitude;

    public FirebaseMarker() {

    }

    public FirebaseMarker(String title, String numberPhone ,double latitude, double longitude) {
        this.title = title;
        this.numberPhone = numberPhone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
