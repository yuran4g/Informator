package osData;

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
        String result = "";
        for (String param : params) {
            if (grabbedData.get(param)==null) continue;
            result = result + grabbedData.get(param);
        }
        result=result.replace("\n","\r\n");
        return result;
    }

    public void grabData(ArrayList<String> params) {
        HashMap<String,String> result = new HashMap<String,String>();
        String data;
        for (String param : params) {
            if (param.equals("Java")){
                data = System.getProperty("java.version");
            }
            else if (param.equals("User")) {
                data = System.getProperty("user.name");
            }
            else if (param.equals(".NET")){
                data = getNETVersion();
            }
            else{
                data = getRegistryValue(param);
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
                String s=null;
                ArrayList<String> ret = executeScript(builder);
                int n=0;
                for (;n<ret.size();n++){
                    if (ret.get(n).contains("REG_")) break;
                }
                if (ret.get(n).contains("\t"))
                    s = ret.get(n).split("\t")[2];
                else if (ret.get(n).contains("    "))
                    s = ret.get(n).split("[\\s]{4}")[3];
                if (s.contains(r.getValue()))results+="- Microsoft .NET Framework "+r.getVersion()+" ";
                else continue;
                builder = new ProcessBuilder("reg", "query", r.getPathSP(), "/v", r.getKeySP());
                s = executeScript(builder).get(2).trim().split(" ")[8];
                if (!s.equals("0x0"))results+="SP"+s.replaceAll("^0x","")+"\n";
                else results+="\n";
            } catch (Exception e) {
                logger.trace("Can not find registry = "+r.getPath());
            }
            finally {
                if (results.charAt(results.length()-1)!='\n') results+='\n';
            }
        }
        return results.length()>1?results:"";
    }

    private String getRegistryValue(String name) {
        Reg[] rs = RegsWorker.getRegs(name);
        String ret = findValues(rs,RegsWorker.getAllFlag(name),RegsWorker.getSeparator(name));
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

    private String findValues(Reg[] regs, boolean all,String separator){
        String final_value = "";
        for(Reg r:regs) {
            try {
                ProcessBuilder builder = new ProcessBuilder("reg", "query", r.getPath(), "/v", r.getKey());
                ArrayList<String> ret = executeScript(builder);
                int n=0;
                for (;n<ret.size();n++){
                    if (ret.get(n).contains("REG_SZ")) break;
                }
                String[] a=null;
                if (ret.get(n).contains("\t"))
                    a = ret.get(n).split("\t");
                else if (ret.get(n).contains("    "))
                    a=ret.get(n).split("[\\s]{4}");
                List<String> list = new ArrayList<String>(Arrays.asList(a));
                list.removeAll(Arrays.asList("", null));
                for (int i = 2; i < list.size(); ) {
                    final_value = final_value + list.get(i);
                    i = i + 1;
                }
                if (!all) break;
                else final_value+=separator;
            } catch (Exception e) {
                logger.info("Can not find " + r.getPath() + " " + r.getKey()+"\n");
            }
        }
        return final_value;
    }
}