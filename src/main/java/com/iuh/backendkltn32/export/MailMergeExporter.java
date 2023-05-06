package com.iuh.backendkltn32.export;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MailMergeExporter {
	
	private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    
    
    
    public MailMergeExporter() {
		super();
		this.workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() throws IOException {
        sheet = workbook.createSheet("MailMerge_PhieuChamHD");
         
        Row row = sheet.createRow(0);
        
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        XSSFFont font = workbook.createFont();

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderRight(BorderStyle.MEDIUM);
       
        font.setBold(true);
        font.setFontHeight(12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        font.setFontHeightInPoints((short) 12);
         
        createCell(row, 0, "STT Nhóm", style);    
        createCell(row, 1, "Mã SV 1", style);      
        createCell(row, 2, "Họ tên SV 1", style);       
        createCell(row, 3, "Email SV1", style);    
        createCell(row, 4, "Mã SV 2", style);
        createCell(row, 5, "Họ tên SV2", style);
        createCell(row, 6, "Email SV 2", style);
        createCell(row, 7, "Mã đề tài", style);
        createCell(row, 8, "GVHD", style);
        createCell(row, 9, "KQ_GVHD_sv1", style);
        createCell(row, 10, "KQ-PB1_SV1", style);
        createCell(row, 11, "KQ_PB2_sv1", style);
        createCell(row, 12, "KQ_GVHD_sv2", style);
        createCell(row, 13, "KQ-PB1_SV2", style);
        createCell(row, 14, "KQ_PB2_sv2", style);
        createCell(row, 15, "Hình thức báo cáo", style);
        createCell(row, 16, "Hội đồng", style);
        createCell(row, 17, "TV_HD1", style);
        createCell(row, 18, "TV_HD2", style);
        createCell(row, 19, "TV_HD3", style);
         
    }
    
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
   	 sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);

        if (value instanceof Integer){
            cell.setCellValue((Integer) value);
        }else if (value instanceof Double){
            cell.setCellValue((Double) value);
        }else if (value instanceof Boolean){
            cell.setCellValue((Boolean) value);
        }else if (value instanceof Long){
            cell.setCellValue((Long) value);
        }else {
            cell.setCellValue((String) value);
            sheet.autoSizeColumn(columnCount);
        }
        cell.setCellStyle(style);
   }
    
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
//        writeDataLines();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }

}
