import Util.Settings;
import fileHelper.EntityList;
import osData.Properties;
import osData.RegsWorker;
import ui.*;

/**
 * Created by yksenofontov on 08.07.2016.
 */
public class Launcher{
    public static void main(String[] args) throws Exception{
//        initialization
//        add/update/delete examples
        try {
            EntityList.loadEntityList();
        } catch (Exception e1) {
// implement
        }

//        EntityList.addEntity("log","application.log");
        /*Entity entity = EntityList.getEntities().get(0);
        String path = entity.archive();*/
//        that path should be opened at windows explorer


//        Entity entity2 = entityList.getEntities().get(1);
//        entity2.clean();//        entityList.removeEntity(testEntity2);
        /*FileZip.zipEntity("resources");
        Zipper.Zip("resources","test");
        ZipPack zp = new ZipPack();
        zp.setPackDirectoryPath("src");
        zp.packDirectory();
        zp.setPackFilePath("registers.json");
        zp.packFile();*/
        RegsWorker.loadRegs();
        Settings.LoadSettings();
        Properties.getInstance().setUserProperties(RegsWorker.getNames());
        new NewArchiver();
    }
}