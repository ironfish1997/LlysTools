package top.liuliyong.util.fileReadWriteUtils;

import java.io.*;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * 存取properties文件
 */
public class PropertiesFileHandler {
    private volatile static PropertiesFileHandler propUtil;
    private static Properties prop;
    private String filePath;

    private PropertiesFileHandler(){

    }

    //读取项目内部的properties文件，isIn设为true
    public static PropertiesFileHandler PropertiesFileHandler(String filePath, boolean isIn) throws IOException {
        if (propUtil == null) {
            synchronized (PropertiesFileHandler.class) {
                if (propUtil == null) {
                    propUtil=new PropertiesFileHandler();
                    Properties props = new Properties();
                    propUtil.filePath = filePath;
                    Properties tempProp = new Properties();
                    String currentEncode;
                    try {
                        InputStream inStream;
                        if (!isIn) {
                            inStream = new FileInputStream(filePath);
                        } else {
                            inStream = PropertiesFileHandler.class.getClassLoader().getResourceAsStream(filePath);
                        }
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
                        tempProp.load(reader);
                        currentEncode = tempProp.getProperty("properties_encode");
                    }catch (IOException e){
                        throw e;
                    }
                    InputStream inStream;
                    if (!isIn) {
                        inStream = new FileInputStream(filePath);
                    } else {
                        inStream = PropertiesFileHandler.class.getClassLoader().getResourceAsStream(filePath);
                    }
                    BufferedReader encodingCorrectReader = new BufferedReader(new InputStreamReader(inStream, currentEncode));
                    props.load(encodingCorrectReader);
                    prop = props;
                }
            }
        }
        return propUtil;
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
