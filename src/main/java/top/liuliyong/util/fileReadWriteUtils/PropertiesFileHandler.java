package top.liuliyong.util.fileReadWriteUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * 存取properties文件
 */
public class PropertiesFileHandler {
    private volatile Properties prop;
    private volatile String filePath;


    //读取项目内部的properties文件，isIn设为true
    public PropertiesFileHandler(String filePath, boolean isIn) throws IOException {
        Properties props = new Properties();
        InputStream inStream;
        File fatherDir = new File("./props/");
        File file = new File("./props/" + filePath);
        if (!fatherDir.exists()) {
            fatherDir.mkdirs();
        }
        if (!file.exists())
            file.createNewFile();
        if (!isIn) {
            inStream = new FileInputStream("./props/" + filePath);
        } else {
            inStream = PropertiesFileHandler.class.getClassLoader().getResourceAsStream("./props/" + filePath);
        }
        assert inStream != null;
        BufferedReader encodingCorrectReader = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8));
        props.load(encodingCorrectReader);
        this.prop = props;
        this.filePath = "./props/" + filePath;
    }

    public String getProperty(String propName) {
        return prop.getProperty(propName);
    }

    public void setProperty(Map<String, String> parameter) {
        Properties properties = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream(filePath);
            for (Map.Entry<String, String> entry : parameter.entrySet()) {
                properties.setProperty(entry.getKey(), entry.getValue());
            }
            properties.store(output, "user modify" + new Date().toString());// 保存键值对到文件中
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setProperty(String key, String value) {
        if (key == null) {
            return;
        }
        Properties properties = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream(filePath);
            properties.setProperty(key, value);
            properties.store(output, "user modify" + new Date().toString());// 保存键值对到文件中
            output.flush();
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
