import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yksenofontov on 08.07.2016.
 */
public class PCDataGrabber {
    private final static Logger logger = Logger.getLogger(PCDataGrabber.class);
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
            if (param.equals("User")){
                String data = getUserName();
                if (!data.equals("")) result.put("User","User: " + data + "\n");
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
        String results = "\n";
        Reg[] regs = RegsWorker.loadRegs("regs.json");
        for (Reg r:regs){
            try {
                ProcessBuilder builder = new ProcessBuilder("reg", "query", r.getPath(), "/v", r.getKey());
                String s = executeScript(builder).get(2).trim().split(" ")[8];
                if (s.contains(r.getValue()))results+="- Microsoft .NET Framework "+r.getVersion()+" ";
                else continue;
                builder = new ProcessBuilder("reg", "query", r.getPathSP(), "/v", r.getKeySP());
                s = executeScript(builder).get(2).trim().split(" ")[8];
                if (!s.equals("0x0"))results+="SP"+s.replaceAll("^0x","")+"\n";
                else results+="\n";
            } catch (Exception e) {
                logger.trace("Can not find registry = "+r.getPath());
            }
        }
        return results.length()>1?results:"";
    }

    private String getIEVersion() {
        try {
            ProcessBuilder builder = new ProcessBuilder("reg", "query", "HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Internet Explorer\\", "/v", "svcVersion");
            String[] a = executeScript(builder).get(2).split(" ");
            return a[a.length - 1];
        } catch (Exception e) {
            logger.info("Can not find IE version");
            return "";
        }

    }

    private String getFirefoxVersion() {
//        need to check at different OS versions
        String[] regs = {"HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\Mozilla\\Mozilla Firefox",
                "HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\mozilla.org\\Mozilla"};
        for (String reg : regs) {
            try {
                ProcessBuilder builder = new ProcessBuilder("reg", "query", reg, "/v", "CurrentVersion");
                String[] a = executeScript(builder).get(2).split(" ");
                for (int i = 0; i < 10; i++) {
                    a = ArrayUtils.removeElement(a, "");
                }
                return a[2];
            } catch (Exception e) {
                logger.info("Can not find Firefox version");
            }
        }
        return "";
    }

    private String getChromeVersion() {
//        need to check at different OS versions
        try {
            ProcessBuilder builder = new ProcessBuilder("reg", "query", "HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\Google Chrome", "/v", "Version");
            String[] a = executeScript(builder).get(2).split(" ");
            return a[a.length - 1];
        } catch (Exception e) {
            logger.info("Can not find Chrome version");
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
            logger.info("Can not execute script");
            ex.printStackTrace();
        }
        return result;
    }

    private String getUserName(){
        try{return System.getProperty("user.name");}
        catch (Exception e){
            logger.info("Can not get user name");
            return "";}
    }
}