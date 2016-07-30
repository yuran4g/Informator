package osData;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by yksenofontov on 08.07.2016.
 */
public final class Properties {

    private static Properties instance = null;

    private Properties() {
    }

    public static Properties getInstance() {
        if (instance == null)
            instance = new Properties();
        return instance;
    }

    private ArrayList<String> userProperties;

    public ArrayList<String> getUserProperties() {
        return userProperties;
    }

    public void setUserProperties(ArrayList<String> userProperties) {
        this.userProperties = userProperties;
    }
}
