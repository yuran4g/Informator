import java.util.ArrayList;

/**
 * Created by yksenofontov on 08.07.2016.
 */
public class Informator {

    public Informator(){
        ArrayList<String> params = new ArrayList<String>();
//        ("OS", "IE", "Chrome", "Firefox", "Java", "NET")
        params.add("OS");
        params.add("IE");
        String pcData = PCDataGrabber.getInstance().getPCdata(params);
        ClipboardAccess.getInstance().copyToClipboard(pcData);
    }

}
