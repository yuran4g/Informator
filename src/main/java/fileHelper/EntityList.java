package fileHelper;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yksenofontov on 27.07.2016.
 */
public final class EntityList {
    private final static Logger logger = Logger.getLogger(EntityList.class);
    private static List<Entity> entities = new ArrayList<Entity>();
    private static String ENTITYFILE = "entityList.txt";

    private EntityList(){}

    public static List<Entity> getEntities() {
        return entities;
    }

    public static Entity getEntity(String name){
        for (Entity e:entities){
            if (e.getName().equals(name))return e;
        }
        return null;
    }

    public static void addEntity(String name, String link) throws Exception {
        entities.add(new Entity(name, link));
        logger.info("New entity successfully added.");
    }

    public static void removeEntity(Entity ent) {
        entities.remove(ent);
        logger.info("Entity successfully removed.");
    }

    public static void updateEntity(Entity ent, String newName, String newLink) throws Exception {
        File file = new File(newLink);
        if (file.exists()) {
            int index = entities.indexOf(ent);
            entities.set(index, new Entity(newName, newLink));
            logger.info("Entity successfully updated.");
        } else {
            throw new Exception("Path doesn't exist");
        }
    }

    public static void saveEntityList() throws Exception{
        FileWriter writer = new FileWriter(ENTITYFILE);
        for(Entity entity: EntityList.getEntities()) {
            writer.write(entity.getName()+";"+entity.getLink()+"\n");
        }
        writer.close();
        logger.info("Entitylist successfully saved");
    }

    public static void loadEntityList() throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(ENTITYFILE));
        String line;
        while ((line = reader.readLine()) != null) {
            addEntity(line.split(";")[0],line.split(";")[1]);
        }
        reader.close();
        logger.info("Entitylist successfully loaded");
    }
}
