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
        reg[] regs = {
                new reg("HKLM\\Software\\Microsoft\\Active Setup\\Installed Components\\{78705f0d-e8db-4b2d-8193-982bdda15ecd}","Version","","1.0","HKLM\\Software\\Microsoft\\Active Setup\\Installed Components\\{78705f0d-e8db-4b2d-8193-982bdda15ecd}","Version"),
                new reg("HKLM\\Software\\Microsoft\\Active Setup\\Installed Components\\{FDC11A6F-17D1-48f9-9EA3-9051954BAA24}","Version","","1.0","HKLM\\Software\\Microsoft\\Active Setup\\Installed Components\\{FDC11A6F-17D1-48f9-9EA3-9051954BAA24}","Version"),
                new reg("HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v1.1.4322","","","1.1","HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v1.1.4322","SP"),
                new reg("HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v2.0.50727","Version","","2.0","HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v2.0.50727","SP"),
                new reg("HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v2.0.50727","Increment","","2.0 Original Release (RTM)","HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v2.0.50727","SP"),
                new reg("HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v3.0","Version","","3.0","HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v3.0","SP"),
                new reg("HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v3.5","Version","","3.5","HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v3.5","SP"),
                new reg("HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v4\\Client","Version","","4.0 Client Profile","HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v4\\Client","Servicing"),
                new reg("HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full","Version","","4.0 Full Profile","HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full","Servicing"),
                new reg("HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full","Release","0x6004f","4.6","HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full","Servicing"),
                new reg("HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full","Release","0x60051","4.6","HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full","Servicing"),
                new reg("HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full","Release","0x6040e","4.6.1","HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full","Servicing"),
                new reg("HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full","Release","0x6041f","4.6.1","HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full","Servicing")};
        for (reg r:regs){
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

    private class reg{
        String path,key,version,value,pathSP,keySP;
        public reg(String Path,String Key,String Value,String Version,String PathSP,String KeySP)
        {
            path=Path;
            key=Key;
            version=Version;
            value=Value;
            pathSP=PathSP;
            keySP=KeySP;
        }

        public String getPath(){return path;}
        public String getKey(){return key;}
        public String getVersion(){return version;}
        public String getValue(){return value;}
        public String getPathSP(){return pathSP;}
        public String getKeySP(){return keySP;}
    }
}