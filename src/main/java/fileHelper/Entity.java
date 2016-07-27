package fileHelper;

import org.apache.log4j.Logger;

/**
 * Created by yksenofontov on 27.07.2016.
 */
public class Entity {
    private final static Logger logger = Logger.getLogger(Entity.class);
    private String Name;
    private String Link;

    public Entity(String name, String link) {
        Name = name;
        Link = link;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String archive(){
        logger.info("Entity successfully archived");
        return "";
    }
    public String clean(){
        logger.info("Entity successfully cleaned");
        return "";
    }
}
