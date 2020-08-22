package top.liuliyong.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @Author liyong.liu
 * @Date 2019-08-05
 **/
public class ImageWriter {
    private final String basePath;

    public ImageWriter(String basePath) {
        if (basePath.lastIndexOf("/") == basePath.length() - 1) {
            throw new IllegalArgumentException("base path cannot end with slash");
        }
        this.basePath = basePath;
    }

    public void writeImage(String imageName, byte[] content) {
        writeImage(basePath + "/" + imageName, content);
    }


    public static void writeImage(String basePath, String imageName, byte[] content) throws IOException {
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
                return;
            }
        }
        imgFile.createNewFile();
        FileOutputStream fs = new FileOutputStream(imgFile);
        fs.write(content);
        fs.flush();
        fs.close();
    }


    /**
     * 图片base64转化成byte数组
     *
     * @param imageBase64
     * @return
     */
    public static byte[] base64toImage(String imageBase64) {
        byte[] b1;
        if (imageBase64.contains("data:image/jpeg;base64,")) {
            b1 = Base64.getDecoder().decode(imageBase64.replaceAll("data:image/jpeg;base64,", ""));
        } else {
            if (imageBase64.contains("data:image/png;base64,")) {
                b1 = Base64.getDecoder().decode(imageBase64.replaceAll("data:image/png;base64,", ""));
            } else {
                b1 = Base64.getDecoder().decode(imageBase64.replaceAll("data:image/jpg;base64,", ""));
            }
        }
        for (int i = 0; i < b1.length; ++i) {
            if (b1[i] < 0) {
                // 调整异常数据
                b1[i] += 256;
            }
        }
        return b1;
    }
}
