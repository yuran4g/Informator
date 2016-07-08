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
        Runtime rt = Runtime.getRuntime();
        Process proc = null;
        try {
            proc = rt.exec(commands);
        } catch (IOException e) {
            System.out.println("Can not execute script = " + commands);
        }
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        System.out.println("Here is the standard output of the command:\n");
        String s;
        ArrayList<String> result = new ArrayList<String>();
        try {
            int i = 0;
            while ((s = stdInput.readLine()) != null) {
                result.add(s);
                i = i + 1;
                System.out.println(s);
            }
        } catch (IOException e) {
            System.out.println("Can not get output of script = " + commands);
        }
        return result;
    }
}
