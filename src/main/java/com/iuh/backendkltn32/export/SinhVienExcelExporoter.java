package com.iuh.backendkltn32.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.iuh.backendkltn32.entity.SinhVien;

public class SinhVienExcelExporoter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<SinhVien> dsSinhVien;
     
    public SinhVienExcelExporoter(List<SinhVien> dsSinhVien) {
        this.dsSinhVien = dsSinhVien;
        workbook = new XSSFWorkbook();
    }
 
 
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Users");
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "Mã sinh viên", style);      
        createCell(row, 1, "E-mail", style);       
        createCell(row, 2, "Họ tên", style);    
        createCell(row, 3, "Lớp học", style);
        createCell(row, 4, "Nơi sinh", style);
         
    }
     
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
     
    private void writeDataLines() {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
                 
        for (SinhVien sinhVien : dsSinhVien) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, sinhVien.getMaSinhVien(), style);
            createCell(row, columnCount++, sinhVien.getEmail(), style);
            createCell(row, columnCount++, sinhVien.getTenSinhVien(), style);
            createCell(row, columnCount++, sinhVien.getLopDanhNghia().getMaLopDanhNghia().toString(), style);
            createCell(row, columnCount++, sinhVien.getNoiSinh(), style);
             
        }
    }
     
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }
}