package util.logging;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogWriter {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String fs = System.getProperties().getProperty("file.separator");

    private static void write(String fileName, String message, Exception exp, boolean addDate) {
        File logFile = new File(fileName);
        File logDir = new File("logs");
        try {
            if(!logDir.isDirectory()){
                logDir.mkdir();
            }
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, true)));
            if(addDate){
                writer.write(timeFormat.format(new Date()) + ": " + message);
            }else {
                writer.write(message);
            }
            writer.newLine();
            if (exp != null) {
                PrintWriter pw = new PrintWriter(writer);
                exp.printStackTrace(pw);
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void recordLog(String prefix, String message, Exception e) {
        String nowDate = dateFormat.format(new Date());
        String nowTime = timeFormat.format(new Date());
        String Log = String.format("%s====> %s", nowTime, message);
        if (e != null) {
            System.err.println(Log);
        }else {
            System.out.println(Log);
        }
        LogWriter.write("logs" + fs + prefix + "_" + nowDate + ".log", message, e, true);
    }

    public static void writeDirectly(String fileName, String message){
        String nowDate = dateFormat.format(new Date());
        write("logs" + fs + fileName + "_" + nowDate + ".log",message,null,false);
    }
}
