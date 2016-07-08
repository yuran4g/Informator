import java.io.IOException;
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
    private final static String OS_SCRIPT = "ver";
    private void setGrabbedData(String grabbedData) {
        PCDataGrabber.grabbedData.add(grabbedData);
    }

    public String getPCdata(ArrayList<String> params){
        String result="PC Configuration:\n";
        for (String param : params) {
            if (param.equals("OS")) result = result + "OS version: " + getOSVersion() + "\n";

        }
      return result;
    }

    private String getOSVersion(){
        return System.getProperty("os.name");
    }
}
