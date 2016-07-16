import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Андрей on 17.07.2016.
 */
public final class RegsWorker {
    public static Reg[] LoadRegs(String Path){
        Reg[] ret=null;
        com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
        try{
            ret = mapper.readValue(new FileInputStream(Path),Reg[].class);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }
    public static void SaveRegs(String Path,Reg[] regs){
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("regs.json"), regs);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}


/*Reg[] regs = {
                new Reg("HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v2.0.50727","Version","","2.0","HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v2.0.50727","SP"),
                new Reg("HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v2.0.50727","Increment","","2.0 Original Release (RTM)","HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v2.0.50727","SP"),
                new Reg("HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v3.0","Version","","3.0","HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v3.0","SP"),
                new Reg("HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v3.5","Version","","3.5","HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v3.5","SP"),
                new Reg("HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v4\\Client","Version","","4.0 Client Profile","HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v4\\Client","Servicing"),
                new Reg("HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full","Version","","4.0 Full Profile","HKLM\\Software\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full","Servicing"),
                new Reg("HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full","Release","0x6004f","4.6","HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full","Servicing"),
                new Reg("HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full","Release","0x60051","4.6","HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full","Servicing"),
                new Reg("HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full","Release","0x6040e","4.6.1","HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full","Servicing"),
                new Reg("HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full","Release","0x6041f","4.6.1","HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full","Servicing")};*/