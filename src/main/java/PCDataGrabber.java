import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.sun.corba.se.spi.activation.IIOP_CLEAR_TEXT.value;

/**
 * Created by yksenofontov on 08.07.2016.
 */
public class PCDataGrabber {

    private static PCDataGrabber instance = null;

    private PCDataGrabber() {
    }

    public static PCDataGrabber getInstance() {
        if (instance == null)
            instance = new PCDataGrabber();
        return instance;
    }

    private static ArrayList<String> grabbedData;

    public static ArrayList<String> getGrabbedData() {
        return grabbedData;
    }

    private final static String JAVA_SCRIPT = "java -version";

    private void setGrabbedData(String grabbedData) {
        PCDataGrabber.grabbedData.add(grabbedData);
    }

    public String getPCdata(ArrayList<String> params) {
        String result = "PC Configuration:\n";
        for (String param : params) {
            if (param.equals("OS")) result = result + "OS version: " + getOSVersion() + "\n";
            if (param.equals("Java")) result = result + "Java version: " + getJavaVersion().get(0) + "\n";
        }
        return result;
    }

    private String getOSVersion() {
        return System.getProperty("os.name");
    }

    private ArrayList<String> getJavaVersion() {
        return executeScript(JAVA_SCRIPT);
    }

    private ArrayList<String> executeScript(String commands) {
        ArrayList<String> result = new ArrayList<String>();

        String line;
        try {
            Process p = Runtime.getRuntime().exec(commands);
            BufferedReader input = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                result.add(line);
            }
            input.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }
}
