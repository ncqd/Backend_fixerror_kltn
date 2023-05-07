package com.iuh.backendkltn32.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.SinhVien;

public class DanhSachNhomKLTNChamDiemExporter {

	private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    
    
    
    public DanhSachNhomKLTNChamDiemExporter() {
		super();
		this.workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() throws IOException {
        sheet = workbook.createSheet("DS_Nhom_KLTN_RA_HD_POSTER");
         
        Row row = sheet.createRow(1);
        sheet.setColumnWidth(2, 50);
        
        CellStyle style = workbook.createCellStyle();
        CellStyle styleName = workbook.createCellStyle();
        style.setWrapText(true);
        XSSFFont font = workbook.createFont();
        XSSFFont fontName = workbook.createFont();
       
        fontName.setBold(true);
        fontName.setFontHeightInPoints((short) 18);
        styleName.setFont(fontName);
        styleName.setAlignment(HorizontalAlignment.CENTER);
        
        
        createCell(row, 1, "NGÀNH KỸ THUẬT PHẦN MỀM \n"
        		+ "Thời gian: 8g - ngày 08/12/2022 - địa điểm theo thông báo của khoa", styleName);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 17));
        
        row = sheet.createRow(2);
        createCell(row, 0, "Lưu ý: \n"
        		+ "(1) Thời gian nộp quyển báo cáo (bản cứng & mềm): khoảng sau 1 tuần ngày 8/12/2022 - Ngày giờ địa điểm sẽ thông báo trên group KLTN \n"
        		+ "(2) Bản cứng: In bìa xanh theo mẫu của khoa + đĩa CD chứa bản mềm, hướng dẫn và sản phẩm, và có đầy đủ chữ ký của GVHD, 2 thành viên trong hội đồng báo cáo --> trong buổi báo cáo các bạn in rời các tờ giấy nhận xét và nhờ các GV nhận xét và ký tên.", styleName);
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 17));
        
        row = sheet.createRow(3);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderRight(BorderStyle.MEDIUM);
        font.setFontHeight(15);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);

        createCell(row, 0, "STT Nhóm", style);    
        createCell(row, 1, "Mã SV 1", style);      
        createCell(row, 2, "Họ tên SV 1", style);       
        createCell(row, 3, "Email SV1", style);    
        createCell(row, 4, "Mã SV 2", style);
        createCell(row, 5, "Họ tên SV2", style);
        createCell(row, 6, "Email SV 2", style);
        createCell(row, 7, "Mã đề tài", style);
        createCell(row, 8, "GVHD", style);
        createCell(row, 9, "Tên đề tài SV nhận từ bộ môn", style);
        createCell(row, 10, "Đồng ý cho SV ra phản biện ngày 2/12/2022", style);
        createCell(row, 11, "Ghi chú", style);
        createCell(row, 12, "GVPB1", style);
        createCell(row, 13, "GVPB2", style);
        createCell(row, 14, "Hình thức báo cáo", style);
        createCell(row, 15, "Hội đồng", style);
        createCell(row, 16, "TV_HD1", style);
        createCell(row, 17, "TV_HD2", style);
        createCell(row, 18, "ThuKy", style);
    }
    
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        sheet.setColumnWidth(columnCount, 20 * 256);
        sheet.setDefaultRowHeightInPoints( 30);
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
        }
        cell.setCellStyle(style);
   }
    
//    private void writeDataLines() throws Exception {
//		int rowCount = 6;
//
//		CellStyle style = workbook.createCellStyle();
//		style.setWrapText(true);
//		XSSFFont font = workbook.createFont();
//
//		font.setFontHeight(15);
//		font.setFontHeightInPoints((short) 12);
//		font.setFontName("Times New Roman");
//
//		style.setAlignment(HorizontalAlignment.CENTER);
//		style.setVerticalAlignment(VerticalAlignment.CENTER);
//		style.setBorderRight(BorderStyle.MEDIUM);
//		style.setBorderBottom(BorderStyle.MEDIUM);
//		style.setBorderTop(BorderStyle.MEDIUM);
//		style.setBorderLeft(BorderStyle.MEDIUM);
//
//		style.setFont(font);
//
//		for (Nhom nhom : nhomService.layTatCaNhomTheoTinhTrang(hocKy.getMaHocKy(), hocKy.getSoHocKy(), 1)) {
//			Row row = sheet.createRow(rowCount++);
//			row.setHeight((short) 550);
//			int columnCount = 0;
//			createCell(row, columnCount++, nhom.getMaNhom(), style);
//			List<String> mas = sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom());
//			for (String ma : mas) {
//				SinhVien sv = sinhVienService.layTheoMa(ma);
//				createCell(row, columnCount++, sv.getMaSinhVien(), style);
//				createCell(row, columnCount++, sv.getTenSinhVien(), style);
//				createCell(row, columnCount++, sv.getEmail(), style);
//			}
//			if (mas.size() <= 1) {
//				createCell(row, columnCount++, "", style);
//				createCell(row, columnCount++, "", style);
//				createCell(row, columnCount++, "", style);
//			}
//			
//			createCell(row, columnCount++, nhom.getDeTai().getMaDeTai(), style);
//			createCell(row, columnCount++, nhom.getDeTai().getGiangVien().getTenGiangVien(), style);
//			createCell(row, columnCount++, nhom.getDeTai().getTenDeTai(), style);
//		}
//	}
    
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
//        writeDataLines();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }
}
