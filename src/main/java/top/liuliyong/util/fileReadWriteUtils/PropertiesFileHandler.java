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
        if (!checkIsPropFileExist(filePath)) {
            throw new FileNotFoundException(String.format("properties file not found in path %s", filePath));
        }
        Properties props = new Properties();
        InputStream inStream;
        inStream = getPropsInputStream(filePath, isIn);

        //读取配置文件编码格式,重载props
        String currentEncode = "UTF-8";
        {
            Properties tempProp = new Properties();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8));
            tempProp.load(reader);
            if (tempProp.getProperty("properties_encode") != null) {
                currentEncode = tempProp.getProperty("properties_encode");
            }
        }

        inStream = getPropsInputStream(filePath, isIn);
        BufferedReader encodingCorrectReader = new BufferedReader(new InputStreamReader(inStream, currentEncode));
        props.load(encodingCorrectReader);
        this.prop = props;
        this.filePath = filePath;
    }

    public String getProperty(String propName) {
        return prop.getProperty(propName);
    }

    public void coverProperty(Map<String, String> parameter) {
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

    public void coverProperty(String key, String value) {
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
            prop = properties;
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

    private InputStream getPropsInputStream(String filePath, boolean isIn) throws IOException {
        InputStream inStream;
        if (!isIn) {
            inStream = new FileInputStream(filePath);
        } else {
            inStream = PropertiesFileHandler.class.getClassLoader().getResourceAsStream(filePath);
        }
        assert inStream != null;
        return inStream;
    }

    private boolean checkIsPropFileExist(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists())
            return false;
        return true;
    }
}
