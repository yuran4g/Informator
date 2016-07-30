package Util;

import java.io.File;

/**
 * Created by Андрей on 31.07.2016.
 */
public final class Cleaner {
    private Cleaner() {
    }

    public static void Delete(String Path) {
        File f=new File(Path);
        delete(f);
    }

    private static void delete(File f) {
        if (f.isDirectory()){
            deleteDir(f);
        }
        f.delete();
    }

    private static void deleteDir(File f) {
        for (File file:f.listFiles()){
            if (file.isDirectory()){
                deleteDir(file);
            }
            file.delete();
        }
    }
}
