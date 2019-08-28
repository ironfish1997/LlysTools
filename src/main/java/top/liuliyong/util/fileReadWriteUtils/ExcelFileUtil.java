package top.liuliyong.util.fileReadWriteUtils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellType.STRING;

public class ExcelFileUtil {
    private String filePath;
    private int fileType;//1为xls，2为xlsx

    public ExcelFileUtil(String filePath, Integer fileType) {
        assert (filePath != null && filePath.trim().length() != 0);
        this.filePath = filePath;
        assert (fileType == 1 || fileType == 2);
        this.fileType = fileType;
    }

    public String[] getColumnByColumnName(String colName) throws IOException {
        List<List<String>> lines = getRecords();
        int count = -1;
        Object[] temp = lines.get(0).toArray();
        String[] headers = new String[temp.length];
        for(int i =0 ; i<temp.length;i++){
            headers[i] = temp[i]+"";
        }
        {
            int i = 0;
            while (i < headers.length) {
                if (colName.equals(headers[i])) {
                    count = i;
                }
                i++;
            }
        }
        //can't find the col
        if (count == -1) {
            return null;
        }
        String[] result = new String[lines.size() - 1];
        for (int i = 1; i < lines.size(); i++) {
            try{
                result[i - 1] = lines.get(i).get(count);
            }catch (Exception e){
                result[i-1] = "";
            }
        }
        return result;
    }

    public String[][] getColumnsByColumnNames(String[] colNames) throws IOException {
        if (colNames == null || colNames.length == 0) {
            return null;
        }
        List<List<String>> lines = getRecords();
        String[][] cache = new String[getColumnByColumnName(colNames[0]).length][colNames.length];
        for (int i = 0; i < colNames.length; i++) {
            String[] colRes = getColumnByColumnName(colNames[i]);
            for (int j = 0; j < colRes.length; j++) {
                cache[j][i] = colRes[j];
            }
        }
        return cache;
    }


    public static List<List<String>> readXls(String path) throws IOException {
        InputStream is = new FileInputStream(path);
        // HSSFWorkbook 标识整个excel
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        List<List<String>> result = new ArrayList<List<String>>();
        int size = hssfWorkbook.getNumberOfSheets();
        // 循环每一页，并处理当前循环页
        for (int numSheet = 0; numSheet < size; numSheet++) {
            // HSSFSheet 标识某一页
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            int headCount = -1;
            boolean firstRow = true;
            // 处理当前页，循环读取每一行
            for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                // HSSFRow表示行
                HSSFRow hssfRow = null;
                if((hssfRow=hssfSheet.getRow(rowNum))!=null){
                    int minColIx = hssfRow.getFirstCellNum();
                    int maxColIx = hssfRow.getLastCellNum();
                    if (minColIx==maxColIx)
                        continue;
                    List<String> rowList = new ArrayList<String>();
                    for (int colIx = 0; colIx<minColIx;colIx++){
                        rowList.add("");
                    }
                    for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                        Cell cell = hssfRow.getCell(colIx);
                        if (cell == null) {
                            rowList.add("");
                        }
                        rowList.add(cell.toString());
                    }
                    if(firstRow){
                        headCount = maxColIx-minColIx;
                        firstRow=false;
                    }
                    for (int colIx = maxColIx;colIx<headCount;colIx++){
                        rowList.add("");
                    }
                    result.add(rowList);
                }
            }
        }
        return result;
    }


    /**
     * @param @param  path
     * @param @return
     * @param @throws Exception    设定文件
     * @return List<List   <   String>>    返回类型
     * @throws
     * @Title: readXlsx
     * @Description: 处理Xlsx文件
     */
    public static List<List<String>> readXlsx(String path) throws IOException {
        List<List<String>> result = new ArrayList<List<String>>();
        try (InputStream is = new FileInputStream(path)) {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
            // 循环每一页，并处理当前循环页
            for (Sheet xssfSheet : xssfWorkbook) {
                if (xssfSheet == null) {
                    continue;
                }
                int headCount = -1;
                boolean firstRow = true;
                // 处理当前页，循环读取每一行
                for (int rowNum = 0; rowNum < xssfSheet.getLastRowNum(); rowNum++) {
                    Row xssfRow=null;
                    if((xssfRow=xssfSheet.getRow(rowNum))!=null){
                        int minColIx = xssfRow.getFirstCellNum();
                        int maxColIx = xssfRow.getLastCellNum();
                        if (minColIx==maxColIx)
                            continue;
                        List<String> rowList = new ArrayList<String>();
                        for (int colIx = 0; colIx<minColIx;colIx++){
                            rowList.add("");
                        }
                        for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                            Cell cell = xssfRow.getCell(colIx);
                            if (cell == null) {
                                rowList.add("");
                            }
                            rowList.add(cell.toString());
                        }
                        if(firstRow){
                            headCount = maxColIx-minColIx;
                            firstRow=false;
                        }
                        for (int colIx = maxColIx;colIx<headCount;colIx++){
                            rowList.add("");
                        }
                        result.add(rowList);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }


    /**
     * 改造poi默认的toString（）方法如下
     *
     * @param @param  cell
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: getStringVal
     * @Description: 1.对于不熟悉的类型，或者为空则返回""控制串
     * 2.如果是数字，则修改单元格类型为String，然后返回String，这样就保证数字不被格式化了
     */
    private static String getStringVal(HSSFCell cell) {
        switch (cell.getCellType()) {
            case BOOLEAN:
                return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
            case FORMULA:
                return cell.getCellFormula();
            case NUMERIC:
                cell.setCellType(STRING);
                return cell.getStringCellValue();
            case STRING:
                return cell.getStringCellValue();
            default:
                return "";
        }
    }

    private List<List<String>> getRecords() throws IOException {
        List<List<String>> lines = null;
        if (fileType == 1) {
            lines = readXls(filePath);
        } else {
            lines = readXlsx(filePath);
        }
        return lines;
    }
}