package top.liuliyong.util.fileReadWriteUtils;


import java.io.IOException;

/**
 * 断点续传工具类
 */
public class BreakPointHandler {
    private PropertiesFileHandler propertiesFileHandler;

    private static class BreakPointHandlerHolder {
        private static final BreakPointHandler INSTANCE = new BreakPointHandler();
    }

    public static BreakPointHandler getInstance() {
        return BreakPointHandlerHolder.INSTANCE;
    }

    private BreakPointHandler() {
        try {
            propertiesFileHandler = new PropertiesFileHandler("./time.properties", false);
        } catch (IOException e) {
            throw new RuntimeException("read breakpoint file error", e);
        }
    }

    public Long getBreakPoint() {
        String breakPageNo = propertiesFileHandler.getProperty("breakPageNo");
        return breakPageNo == null ? null : Long.parseLong(breakPageNo);
    }

    public void setBreakPoint(Long breakPageNum) {
        propertiesFileHandler.setProperty("breakPageNo", String.valueOf(breakPageNum));
    }
}
