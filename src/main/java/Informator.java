import java.util.ArrayList;

/**
 * Created by yksenofontov on 08.07.2016.
 */
public class Informator {

    public Informator(){
        ArrayList<String> params = new ArrayList<String>();
        params.add("OS");
        String pcData = PCDataGrabber.getInstance().getPCdata(params);
        ClipboardAccess.getInstance().copyToClipboard(pcData);
    }

}
