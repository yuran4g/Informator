import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Андрей on 17.07.2016.
 */
public final class RegsWorker {
    private final static Logger logger = Logger.getLogger(PCDataGrabber.class);
    public static Reg[] loadRegs(String path){
        Reg[] ret=null;
        com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
        try{
            ret = mapper.readValue(new FileInputStream(path),Reg[].class);
        }
        catch (Exception e){
            logger.error("Can not load registry from file = " + path);
        }
        return ret;
    }
    public static void saveRegs(String path,Reg[] regs){
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("regs.json"), regs);
        }
        catch (Exception e){
            logger.error("Can not save registry to file = " + path);
        }
    }
}