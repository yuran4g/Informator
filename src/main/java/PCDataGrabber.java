import org.apache.commons.lang3.ArrayUtils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

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

    public static boolean Contains(String param){
        return grabbedData.containsKey(param);
    }

    private static HashMap<String,String> grabbedData;

    public static String getGrabbedData(ArrayList<String> params) {
        String result = "PC Configuration:\n";
        for (String param : params) {
            result = result + grabbedData.get(param);
        }
        return result;
    }

    public void grabData(ArrayList<String> params) {
        HashMap<String,String> result = new HashMap<String,String>();
        for (String param : params) {
            if (param.equals("OS")) {
                String data = getOSVersion();
                if (!data.equals("")) result.put("OS","OS version: " + data + "\n");
            }
            if (param.equals("Java")){
                String data = getJavaVersion();
                if (!data.equals("")) result.put("Java","Java version: " + data + "\n");
            }
            if (param.equals("IE")){
                String data = getIEVersion();
                if (!data.equals("")) result.put("IE","IE version: " + data + "\n");
            }
            if (param.equals("Chrome")){
                String data = getChromeVersion();
                if (!data.equals("")) result.put("Chrome","Chrome version: " + data + "\n");
            }
            if (param.equals("Firefox")){
                String data = getFirefoxVersion();
                if (!data.equals("")) result.put("Firefox","Firefox version: " + data + "\n");
            }
            if (param.equals("NET")){
                String data = getNETVersion();
                if (!data.equals("")) result.put("NET","NET: " + data);
            }
        }
        grabbedData = result;
    }

    private String getOSVersion() {
        return System.getProperty("os.name");
    }

    private String getJavaVersion() {
        return System.getProperty("java.version");
    }

    private String getNETVersion() {
        try {
            ProcessBuilder builder = new ProcessBuilder("wmic", "product", "get", "description");
            ArrayList<String> script_output = executeScript(builder);
            String results = "\n";
            for (String result : script_output) {
                if (result.contains(".NET Framework")) results = results + "- " + result.trim() + "\n";
            }
            return results;
        } catch (Exception e) {
            return "";
        }
    }

    private String getIEVersion() {
        try {
            ProcessBuilder builder = new ProcessBuilder("reg", "query", "HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Internet Explorer\\", "/v", "svcVersion");
            String[] a = executeScript(builder).get(2).split(" ");
            return a[a.length - 1];
        } catch (Exception e) {
            return "";
        }

    }

    private String getFirefoxVersion() {
//        need to check at different OS versions
        try {
            ProcessBuilder builder = new ProcessBuilder("reg", "query", "HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\Mozilla\\Mozilla Firefox", "/v", "CurrentVersion");
            String[] a = executeScript(builder).get(2).split(" ");
            String result = "";
            for (int i = 0; i < 10; i++) {
                a = ArrayUtils.removeElement(a, "");
            }
            return a[2];
        } catch (Exception e) {
            return "";
        }
    }

    private String getChromeVersion() {
//        need to check at different OS versions
        try {
            ProcessBuilder builder = new ProcessBuilder("reg", "query", "HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\Google Chrome", "/v", "Version");
            String[] a = executeScript(builder).get(2).split(" ");
            return a[a.length - 1];
        } catch (Exception e) {
            return "";
        }
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
