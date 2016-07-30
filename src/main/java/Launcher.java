import fileHelper.Entity;
import fileHelper.EntityList;
import osData.Properties;
import osData.RegsWorker;
import ui.*;

/**
 * Created by yksenofontov on 08.07.2016.
 */
public class Launcher{
    public static void main(String[] args) throws Exception{
        RegsWorker.loadRegs();
        Properties.getInstance().setUserProperties(RegsWorker.getNames());
        if (args.length==0) {
            new Informator();
            new Archiver();
        }
        else
            new Informator(args);
    }
}