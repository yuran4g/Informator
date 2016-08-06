package Util;


import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by Андрей on 06.08.2016.
 */
public final class Settings {
    private Settings(){}
    private static final String SETTINGS_FILE = "settings.txt";
    private static Logger logger = Logger.getLogger(Settings.class);
    private static HashMap<String,String> settings = new HashMap<String, String>();

    private static void defaultSettings(){
        setDeleteTempFolderOnClose(false);
        setSaveEntityOnClose(true);
        setSaveCompressionLevel(true);
    }

    public static void SaveSettings(){
        try {
            PrintWriter writer = new PrintWriter(SETTINGS_FILE);
            writer.write("DeleteTempFolderOnClose="+getDeleteTempFolderOnClose()+"\r\n");
            writer.write("SaveCompressionLevel="+getSaveCompressionLevel()+"\r\n");
            writer.write("SaveEntityOnClose="+getSaveEntityOnClose()+"\r\n");
            writer.write("CompressionLevel="+ZipPack.getCompressionLevel());
            writer.close();
        }
        catch (Exception e){
            logger.error("Can not save settings :",e);
        }
    }
    public static void LoadSettings(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(SETTINGS_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                settings.put(line.split("=")[0],line.split("=")[1]);
            }
            reader.close();
            if (getSaveCompressionLevel()){
                ZipPack.setCompressionLevel(Integer.parseInt(settings.get("CompressionLevel")));
            }
        }
        catch (Exception e){
            logger.error("Can not load settings :",e);
            defaultSettings();
        }
    }
    public static void setSaveEntityOnClose(boolean flag){
        settings.put("SaveEntityOnClose",flag?"true":"false");
    }
    public static boolean getSaveEntityOnClose(){
        return settings.get("SaveEntityOnClose").equals("true");
    }
    public static void setDeleteTempFolderOnClose(boolean flag){
        settings.put("DeleteTempFolderOnClose",flag?"true":"false");
    }
    public static boolean getDeleteTempFolderOnClose(){
        return settings.get("DeleteTempFolderOnClose").equals("true");
    }
    public static void setSaveCompressionLevel(boolean flag){
        settings.put("SaveCompressionLevel",flag?"true":"false");
    }
    public static boolean getSaveCompressionLevel(){
        return settings.get("SaveCompressionLevel").equals("true");
    }
}
