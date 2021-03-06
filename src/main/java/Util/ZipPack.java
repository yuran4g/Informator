package Util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Pack files and directories utility class
 */
public class ZipPack {

    private final static Logger logger = Logger.getLogger(FileZip.class);
    private final static String ARCHIVEFILE = "logs.zip";
    // Paths to file and directory that you want to pack
    private String packFilePath;
    private String packDirectoryPath;
    private static int CompressionLevel=Deflater.DEFAULT_COMPRESSION;

    /**
     * Pack single file.
     *
     * @throws IOException
     */
    public String packFile() throws IOException {
        // Create the ZIP output file
        // File name is the same as the packed file
        // but the extension is changed
        logger.info("Start pack " + packFilePath);
        String outputFile = generatePath() + "\\" + ARCHIVEFILE;
        new File(outputFile).createNewFile();

        // Open the output stream to the destination file
        FileOutputStream fos = new FileOutputStream(outputFile);

        // Open the zip stream to the output file
        ZipOutputStream zos = new ZipOutputStream(fos);
        //zos.setLevel(Deflater.BEST_COMPRESSION);//1.06
        //zos.setLevel(Deflater.BEST_SPEED);//0.17
        zos.setLevel(CompressionLevel);//0.14

        // Create a zip entry conatining packed file name
        ZipEntry ze = new ZipEntry(new File(packFilePath).getName());
        zos.putNextEntry(ze);

        // Open input stream to packed file
        FileInputStream fis = new FileInputStream(packFilePath);

        // An array to which will hold byte being read from the packed file
        byte[] bytesRead = new byte[512];

        // Read bytes from packed file and store them in the ZIP output stream
        int bytesNum;
        while ((bytesNum = fis.read(bytesRead)) > 0) {
            zos.write(bytesRead, 0, bytesNum);
        }

        // Close all streams
        fis.close();
        zos.closeEntry();
        zos.close();
        fos.close();
        logger.info("Pack successfully finished");
        return outputFile;
    }

    /**
     * Packs the given directory.
     *
     * @throws IOException
     */
    public String packDirectory() throws IOException {
        // The output zip file name
        String outputFile = generatePath() + "\\" + ARCHIVEFILE;
        logger.info("Start pack " + packDirectoryPath);
        // Open streams to write the ZIP contents to
        FileOutputStream fos = new FileOutputStream(outputFile);
        ZipOutputStream zos = new ZipOutputStream(fos);

        // iterate directory structure recursively and add zip entries
        packCurrentDirectoryContents(packDirectoryPath, zos);

        // Close the streams
        zos.closeEntry();
        zos.close();
        fos.close();
        logger.info("Pack successfully finished");
        return outputFile;
    }

    /**
     * Recursively pack directory contents.
     *
     * @param directoryPath - current directory path that is visited recursively
     * @param zos           - ZIP output stream reference to add elements to
     * @throws IOException
     */
    private void packCurrentDirectoryContents(String directoryPath, ZipOutputStream zos) throws IOException {

        File dir = new File(directoryPath);
        String[] dirElements = dir.list();

        // Add empty folder
        if (dirElements.length == 0 && dir.isDirectory()) {
            ZipEntry ze= new ZipEntry(directoryPath.replace(packDirectoryPath+"\\", "") + "\\");
            zos.putNextEntry(ze);
        }

        // Iterate through the directory elements
        for (String dirElement : dirElements) {

            // Construct each element full path
            String dirElementPath = directoryPath + "\\" + dirElement;

            // For directories - go down the directory tree recursively
            if (new File(dirElementPath).isDirectory()) {
                packCurrentDirectoryContents(dirElementPath, zos);

            } else {
                // For files add the a ZIP entry
                // THIS IS IMPORTANT: a ZIP entry needs to be a relative path to the file
                // so we cut off the path to the directory that is being packed.
                ZipEntry ze = new ZipEntry(dirElementPath.replace(packDirectoryPath + "\\", ""));
                zos.putNextEntry(ze);

                // Open input stream to packed file
                FileInputStream fis = new FileInputStream(dirElementPath);

                // An array to which will hold byte being read from the packed file
                byte[] bytesRead = new byte[512];

                // Read bytes from packed file and store them in the ZIP output stream
                int bytesNum;
                while ((bytesNum = fis.read(bytesRead)) != -1) {
                    zos.write(bytesRead, 0, bytesNum);
                }

                // Close the stream
                fis.close();
            }
        }
    }

    // Setters

    /**
     * @param packFilePath - a file name that is going to be packed
     **/
    public void setPackFilePath(String packFilePath) {
        this.packFilePath = packFilePath;
    }

    /**
     * @param packDirectoryPath - the directory that is going to be packed
     **/
    public void setPackDirectoryPath(String packDirectoryPath) {
        this.packDirectoryPath = packDirectoryPath;
    }

    private static String generatePath() {
        Random rand = new Random();
        Integer value = rand.nextInt(1000000);
        String tempDir = System.getProperty("user.dir") + "\\TEMP";
        if (!new File(tempDir).exists()){
            new File(tempDir).mkdir();
        }
        String genPath = tempDir + "\\" + value.toString();
        File file = new File(genPath);
        file.mkdir();
        logger.info("New folder generated " + genPath);
        return genPath;
    }
}