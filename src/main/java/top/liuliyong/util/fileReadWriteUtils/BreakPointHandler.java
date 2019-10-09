package com.yitutech.olive.sdd3501.base.util.fileReadWriteUtils;


import java.io.IOException;

/**
 * 断点续传工具类
 */
public class BreakPointHandler {
    private PropertiesFileHandler propertiesFileHandler;

    public BreakPointHandler(String propertiesName) {
        try {
            propertiesFileHandler = new PropertiesFileHandler(String.format("./%s.properties", propertiesName), false);
        } catch (IOException e) {
            throw new RuntimeException("read breakpoint file error", e);
        }
    }

    public String getBreakPoint() {
        return propertiesFileHandler.getProperty("breakPoint");
    }

    public void setBreakPoint(String breakPageNum) {
        propertiesFileHandler.setProperty("breakPoint", String.valueOf(breakPageNum));
    }
}
