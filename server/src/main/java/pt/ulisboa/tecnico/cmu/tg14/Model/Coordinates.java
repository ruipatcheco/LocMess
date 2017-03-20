package pt.ulisboa.tecnico.cmu.tg14.Model;

import java.util.UUID;

/**
 * Created by trosado on 20/03/17.
 */
public class Coordinates {
    private UUID id;
    private float latitude;
    private float longitude;
    private int radius;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

}
