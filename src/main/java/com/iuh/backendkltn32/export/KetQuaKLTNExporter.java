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
import org.apache.poi.ss.usermodel.HorizontalAlignment;
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
	private int count = 8;

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
		sheet.setDefaultRowHeight((short) 500);

		Row row = sheet.createRow(0);

		CellStyle styleHeader = workbook.createCellStyle();
		CellStyle styleSheetName = workbook.createCellStyle();
		CellStyle styleContent = workbook.createCellStyle();

		styleContent.setWrapText(true);
		styleHeader.setWrapText(true);
		styleSheetName.setWrapText(true);

		XSSFFont font = workbook.createFont();
		XSSFFont fontName = workbook.createFont();
		XSSFFont fontContent = workbook.createFont();

		fontName.setBold(true);
		fontName.setFontHeightInPoints((short) 18);

		styleSheetName.setFont(fontName);
		styleSheetName.setAlignment(HorizontalAlignment.CENTER);

		styleContent.setAlignment(HorizontalAlignment.CENTER);
		styleContent.setVerticalAlignment(VerticalAlignment.CENTER);
		styleContent.setBorderRight(BorderStyle.MEDIUM);
		styleContent.setBorderBottom(BorderStyle.MEDIUM);
		styleContent.setBorderLeft(BorderStyle.MEDIUM);
		styleContent.setBorderTop(BorderStyle.MEDIUM);
		fontContent.setFontHeightInPoints((short) 12);
		styleContent.setFont(fontContent);

		List<TieuChiChamDiem> tieuChiChamDiemHD = phatSinhTieuChiTuaDe(row, "HD", styleContent, false);
		List<TieuChiChamDiem> tieuChiChamDiemPB1 = phatSinhTieuChiTuaDe(row, "PB", styleContent, false);
		List<TieuChiChamDiem> tieuChiChamDiemPB2 = phatSinhTieuChiTuaDe(row, "PB", styleContent, false);
		List<TieuChiChamDiem> tieuChiChamDiemHoiDong1 = phatSinhTieuChiTuaDe(row, "CT", styleContent, false);
		List<TieuChiChamDiem> tieuChiChamDiemHoiDong2 = phatSinhTieuChiTuaDe(row, "TK", styleContent, false);

		row = sheet.createRow(1);
		createCell(row, 0, "DANH SÁCH NHÓM THỰC HIỆN - ĐỀ TÀI - GIÁO VIÊN HƯỚNG DẪN KLTN HK1 2022-2023",
				styleSheetName);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
		
		count = 8;
		phatSinhDiemTieuChi(row, tieuChiChamDiemHD, styleContent);
		phatSinhDiemTieuChi(row, tieuChiChamDiemPB1, styleContent);
		phatSinhDiemTieuChi(row, tieuChiChamDiemPB2, styleContent);
		phatSinhDiemTieuChi(row, tieuChiChamDiemHoiDong1, styleContent);
		phatSinhDiemTieuChi(row, tieuChiChamDiemHoiDong2, styleContent);

		row = sheet.createRow(2);
		createCellTieuDe(row, 8, "GVHD", styleContent);
		createCellTieuDe(row, 9, "", styleContent);
		createCellTieuDe(row, 10, "", styleContent);
		createCellTieuDe(row, 11, "", styleContent);
		createCellTieuDe(row, 12, "", styleContent);
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 8, count = 8 + tieuChiChamDiemHD.size()));
		createCellTieuDe(row, count + 1, "GVPB1", styleContent);
		createCellTieuDe(row, count + 2, "", styleContent);
		createCellTieuDe(row, count + 3, "", styleContent);
		createCellTieuDe(row, count + 4, "", styleContent);
		createCellTieuDe(row, count + 5, "", styleContent);
		sheet.addMergedRegion(new CellRangeAddress(2, 2, count + 1, count = count + tieuChiChamDiemPB1.size()+1));
		createCellTieuDe(row, count + 1, "GVPB2", styleContent);
		createCellTieuDe(row, count + 2, "", styleContent);
		createCellTieuDe(row, count + 3, "", styleContent);
		createCellTieuDe(row, count + 4, "", styleContent);
		createCellTieuDe(row, count + 5, "", styleContent);
		sheet.addMergedRegion(new CellRangeAddress(2, 2, count + 1, count = count + tieuChiChamDiemPB2.size()+1));
		createCellTieuDe(row, count + 1, "GVHD1", styleContent);
		createCellTieuDe(row, count + 2, "", styleContent);
		createCellTieuDe(row, count + 3, "", styleContent);
		createCellTieuDe(row, count + 4, "", styleContent);
		createCellTieuDe(row, count + 5, "", styleContent);
		sheet.addMergedRegion(new CellRangeAddress(2, 2, count + 1, count = count + tieuChiChamDiemHoiDong1.size()+1));
		createCellTieuDe(row, count + 1, "GVHD2", styleContent);
		createCellTieuDe(row, count + 2, "", styleContent);
		createCellTieuDe(row, count + 3, "", styleContent);
		createCellTieuDe(row, count + 4, "", styleContent);
		createCellTieuDe(row, count + 5, "", styleContent);
		sheet.addMergedRegion(new CellRangeAddress(2, 2, count + 1, count = count + tieuChiChamDiemHoiDong2.size()+1));
		createCellTieuDe(row, count + 1, "DOANH NGHIỆP", styleContent);
		createCellTieuDe(row, count + 2, "", styleContent);
		createCellTieuDe(row, count + 3, "", styleContent);
		createCellTieuDe(row, count + 4, "", styleContent);
		createCellTieuDe(row, count + 5, "", styleContent);
		sheet.addMergedRegion(new CellRangeAddress(2, 2, count + 1, count = count + tieuChiChamDiemHD.size()+1));

		row = sheet.createRow(3);
		createCellTieuDe(row, 8, "TIÊU CHÍ", styleContent);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 8, count = 8 + tieuChiChamDiemHD.size()));
		createCellTieuDe(row, count + 1, "TIÊU CHÍ", styleContent);
		createCellTieuDe(row, count + 2, "", styleContent);
		createCellTieuDe(row, count + 3, "", styleContent);
		createCellTieuDe(row, count + 4, "", styleContent);
		createCellTieuDe(row, count + 5, "", styleContent);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, count + 1, count = count + tieuChiChamDiemPB1.size()+1));
		createCellTieuDe(row, count + 1, "TIÊU CHÍ", styleContent);
		createCellTieuDe(row, count + 2, "", styleContent);
		createCellTieuDe(row, count + 3, "", styleContent);
		createCellTieuDe(row, count + 4, "", styleContent);
		createCellTieuDe(row, count + 5, "", styleContent);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, count + 1, count = count + tieuChiChamDiemPB2.size()+1));
		createCellTieuDe(row, count + 1, "TIÊU CHÍ", styleContent);
		createCellTieuDe(row, count + 2, "", styleContent);
		createCellTieuDe(row, count + 3, "", styleContent);
		createCellTieuDe(row, count + 4, "", styleContent);
		createCellTieuDe(row, count + 5, "", styleContent);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, count + 1, count = count + tieuChiChamDiemHoiDong1.size()+1));
		createCellTieuDe(row, count + 1, "TIÊU CHÍ", styleContent);
		createCellTieuDe(row, count + 2, "", styleContent);
		createCellTieuDe(row, count + 3, "", styleContent);
		createCellTieuDe(row, count + 4, "", styleContent);
		createCellTieuDe(row, count + 5, "", styleContent);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, count + 1, count = count + tieuChiChamDiemHoiDong2.size()+1));
		createCellTieuDe(row, count + 1, "TIÊU CHÍ", styleContent);
		createCellTieuDe(row, count + 2, "", styleContent);
		createCellTieuDe(row, count + 3, "", styleContent);
		createCellTieuDe(row, count + 4, "", styleContent);
		createCellTieuDe(row, count + 5, "", styleContent);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, count + 1, count = count + tieuChiChamDiemHD.size()+1));

		row = sheet.createRow(4);
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
		styleHeader.setBorderRight(BorderStyle.MEDIUM);
		styleHeader.setBorderBottom(BorderStyle.MEDIUM);
		styleHeader.setBorderLeft(BorderStyle.MEDIUM);
		styleHeader.setBorderTop(BorderStyle.MEDIUM);

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
		createCell(row, 7, "Được ra PB", styleHeader);
		
		count = 8;
		phatSinhTieuChiTuaDe(row, "HD", styleContent, true);
		phatSinhTieuChiTuaDe(row, "PB", styleContent, true);
		phatSinhTieuChiTuaDe(row, "PB", styleContent, true);
		phatSinhTieuChiTuaDe(row, "CT", styleContent, true);
		phatSinhTieuChiTuaDe(row, "TK", styleContent, true);
		phatSinhTieuChiTuaDe(row, "CT", styleContent, true);
		createCellTieuDe(row, count++, "GVHD", styleHeader);
		createCellTieuDe(row, count++, "PB1", styleHeader);
		createCellTieuDe(row, count++, "PB2", styleHeader);
		createCellTieuDe(row, count++, "TBBM", styleHeader);
		createCellTieuDe(row, count++, "TVHD1", styleHeader);
		createCellTieuDe(row, count++, "TVHD2", styleHeader);
		createCellTieuDe(row, count++, "TBTVHD", styleHeader);
		createCellTieuDe(row, count++, "Kết quả", styleHeader);
		createCellTieuDe(row, count++, "Điểm Báo", styleHeader);
		createCellTieuDe(row, count++, "Điểm TK", styleHeader);
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
	
	private void createCellTieuDe(Row row, int columnCount, Object value, CellStyle style) {
		Cell cell = row.createCell(columnCount);
		sheet.autoSizeColumn(count);
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

	private List<TieuChiChamDiem> phatSinhTieuChiTuaDe(Row row, String vaiTro, CellStyle styleContent, boolean stt)
			throws Exception {
		List<TieuChiChamDiem> tieuChiChamDiems = new ArrayList<>();
		List<String> maTieuChiChamDiems = new ArrayList<>();

		for (PhieuChamMau pc : phieuChamMauService.layHet(vaiTro, hocKy.getMaHocKy())) {
			maTieuChiChamDiems = Arrays
					.asList(pc.getTieuChiChamDiems().substring(1, pc.getTieuChiChamDiems().length() - 1).split(",\\s"));
		}

		for (String tc : maTieuChiChamDiems) {
			createCellTieuDe(row, count++, stt ? tc : "LO" + tc, styleContent);
			tieuChiChamDiems.add(tieuChiChamDiemService.layTheoMa(tc));
		}
		createCellTieuDe(row, count++, "    ", styleContent);
		return tieuChiChamDiems;
	}

	private void phatSinhDiemTieuChi(Row row, List<TieuChiChamDiem> tieuChiChamDiems, CellStyle styleContent)
			throws Exception {

		for (TieuChiChamDiem tc : tieuChiChamDiems) {
			createCellTieuDe(row, count++, tc.getDiemToiDa(), styleContent);
		}
		createCellTieuDe(row, count++, "10", styleContent);
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
