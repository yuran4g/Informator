import Util.Settings;
import fileHelper.EntityList;
import org.apache.log4j.Logger;
import osData.Properties;
import osData.RegsWorker;
import ui.*;

/**
 * Created by yksenofontov on 08.07.2016.
 */
public class Launcher{
    private static Logger log = Logger.getLogger(Launcher.class);
    public static void main(String[] args) throws Exception{
        try {
            EntityList.loadEntityList();
        } catch (Exception e1) {
            log.error("Can't load entities: ",e1);
        }
        RegsWorker.loadRegs();
        Settings.LoadSettings();
        Properties.getInstance().setUserProperties(RegsWorker.getNames());
        new NewArchiver();
    }
}