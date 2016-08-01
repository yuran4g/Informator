package fileHelper;

import Util.FileZip;
import Util.ZipPack;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

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

    public String archive() throws IOException{
        String archivePath;
        ZipPack zp = new ZipPack();
        String path = getLink();
        if (new File(path).isDirectory()) {
            zp.setPackDirectoryPath(path);
                archivePath = zp.packDirectory();
        } else {
            zp.setPackFilePath(path);
            archivePath = zp.packFile();
        }
        logger.info("Entity successfully archived");
        return archivePath;
    }

    public void clean() throws Exception {
        File file = new File(Link);
        if (file.isFile() && file.exists()) {
            file.delete();
            logger.info("Entity successfully cleaned");
        } else {
            if (file.isDirectory() && file.exists()) {
                deleteFolder(file);
            } else {
                throw new Exception("Can not find file/dir");
            }
            logger.info("Entity successfully cleaned");
        }
    }

    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) {
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }
}
