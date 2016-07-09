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

    private void setGrabbedData(String grabbedData) {
        PCDataGrabber.grabbedData.add(grabbedData);
    }

    public String getPCdata(ArrayList<String> params) {
        String result = "PC Configuration:\n";
        for (String param : params) {
            if (param.equals("OS")) result = result + "OS version: " + getOSVersion() + "\n";
            if (param.equals("Java")) result = result + "Java version: " + getJavaVersion() + "\n";
            if (param.equals("IE")) result = result + "IE version: " + getIEVersion() + "\n";
            if (param.equals("Chrome")) result = result + "Chrome version: " + getChromeVersion() + "\n";
            if (param.equals("Firefox")) result = result + "Firefox version: " + getFirefoxVersion() + "\n";
            if (param.equals("NET")) result = result + "NET: " +getNETVersion();
        }
        return result;
    }

    private String getOSVersion() {
        return System.getProperty("os.name");
    }

    private String getJavaVersion() {
        return System.getProperty("java.version");
    }

    private String getNETVersion() {
        ProcessBuilder builder = new ProcessBuilder("wmic","product","get","description");
        ArrayList<String> script_output = executeScript(builder);
        String results = "\n";
        for (String result: script_output) {
            if (result.contains(".NET Framework")) results= results+"- "+result.trim() + "\n";
        }
        return results;
    }

    private String getIEVersion() {
        ProcessBuilder builder = new ProcessBuilder("reg", "query","HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Internet Explorer\\","/v","svcVersion");
        String[] a = executeScript(builder).get(2).split(" ");
        return a[a.length-1];
    }

    private String getFirefoxVersion() {
        return "";
    }

    private String getChromeVersion() {
        return "";
    }

    private ArrayList<String> executeScript(ProcessBuilder builder) {
        ArrayList<String> result = new ArrayList<String>();

        String line;
        try {
            Process p = builder.start();
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
