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
        instance.loadUserProperties();
        return instance;
    }

    private final static String PROPERTIES_FILE = "properties.txt";

    public final static ArrayList<String> allProperties = new ArrayList<String>(Arrays.asList("OS", "User","IE", "Chrome", "Firefox", "Java", "NET"));

    private ArrayList<String> userProperties;

    public ArrayList<String> getUserProperties() {
        return userProperties;
    }

    public void setUserProperties(ArrayList<String> userProperties) {
        this.userProperties = userProperties;
    }

    private void loadUserProperties() {
        File file = new File(PROPERTIES_FILE);
        if (file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                System.out.println("Property file does not exist. Use default values.");
                userProperties = allProperties;
            }
        }
        else{
           setUserProperties(allProperties);
        }
    }

    public void saveUserProperties() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(PROPERTIES_FILE));
            for (String prop: allProperties) {
                writer.write(prop + " \n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Can not save properties to file " + PROPERTIES_FILE);
        }
    }

}
