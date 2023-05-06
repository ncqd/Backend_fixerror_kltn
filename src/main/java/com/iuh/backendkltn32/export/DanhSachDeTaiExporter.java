package com.iuh.backendkltn32.export;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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

import com.iuh.backendkltn32.entity.DeTai;
import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.service.DeTaiService;
import com.iuh.backendkltn32.service.GiangVienService;
import com.iuh.backendkltn32.service.NhomService;

public class DanhSachDeTaiExporter {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private DeTaiService deTaiService;
	private String maHocKy;

	public DanhSachDeTaiExporter(DeTaiService deTaiService, String maHocKy) {
		super();
		this.workbook = new XSSFWorkbook();
		this.deTaiService = deTaiService;
		this.maHocKy = maHocKy;
	}

	private void writeHeaderLine() throws IOException {
		sheet = workbook.createSheet("DanhSach_DeTai_KLTN");

		Row row = sheet.createRow(1);
		sheet.setColumnWidth(2, 50);

		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		XSSFFont font = workbook.createFont();

		row = sheet.createRow(0);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setBorderRight(BorderStyle.MEDIUM);
		font.setFontHeight(15);
		font.setFontHeightInPoints((short) 12);
		style.setFont(font);
		createCell(row, 0, "Mã đề tài", style);
		createCell(row, 1, "GVHD", style);
		createCell(row, 2, "Tên đề tài", style);
		createCell(row, 3, "MỤC TIÊU ĐỀ TÀI", style);
		createCell(row, 4, "DỰ KIẾN SẢN PHẨM NGHIÊN CỨU CỦA ĐỀ TÀI VÀ KHẢ NĂNG ỨNG DỤNG", style);
		createCell(row, 5, "Mô tả", style);
		createCell(row, 6, "Yêu cầu đầu vào", style);
		createCell(row, 7, "Yêu cầu đầu ra (Output Standards)", style);
	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		Cell cell = row.createCell(columnCount);
		sheet.setColumnWidth(columnCount, 20 * 256);

		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Double) {
			cell.setCellValue((Double) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof Long) {
			cell.setCellValue((Long) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void writeDataLines() throws Exception {
		int rowCount = 6;

		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		XSSFFont font = workbook.createFont();

		font.setFontHeight(15);
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Times New Roman");

		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setBorderRight(BorderStyle.MEDIUM);
		style.setBorderBottom(BorderStyle.MEDIUM);
		style.setBorderTop(BorderStyle.MEDIUM);
		style.setBorderLeft(BorderStyle.MEDIUM);

		style.setFont(font);

		for (DeTai deTai : deTaiService.layDsDeTaiXuaExcel(maHocKy)) {
			Row row = sheet.createRow(rowCount++);
			row.setHeight((short) 550);
			int columnCount = 0;
			createCell(row, columnCount++, deTai.getMaDeTai(), style);
			createCell(row, columnCount++, deTai.getGiangVien().getTenGiangVien(), style);
			createCell(row, columnCount++, deTai.getMucTieuDeTai(), style);
			createCell(row, columnCount++, deTai.getSanPhamDuKien(), style);
			createCell(row, columnCount++, deTai.getMoTa(), style);
			createCell(row, columnCount++, deTai.getYeuCauDauVao(), style);
			createCell(row, columnCount++, "A.Sinh viên tham gia đề tài\r\n"
					+ "1) Nắm vững kiến thức, kỹ năng lập trình Java\r\n"
					+ "2) Có kiến thức, kỹ năng triển khai dự án phần mềm đầy đủ qui trình từ Requirement => Design => Coding => Unit Testing \n"
					+ "3) Có năng lực đầy đủ về Lập kế hoạch, theo dõi tiến độ, phân tích các vấn đề phát sinh trong quá trình thực hiện dự án. \n"
					+ "4) Có kiến thức về AI \n"
					+ "\n"
					+ "B.Sản phẩm \n"
					+ "1) Có tài liệu mô tả Yêu cầu dự án.\r\n"
					+ "2) Có tài liệu mô tả Thiết kế kiến trúc của dự án.\r\n"
					+ "3) Có tài liệu mô tả Thiết kế chi tiết cho chức năng chính của dự án.\r\n"
					+ "4) Có mã nguồn được lưu vết (tracking) định kỳ trên hệ thống quản lý phiên bản (version control) như Subversion hoặc Git; Mã nguồn trình bày rõ ràng, dễ hiểu theo tiêu chuẩn đã được thống nhất.\r\n"
					+ "5) Có tài liệu mô tả tình huống test và kết quả test cho chức năng chính của dự án.\r\n"
					+ "6) Có tài liệu mô tả cách biên dịch, đóng gói và hướng dẫn nâng cấp mã nguồn cho dự án.", style);
		}
	}

	public void export(HttpServletResponse response) throws Exception {
		writeHeaderLine();
        writeDataLines();

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();

	}

}
