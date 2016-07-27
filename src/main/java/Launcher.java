import fileHelper.Entity;
import fileHelper.EntityList;
import osData.RegsWorker;

/**
 * Created by yksenofontov on 08.07.2016.
 */
public class Launcher{
    public static void main(String[] args) {
//        initialization
//        add/update/delete examples
        EntityList entityList = new EntityList();
        entityList.addEntity("name1","d:\\11\11");
        entityList.addEntity("name2","d:\\22");
        entityList.addEntity("name3","d:\\33");
        entityList.addEntity("name4","d:\\4");
        Entity testEntity = entityList.getEntities().get(1);
        entityList.updateEntity(testEntity,"newName","newLink");
        Entity testEntity2 = entityList.getEntities().get(2);
        entityList.removeEntity(testEntity2);


        RegsWorker.loadRegs();
        if (args.length==0)
            new Informator();
        else
            new Informator(args);
    }
}