package osData;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;


public final class RegsWorker {
    private final static Logger logger = Logger.getLogger(PCDataGrabber.class);
    private static RegHolder[] regs=null;

    public static void loadRegs(){
        com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
        try{
            regs = mapper.readValue(new FileInputStream("registers.json"),RegHolder[].class);
        }
        catch (Exception e){
            logger.error("Can not load registry from file registers.json");
        }
    }
    public static Reg[] getRegs(String name){
        for (RegHolder rh : regs) {
            if (rh.Name.equals(name)) return rh.regs;
        }
        return null;
    }
    public static void saveRegs(String path,Reg[] regs){
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(path), regs);
        }
        catch (Exception e){
            logger.error("Can not save registry to file = " + path);
        }
    }
}