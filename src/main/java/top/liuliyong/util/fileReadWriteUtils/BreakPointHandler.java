package top.liuliyong.util.fileReadWriteUtils;


import java.io.File;
import java.io.IOException;

/**
 * 断点续传工具类
 */
public class BreakPointHandler {
    private PropertiesFileHandler propertiesFileHandler;

    public BreakPointHandler(String propertiesName) {
        try {
            checkIsPropFileExist(propertiesName);
            propertiesFileHandler = new PropertiesFileHandler(String.format("./props/%s.properties", propertiesName), false);
        } catch (IOException e) {
            throw new RuntimeException("read breakpoint file error", e);
        }
    }

    public String getBreakPoint() {
        return propertiesFileHandler.getProperty("breakPoint");
    }

    public void setBreakPoint(String breakPoint) {
        propertiesFileHandler.coverProperty("breakPoint", String.valueOf(breakPoint));
    }

    private void checkIsPropFileExist(String propertiesName) throws IOException {
        File fatherDir = new File("./props/");
        File file = new File("./props/" + propertiesName + ".properties");
        if (!fatherDir.exists()) {
            fatherDir.mkdirs();
        }
        if (!file.exists())
            file.createNewFile();
    }
}
