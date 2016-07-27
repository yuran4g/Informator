package fileHelper;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yksenofontov on 27.07.2016.
 */
public class EntityList {
    private final static Logger logger = Logger.getLogger(EntityList.class);
    private List<Entity> entities = new ArrayList<Entity>();

    public List<Entity> getEntities() {
        return entities;
    }

    public void addEntity(String name, String link) throws Exception {
        File file = new File(link);
        if (file.exists()) {
            entities.add(new Entity(name, link));
        } else {
            throw new Exception("Path doesn't exist");
        }
        logger.info("New entity successfully added.");
    }

    public void removeEntity(Entity ent) {
        entities.remove(ent);
        logger.info("Entity successfully removed.");
    }

    public void updateEntity(Entity ent, String newName, String newLink) throws Exception {
        File file = new File(newLink);
        if (file.exists()) {
            int index = entities.indexOf(ent);
            entities.set(index, new Entity(newName, newLink));
            logger.info("Entity successfully updated.");
        } else {
            throw new Exception("Path doesn't exist");
        }
    }
}
