package com.iuh.backendkltn32.export;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.service.NhomService;
import com.iuh.backendkltn32.service.SinhVienService;

public class DanhSachNhomExporter {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	private NhomService nhomService;

	private SinhVienService sinhVienService;

	private HocKy hocKy;

	public DanhSachNhomExporter(HocKy hocKy, NhomService nhomService, SinhVienService sinhVienService) {
		this.workbook = new XSSFWorkbook();
		this.hocKy = hocKy;
		this.nhomService = nhomService;
		this.sinhVienService = sinhVienService;
	}

	private void writeHeaderLine() throws Exception {
		sheet = workbook.createSheet("DS_Nhom_KLTN");
		Long maHocKyTruoc = Long.parseLong(hocKy.getMaHocKy().substring(0,2)) - 1;
		Long maHocKyHT = Long.parseLong(hocKy.getMaHocKy().substring(0,2)) ;

		Row row = sheet.createRow(1);
		row.setHeight((short) 550);

		CellStyle style = workbook.createCellStyle();
		CellStyle styleHeader = workbook.createCellStyle();
		style.setWrapText(true);
		XSSFFont font = workbook.createFont();
		XSSFFont fontHeader = workbook.createFont();

		fontHeader.setBold(true);
		fontHeader.setFontHeightInPoints((short) 18);
		fontHeader.setFontName("Times New Roman");
		styleHeader.setFont(fontHeader);
		styleHeader.setAlignment(HorizontalAlignment.CENTER);

		font.setFontHeight(15);
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Times New Roman");

		createCell(row, 0, "DANH SÁCH NHÓM THỰC HIỆN - ĐỀ TÀI - GIÁO VIÊN HƯỚNG DẪN KLTN HK1 20"+ maHocKyTruoc + "-20" + maHocKyHT, styleHeader);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));

		row = sheet.createRow(5);
		row.setHeight((short) 550);

		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setBorderRight(BorderStyle.MEDIUM);
		style.setBorderBottom(BorderStyle.MEDIUM);
		style.setBorderTop(BorderStyle.MEDIUM);
		style.setBorderLeft(BorderStyle.MEDIUM);

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
		createCell(row, 9, "Tên Đề Tài", style);
	}

	private void writeDataLines() throws RuntimeException {
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

		for (Nhom nhom : nhomService.layTatCaNhomTheoTinhTrang(hocKy.getMaHocKy(), hocKy.getSoHocKy(), 1)) {
			Row row = sheet.createRow(rowCount++);
			row.setHeight((short) 550);
			int columnCount = 0;
			createCell(row, columnCount++, nhom.getMaNhom(), style);
			List<String> mas = sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom());
			for (String ma : mas) {
				SinhVien sv = sinhVienService.layTheoMa(ma);
				createCell(row, columnCount++, sv.getMaSinhVien(), style);
				createCell(row, columnCount++, sv.getTenSinhVien(), style);
				createCell(row, columnCount++, sv.getEmail(), style);
			}
			if (mas.size() <= 1) {
				createCell(row, columnCount++, "", style);
				createCell(row, columnCount++, "", style);
				createCell(row, columnCount++, "", style);
			}
			
			createCell(row, columnCount++, nhom.getDeTai().getMaDeTai(), style);
			createCell(row, columnCount++, nhom.getDeTai().getGiangVien().getTenGiangVien(), style);
			createCell(row, columnCount++, nhom.getDeTai().getTenDeTai(), style);
		}
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

	public void export(HttpServletResponse response) throws Exception {
		writeHeaderLine();
        writeDataLines();

		workbook.write(response.getOutputStream());
		workbook.close();


	}

}
