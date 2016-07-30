import Util.Cleaner;
import Util.FileZip;
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
//        initialization
//        add/update/delete examples
        /*EntityList.addEntity("name1","c:\\2\\123 321\\Новый текстовый документ.txt");
        EntityList.addEntity("name2","c:\\2\\123 321");
        Entity entity = EntityList.getEntities().get(0);
        String path = entity.archive();*/
//        that path should be opened at windows explorer


//        Entity entity2 = entityList.getEntities().get(1);
//        entity2.clean();
//        entityList.removeEntity(testEntity2);

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