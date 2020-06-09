package com.kuangji.paopao.util.excel;

import com.alibaba.fastjson.JSON;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PoiUtil {

    private final static String excel2003L =".xls";    //2003- 版本的excel
    private final static String excel2007U =".xlsx";   //2007+ 版本的excel

    /**
     * 描述：获取IO流中的数据，组装成List<List<Object>>对象
     * @param file
     * @return
     * @throws Exception
     */
    public static  List<List<Object>> importExcel(File file) throws Exception{
        List<List<Object>> list = null;

        //创建Excel工作薄
        Workbook work = getWorkbook(file);
        if(null == work){
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        list = new ArrayList<List<Object>>();
        //遍历Excel中所有的sheet
        for (int i = 0; i <work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if(sheet==null){continue;}

            //遍历当前sheet中的所有行
            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if(row==null||row.getFirstCellNum()==j){continue;}

                //遍历所有的列
                List<Object> li = new ArrayList<Object>();
                for (int y = row.getFirstCellNum(); y <row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    li.add(getCellValue(cell));
                }
                list.add(li);
            }
        }
        return list;
    }

    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     * @param file
     * @return
     * @throws Exception
     */
    public static  Workbook getWorkbook(File file) throws Exception{
        Workbook wb = null;
        String fileType = file.getName().substring(file.getName().lastIndexOf("."));
        if(excel2003L.equals(fileType)){
            wb = new HSSFWorkbook(new FileInputStream(file));  //2003-
        }else if(excel2007U.equals(fileType)){
            wb = new XSSFWorkbook(new FileInputStream(file));  //2007+
        }else{
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }

    /**
     * 描述：对表格中数值进行格式化
     * @param cell
     * @return
     */
    public static Object getCellValue(Cell cell){
        //用String接收所有返回的值
        String value = null;
        DecimalFormat df = new DecimalFormat("0");  //格式化number String字符
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");  //日期格式化
        DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:  //String类型的数据
                value =  cell.getStringCellValue();
                break;

            case Cell.CELL_TYPE_NUMERIC:   //数值类型(取值用cell.getNumericCellValue() 或cell.getDateCellValue())
                if("General".equals(cell.getCellStyle().getDataFormatString())){
                    value = df.format(cell.getNumericCellValue());
                }else if(HSSFDateUtil.isCellDateFormatted(cell)){
                    value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                }else{
                    value = df2.format(cell.getNumericCellValue());
                }
                break;

            case Cell.CELL_TYPE_BOOLEAN:  //Boolean类型
                value = String.valueOf(cell.getBooleanCellValue());
                break;


            case Cell.CELL_TYPE_FORMULA: //表达式类型
                value = String.valueOf(cell.getCellFormula());
                break;

            case Cell.CELL_TYPE_ERROR: //异常类型 不知道何时算异常
                value=String.valueOf(cell.getErrorCellValue());
                break;

            case Cell.CELL_TYPE_BLANK:  //空，不知道何时算空
                value = "";
                break;

            default:
                value = "";
                break;
        }
        if(value.equals("")||value==null){
            value = "";
        }
        if (cell == null) {
            return "";
        }
        return value;
    }

    //读取excel里的字段
    public static void main(String[] args){
        //excel 导入数据demo
        File file = new File("/Users/mac/Documents/data/company.xls");
        List<List<Object>> dataList= null;
        try {
            dataList = PoiUtil.importExcel(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //数据封装格式一，将表格中的数据遍历取出后封装进对象放进List
        for (int i = 0; i <dataList.size(); i++) {
                Object companyName = dataList.get(i).get(0);
                Object shortName = dataList.get(i).get(1);
                Object contact = dataList.get(i).get(2);
                Object phone = dataList.get(i).get(3);
                System.out.println("------------------");
                System.out.println((String)companyName+":"+(String)companyName);
        }
        System.out.println(JSON.toJSONString(dataList));
    }
}
