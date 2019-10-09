package com.yitutech.olive.sdd3501.base.util.fileReadWriteUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CSVFileUtil {
    // CSV文件编码
    public static final String ENCODE = "UTF-8";

    private FileInputStream fis = null;
    private InputStreamReader isw = null;
    private BufferedReader br = null;
    private static Map<String, CSVFileUtil> utilCache;
    private List<String> lines = new ArrayList<>();
    private AtomicInteger rowCount = new AtomicInteger(0);

    public CSVFileUtil(String filename) {
        try {
            fis = new FileInputStream(filename);
            isw = new InputStreamReader(fis, ENCODE);
            br = new BufferedReader(isw);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ==========公开方法=============================

    /**
     * 从CSV文件流中读取一个CSV行。
     *
     * @throws Exception
     */
    public String readLine() throws Exception {

        StringBuffer readLine = new StringBuffer();
        boolean bReadNext = true;

        while (bReadNext) {
            //
            if (readLine.length() > 0) {
                readLine.append("\r\n");
            }
            // 一行
            String strReadLine = br.readLine();

            // readLine is Null
            if (strReadLine == null) {
                return null;
            }
            readLine.append(strReadLine);
            rowCount.incrementAndGet();
            // 如果双引号是奇数的时候继续读取。考虑有换行的情况。
            if (countChar(readLine.toString(), '"', 0) % 2 == 1) {
                bReadNext = true;
            } else {
                bReadNext = false;
            }
        }
        return readLine.toString();
    }

    public List<String> readLines() throws Exception {
        if (rowCount.get() != 0 && this.lines == null) {
            throw new IllegalAccessException("The header point has been changed");
        }
        if (this.lines != null && this.lines.size() != 0) {
            return lines;
        }
        String line = "";
        synchronized (lines) {
            while ((line = readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    public String[] getColumnByColumnName(String colName) {
        List<String> lines = null;
        try {
            lines = readLines();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int count = -1;
        String[] headers = lines.get(0).split(",");
        for (int i = 0; i < headers.length; i++) {
            if (colName.equals(headers[i])) {
                count = i;
            }
        }
        //can't find the col
        if (count == -1) {
            return null;
        }
        String[] result = new String[lines.size() - 1];
        for (int i = 1; i < lines.size(); i++) {
            result[i - 1] = lines.get(i).split(",")[count];
        }
        return result;
    }

    public String[][] getColumnsByColumnNames(String[] colNames) {
        if (colNames == null || colNames.length == 0) {
            return null;
        }
        List<String> lines = null;
        try {
            lines = readLines();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[][] cache = new String[getColumnByColumnName(colNames[0]).length][colNames.length];
        for (int i = 0; i < colNames.length; i++) {
            String[] colRes = getColumnByColumnName(colNames[i]);
            for (int j = 0; j < colRes.length; j++) {
                cache[j][i] = colRes[j];
            }
        }
        return cache;
    }


    /**
     * 把CSV文件的一行转换成字符串数组。指定数组长度，不够长度的部分设置为null。
     */
    public static String[] fromCSVLine(String source, int size) {
        ArrayList tmpArray = fromCSVLinetoArray(source);
        if (size < tmpArray.size()) {
            size = tmpArray.size();
        }
        String[] rtnArray = new String[size];
        tmpArray.toArray(rtnArray);
        return rtnArray;
    }


    /**
     * 把CSV文件的一行转换成字符串数组。不指定数组长度。
     */
    public static ArrayList fromCSVLinetoArray(String source) {
        if (source == null || source.length() == 0) {
            return new ArrayList();
        }
        int currentPosition = 0;
        int maxPosition = source.length();
        int nextComma = 0;
        ArrayList rtnArray = new ArrayList();
        while (currentPosition < maxPosition) {
            nextComma = nextComma(source, currentPosition);
            rtnArray.add(nextToken(source, currentPosition, nextComma));
            currentPosition = nextComma + 1;
            if (currentPosition == maxPosition) {
                rtnArray.add("");
            }
        }
        return rtnArray;
    }


    /**
     * 把字符串类型的数组转换成一个CSV行。（输出CSV文件的时候用）
     */
    public static String toCSVLine(String[] strArray) {
        if (strArray == null) {
            return "";
        }
        StringBuffer cvsLine = new StringBuffer();
        for (int idx = 0; idx < strArray.length; idx++) {
            String item = addQuote(strArray[idx]);
            cvsLine.append(item);
            if (strArray.length - 1 != idx) {
                cvsLine.append(',');
            }
        }
        return cvsLine.toString();
    }

    /**
     * 字符串类型的List转换成一个CSV行。（输出CSV文件的时候用）
     */
    public static String toCSVLine(ArrayList strArrList) {
        if (strArrList == null) {
            return "";
        }
        String[] strArray = new String[strArrList.size()];
        for (int idx = 0; idx < strArrList.size(); idx++) {
            strArray[idx] = (String) strArrList.get(idx);
        }
        return toCSVLine(strArray);
    }

    /**
     * 计算指定文字的个数。
     *
     * @param str   文字列
     * @param c     文字
     * @param start 开始位置
     * @return 个数
     */
    private int countChar(String str, char c, int start) {
        int i = 0;
        int index = str.indexOf(c, start);
        return index == -1 ? i : countChar(str, c, index + 1) + 1;
    }

    /**
     * 查询下一个逗号的位置。
     *
     * @param source 文字列
     * @param st     检索开始位置
     * @return 下一个逗号的位置。
     */
    private static int nextComma(String source, int st) {
        int maxPosition = source.length();
        boolean inquote = false;
        while (st < maxPosition) {
            char ch = source.charAt(st);
            if (!inquote && ch == ',') {
                break;
            } else if ('"' == ch) {
                inquote = !inquote;
            }
            st++;
        }
        return st;
    }

    /**
     * 取得下一个字符串
     */
    private static String nextToken(String source, int st, int nextComma) {
        StringBuffer strb = new StringBuffer();
        int next = st;
        while (next < nextComma) {
            char ch = source.charAt(next++);
            if (ch == '"') {
                if ((st + 1 < next && next < nextComma) && (source.charAt(next) == '"')) {
                    strb.append(ch);
                    next++;
                }
            } else {
                strb.append(ch);
            }
        }
        return strb.toString();
    }

    /**
     * 在字符串的外侧加双引号。如果该字符串的内部有双引号的话，把"转换成""。
     *
     * @param item 字符串
     * @return 处理过的字符串
     */
    private static String addQuote(String item) {
        if (item == null || item.length() == 0) {
            return "\"\"";
        }
        StringBuffer sb = new StringBuffer();
        sb.append('"');
        for (int idx = 0; idx < item.length(); idx++) {
            char ch = item.charAt(idx);
            if ('"' == ch) {
                sb.append("\"\"");
            } else {
                sb.append(ch);
            }
        }
        sb.append('"');
        return sb.toString();
    }
}
