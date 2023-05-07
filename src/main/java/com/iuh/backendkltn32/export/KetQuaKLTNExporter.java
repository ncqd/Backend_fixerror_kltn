package com.iuh.backendkltn32.export;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.PhieuChamMau;
import com.iuh.backendkltn32.entity.TieuChiChamDiem;
import com.iuh.backendkltn32.service.PhieuChamMauService;
import com.iuh.backendkltn32.service.TieuChiChamDiemService;

public class KetQuaKLTNExporter {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private PhieuChamMauService phieuChamMauService;
	private TieuChiChamDiemService tieuChiChamDiemService;
	private HocKy hocKy;
	private int count = 7;

	public KetQuaKLTNExporter(PhieuChamMauService phieuChamMauService, TieuChiChamDiemService tieuChiChamDiemService,
			HocKy hocKy) {
		super();
		this.workbook = new XSSFWorkbook();
		this.phieuChamMauService = phieuChamMauService;
		this.tieuChiChamDiemService = tieuChiChamDiemService;
		this.hocKy = hocKy;
	}

	private void writeHeaderLine() throws Exception {
		sheet = workbook.createSheet("DS_Nhom_KLTN");

		Row row = sheet.createRow(0);

		CellStyle styleHeader = workbook.createCellStyle();
		CellStyle styleSheetName = workbook.createCellStyle();
		CellStyle styleContent = workbook.createCellStyle();

		styleContent.setWrapText(true);

		XSSFFont font = workbook.createFont();
		XSSFFont fontName = workbook.createFont();
		XSSFFont fontContent = workbook.createFont();

		fontName.setBold(true);
		fontName.setFontHeightInPoints((short) 18);

		styleSheetName.setFont(fontName);
		styleSheetName.setAlignment(HorizontalAlignment.CENTER);

		fontContent.setFontHeightInPoints((short) 12);
		styleContent.setFont(fontContent);

		List<TieuChiChamDiem> tieuChiChamDiemHD = phatSinhTieuChiTuaDe(row, "HD", styleContent);
		List<TieuChiChamDiem> tieuChiChamDiemPB1 = phatSinhTieuChiTuaDe(row, "PB", styleContent);
		List<TieuChiChamDiem> tieuChiChamDiemPB2 = phatSinhTieuChiTuaDe(row, "PB", styleContent);
		List<TieuChiChamDiem> tieuChiChamDiemHoiDong1 = phatSinhTieuChiTuaDe(row, "CT", styleContent);
		List<TieuChiChamDiem> tieuChiChamDiemHoiDong2 = phatSinhTieuChiTuaDe(row, "TK", styleContent);

		row = sheet.createRow(1);
		createCell(row, 0, "DANH SÁCH NHÓM THỰC HIỆN - ĐỀ TÀI - GIÁO VIÊN HƯỚNG DẪN KLTN HK1 2022-2023",
				styleSheetName);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));
		count = 7;
		phatSinhDiemTieuChi(row, tieuChiChamDiemHD, styleContent);
		phatSinhDiemTieuChi(row, tieuChiChamDiemPB1, styleContent);
		phatSinhDiemTieuChi(row, tieuChiChamDiemPB2, styleContent);
		phatSinhDiemTieuChi(row, tieuChiChamDiemHoiDong1, styleContent);
		phatSinhDiemTieuChi(row, tieuChiChamDiemHoiDong2, styleContent);

		row = sheet.createRow(4);
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
		styleHeader.setBorderRight(BorderStyle.MEDIUM);

		font.setFontHeight(15);
		font.setFontHeightInPoints((short) 12);

		styleHeader.setFont(font);
		createCell(row, 0, "STT Nhóm", styleHeader);
		createCell(row, 1, "Mã SV", styleHeader);
		createCell(row, 2, "Họ tên", styleHeader);
		createCell(row, 3, "Email", styleHeader);
		createCell(row, 4, "Mã đề tài", styleHeader);
		createCell(row, 5, "GVHD", styleHeader);
		createCell(row, 6, "Tên Đề Tài", styleHeader);
		createCell(row, 6, "Được ra PB", styleHeader);
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

	private List<TieuChiChamDiem> phatSinhTieuChiTuaDe(Row row, String vaiTro, CellStyle styleContent)
			throws Exception {
		List<TieuChiChamDiem> tieuChiChamDiems = new ArrayList<>();
		List<String> maTieuChiChamDiems = new ArrayList<>();

		for (PhieuChamMau pc : phieuChamMauService.layHet(vaiTro, hocKy.getMaHocKy())) {
			maTieuChiChamDiems = Arrays
					.asList(pc.getTieuChiChamDiems().substring(1, pc.getTieuChiChamDiems().length() - 1).split(",\\s"));
		}

		for (String tc : maTieuChiChamDiems) {

			System.out.println(tc);
			createCell(row, count++, "LO" + tc, styleContent);
			tieuChiChamDiems.add(tieuChiChamDiemService.layTheoMa(tc));
		}
		createCell(row, count++, "10", styleContent);
		return tieuChiChamDiems;
	}
	
	private void phatSinhDiemTieuChi(Row row, List<TieuChiChamDiem> tieuChiChamDiems, CellStyle styleContent)
			throws Exception {


		for (TieuChiChamDiem tc : tieuChiChamDiems) {
			createCell(row, count++, tc.getDiemToiDa(), styleContent);
		}
		createCell(row, count++, "10", styleContent);
	}

	public void export(HttpServletResponse response) throws Exception {
		writeHeaderLine();
//        writeDataLines();

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();

	}
}
