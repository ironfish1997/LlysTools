package util.logging;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogWriter {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    public static void writeLog(String message) throws IOException {
        String nowDate = dateFormat.format(new Date());
        String logPath = "log_" + nowDate + ".log";
        File logFile = new File(logPath);
        if (!logFile.exists())
            logFile.createNewFile();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile)));
        writer.write(timeFormat.format(new Date())+": "+message);
    }
}
