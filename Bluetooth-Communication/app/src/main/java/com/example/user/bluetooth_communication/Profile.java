package com.example.user.bluetooth_communication;

import java.io.Serializable;

/**
 * Created by Duarte on 14/05/2017.
 */

public class Profile implements Serializable {

    private String key;
    private String value;

    public Profile(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
