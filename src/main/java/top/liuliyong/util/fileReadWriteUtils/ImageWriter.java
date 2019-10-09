package top.liuliyong.util.fileReadWriteUtils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @Author liyong.liu
 * @Date 2019-08-05
 **/
public class ImageWriter {
    private String basePath;

    public ImageWriter(String basePath) {
        if (basePath.lastIndexOf("/") == basePath.length() - 1) {
            throw new IllegalArgumentException("base path cannot end with slash");
        }
        this.basePath = basePath;
    }

    public boolean writeImage(String imageName, byte[] content) {
        return writeImage(basePath + "/" + imageName, content);
    }


    public static boolean writeImage(String basePath, String imageName, byte[] content) throws IOException {
        if (basePath == null || basePath.trim().length() == 0 || imageName == null || imageName.length() == 0 || content == null || content.length == 0) {
            throw new IllegalArgumentException("illegal path or content");
        }
        File baseDir = new File(basePath);
        File imgFile = new File(basePath + "/" + imageName);
        synchronized (ImageWriter.class) {
            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }
            if (imgFile.exists()) {
                throw new IllegalArgumentException("image has existed");
            }
        }
        imgFile.createNewFile();
        FileOutputStream fs = new FileOutputStream(imgFile);
        fs.write(content);
        fs.flush();
        fs.close();
        return true;
    }

    public static boolean writeImageBase64(String basePath, String imageName, String imageBase64) throws IOException {
        byte[] content = Base64.getDecoder().decode(imageBase64);
        return writeImage(basePath, imageName, content);
    }
}
