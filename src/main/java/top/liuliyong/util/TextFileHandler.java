package top.liuliyong.util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 文本文件处理类
 *
 * @Author: Liyong.liu
 * @Date: 2020/4/9
 */
public class TextFileHandler {

    public synchronized static void readTextFileByLine(String filePath, Charset encode,BreakPointHandler breakPoint, LineHandler lineHandler) throws IOException {
        File f = new File(filePath);
        if (!f.exists() || f.isDirectory()) {
            throw new FileNotFoundException(String.format("%s文件不存在", filePath));
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), encode), 100);
        String line;
        int currentLine = 0;
        String bpStr;
        int i = breakPoint == null ? -1 : (bpStr = breakPoint.getBreakPoint()) == null ? -1 : Integer.parseInt(bpStr);
        while ((line = br.readLine()) != null) {
            if (currentLine > i) {
                lineHandler.handleLine(currentLine, line);
                if (breakPoint != null) {
                    breakPoint.setBreakPoint(currentLine + "");
                }
            }
            currentLine++;
        }
    }

    public synchronized static List<String> readTextFileByLine(String filePath, Charset encode, BreakPointHandler breakPoint) throws IOException {
        File f = new File(getFilePath(filePath));
        if (!f.exists() || f.isDirectory()) {
            throw new FileNotFoundException(String.format("%s文件不存在", filePath));
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), encode), 100);
        String line;
        int currentLine = 0;
        String bpStr = null;
        int breakpoint = (bpStr = breakPoint.getBreakPoint()) == null ? -1 : Integer.parseInt(bpStr);
        List<String> result = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            if (currentLine > breakpoint) {
                result.add(line);
                breakPoint.setBreakPoint(currentLine + "");
            }
            currentLine++;
        }
        return result;
    }

    private static String getFilePath(String filePath) {
        return filePath;
    }

    public static List<String> readTextFileByLine(String filePath, Charset encode) throws IOException {
        File f = new File(filePath);
        if (!f.exists() || f.isDirectory()) {
            throw new FileNotFoundException(String.format("%s文件不存在", filePath));
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), encode), 100);
        String line;
        List<String> result = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            result.add(line);
        }
        return result;
    }
}
