package fileHelper;

import org.apache.log4j.Logger;

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

    public void addEntity(String name, String link) {
       entities.add(new Entity(name,link));
       logger.info("New entity successfully added.");
    }

    public void removeEntity(Entity ent) {
        entities.remove(ent);
        logger.info("Entity successfully removed.");
    }

    public void updateEntity(Entity ent,String newName,String newLink) {
        int index = entities.indexOf(ent);
        entities.set(index,new Entity(newName,newLink));
        logger.info("Entity successfully updated.");
    }
}
