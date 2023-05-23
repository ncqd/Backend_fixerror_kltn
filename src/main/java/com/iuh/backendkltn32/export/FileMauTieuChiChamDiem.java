package com.iuh.backendkltn32.export;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileMauTieuChiChamDiem {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	public FileMauTieuChiChamDiem() {
		workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() throws Exception {
		sheet = workbook.createSheet("MauDeTai");

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
		createCell(row, 0, "STT", style);
		createCell(row, 1, "Tên Chuẩn Đầu Ra", style);
		createCell(row, 2, "Điểm Tối Đa", style);

	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof StringBuffer) {
			cell.setCellValue(new String((StringBuffer) value));
		} else if (value instanceof Double) {
			cell.setCellValue((Double) value);
		} else if (value instanceof Long) {
			cell.setCellValue((Long) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void writeDataLines() {
		int rowCount = 1;

		CellStyle styleContent = workbook.createCellStyle();
		XSSFFont fontContent = workbook.createFont();

		fontContent.setFontHeightInPoints((short) 11);
		fontContent.setFontName("Times New Roman");
		styleContent.setFont(fontContent);
		styleContent.setAlignment(HorizontalAlignment.CENTER);
		styleContent.setVerticalAlignment(VerticalAlignment.CENTER);

		Row row = sheet.createRow(rowCount++);
		int columnCount = 0;

		createCell(row, columnCount++, "1", styleContent);
		createCell(row, columnCount++, "Làm việc nhóm", styleContent);
		createCell(row, columnCount++, "0.5", styleContent);

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
