import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class PCDataGrabber {
    private final static Logger logger = Logger.getLogger(PCDataGrabber.class);
    private static PCDataGrabber instance = null;

    private PCDataGrabber() {
    }

    static PCDataGrabber getInstance() {
        if (instance == null)
            instance = new PCDataGrabber();
        return instance;
    }

    static boolean Contains(String param){
        return grabbedData.containsKey(param);
    }

    private static HashMap<String,String> grabbedData;

    static String getGrabbedData(ArrayList<String> params) {
        String result = "";
        for (String param : params) {
            result = result + grabbedData.get(param);
        }
        return result;
    }

    void grabData(ArrayList<String> params) {
        HashMap<String,String> result = new HashMap<String,String>();
        String data = "";
        for (String param : params) {
            if (param.equals("OS")) {
                data = System.getProperty("os.name");
            }
            if (param.equals("Java")){
                data = System.getProperty("java.version");
            }
            if (param.equals("IE")){
                data = getRegistryValue(param);
            }
            if (param.equals("Chrome")){
                data = getRegistryValue(param);
            }
            if (param.equals("Firefox")){
                data = getRegistryValue(param);
            }
            if (param.equals("NET")){
                data = getNETVersion();
            }
            if (param.equals("User")){
                data = System.getProperty("user.name");
            }
            if (!data.equals("")) result.put(param,param+" : " + data + "\n");
        }
        grabbedData = result;
    }

    private String getNETVersion() {
        String results = "\n";
        Reg[] regs = RegsWorker.getRegs(".NET");
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

    private String getRegistryValue(String name) {
        Reg[] rs = RegsWorker.getRegs(name);
        String ret = findValues(rs);
        if (ret.equals(""))logger.info("Can not find "+name+" version");
        return ret;
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

    private String findValues(Reg[] regs){
        for(Reg r:regs) {
            try {
                ProcessBuilder builder = new ProcessBuilder("reg", "query", r.getPath(), "/v", r.getKey());
                String[] a = executeScript(builder).get(2).split(" ");
                List<String> list = new ArrayList<String>(Arrays.asList(a));
                list.removeAll(Arrays.asList("", null));
                String final_value = "";
                for (int i = 2; i < list.size(); ) {
                    final_value = final_value + list.get(i);
                    i = i + 1;
                }
                return final_value;
            } catch (Exception e) {
                logger.trace("Can not find " + r.getPath() + " " + r.getKey());
            }
        }
        return "";
    }

}