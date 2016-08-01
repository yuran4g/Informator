package Util;

import fileHelper.Entity;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by yksenofontov on 30.07.2016.
 */
public class FileZip {
    private final static Logger logger = Logger.getLogger(FileZip.class);
    private final static String ARCHIVEFILE = "logs.zip";

    private static String generatePath() {
        Random rand = new Random();
        Integer value = rand.nextInt(1000000);
        String genPath = System.getProperty("user.dir") + "\\" + value.toString();
        File file = new File(genPath);
        file.mkdir();
        return genPath;
    }

    private static List<String> getContent(String path) {
        List<String> content = new ArrayList<String>();
        File file = new File(path);
        if (file.isFile()) {
            content.add(path);
            return content;
        } else {

            File[] subNote = file.listFiles();
            for (File filename : subNote) {
                content.add(filename.getAbsolutePath());
            }
        }
        return new ArrayList<String>();
    }

    public static String zipEntity(String source) {
        String zipPath = generatePath();
        String zipFile = zipPath + "\\" + ARCHIVEFILE;
        List<String> sourceContent = getContent(source);
        try {
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            for (String filename : sourceContent) {
                addToZipFile(filename, zos);
            }
            zos.close();
            fos.close();

        } catch (FileNotFoundException e) {
            logger.error("File can not be found");
        } catch (IOException e) {
            logger.error("Can not saev file to archive");
        }
        return zipPath;
    }

    private static void addToZipFile(String fileName, ZipOutputStream zos) throws IOException {

        System.out.println("Writing '" + fileName + "' to zip file");
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zos.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }
        zos.closeEntry();
        fis.close();
    }
}