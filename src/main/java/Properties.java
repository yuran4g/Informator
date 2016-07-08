import java.io.*;
import java.util.ArrayList;

/**
 * Created by yksenofontov on 08.07.2016.
 */
public final class Properties {
    private static Properties instance = null;
    private final static String PROPERTIES_FILE = "properties.txt";

    private Properties() {
    }

    public static Properties getInstance() {
        if (instance == null)
            instance = new Properties();
        instance.loadUserProperties();
        return instance;
    }


    public final static String[] allProperties = {"OS", "IE", "Chrome", "Firefox", "Java", "NET"};
    private String[] userProperties;

    public String[] getUserProperties() {
        return userProperties;
    }

    public void setUserProperties(String[] userProperties) {
        this.userProperties = userProperties;
    }

    public void loadUserProperties() {
        File file = new File(PROPERTIES_FILE);
        if (file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                System.out.println("Property file does not exist. Use default values.");
                userProperties = allProperties;
            }
        }
    }

    public void saveUserProperties() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(PROPERTIES_FILE));
            for (int i = 0; i < allProperties.length; i++) {
                writer.write(allProperties[i] + " \n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Can not save properties to file " + PROPERTIES_FILE);
        }
    }

}
