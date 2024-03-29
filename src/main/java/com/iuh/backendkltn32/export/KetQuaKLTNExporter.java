package com.iuh.backendkltn32.export;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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

import com.iuh.backendkltn32.entity.DiemThanhPhan;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.PhieuCham;
import com.iuh.backendkltn32.entity.PhieuChamMau;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.entity.TieuChiChamDiem;
import com.iuh.backendkltn32.service.NhomService;
import com.iuh.backendkltn32.service.PhieuChamMauService;
import com.iuh.backendkltn32.service.PhieuChamService;
import com.iuh.backendkltn32.service.SinhVienService;
import com.iuh.backendkltn32.service.TieuChiChamDiemService;

public class KetQuaKLTNExporter {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private PhieuChamMauService phieuChamMauService;
	private TieuChiChamDiemService tieuChiChamDiemService;
	private PhieuChamService phieuChamService;
	private NhomService nhomService;
	private SinhVienService sinhVienService;
	private HocKy hocKy;
	private int count = 8;

	public KetQuaKLTNExporter(PhieuChamMauService phieuChamMauService, TieuChiChamDiemService tieuChiChamDiemService,
			HocKy hocKy, PhieuChamService phieuChamService, NhomService nhomService, SinhVienService sinhVienService) {
		super();
		this.workbook = new XSSFWorkbook();
		this.phieuChamMauService = phieuChamMauService;
		this.tieuChiChamDiemService = tieuChiChamDiemService;
		this.hocKy = hocKy;
		this.phieuChamService = phieuChamService;
		this.nhomService = nhomService;
		this.sinhVienService = sinhVienService;
	}

	private void writeHeaderLine() throws RuntimeException {
		sheet = workbook.createSheet("KetQua_Nhom_KLTN");
		sheet.setDefaultRowHeight((short) 600);

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
		fontName.setFontHeightInPoints((short) 16);

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
		List<TieuChiChamDiem> tieuChiChamDiemHoiDong3 = phatSinhTieuChiTuaDe(row, "TV3", styleContent, false);

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
		phatSinhDiemTieuChi(row, tieuChiChamDiemHoiDong3, styleContent);

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
		sheet.addMergedRegion(new CellRangeAddress(2, 2, count + 1, count = count + tieuChiChamDiemPB1.size() + 1));
		createCellTieuDe(row, count + 1, "GVPB2", styleContent);
		createCellTieuDe(row, count + 2, "", styleContent);
		createCellTieuDe(row, count + 3, "", styleContent);
		createCellTieuDe(row, count + 4, "", styleContent);
		createCellTieuDe(row, count + 5, "", styleContent);
		sheet.addMergedRegion(new CellRangeAddress(2, 2, count + 1, count = count + tieuChiChamDiemPB2.size() + 1));
		createCellTieuDe(row, count + 1, "GVHD1", styleContent);
		createCellTieuDe(row, count + 2, "", styleContent);
		createCellTieuDe(row, count + 3, "", styleContent);
		createCellTieuDe(row, count + 4, "", styleContent);
		createCellTieuDe(row, count + 5, "", styleContent);
		sheet.addMergedRegion(
				new CellRangeAddress(2, 2, count + 1, count = count + tieuChiChamDiemHoiDong1.size() + 1));
		createCellTieuDe(row, count + 1, "GVHD2", styleContent);
		createCellTieuDe(row, count + 2, "", styleContent);
		createCellTieuDe(row, count + 3, "", styleContent);
		createCellTieuDe(row, count + 4, "", styleContent);
		createCellTieuDe(row, count + 5, "", styleContent);
		sheet.addMergedRegion(
				new CellRangeAddress(2, 2, count + 1, count = count + tieuChiChamDiemHoiDong3.size() + 1));
		createCellTieuDe(row, count + 1, "TVHD", styleContent);
		createCellTieuDe(row, count + 2, "", styleContent);
		createCellTieuDe(row, count + 3, "", styleContent);
		createCellTieuDe(row, count + 4, "", styleContent);
		createCellTieuDe(row, count + 5, "", styleContent);
		sheet.addMergedRegion(new CellRangeAddress(2, 2, count + 1, count = count + tieuChiChamDiemHoiDong3.size() + 1));

		row = sheet.createRow(3);
		createCellTieuDe(row, 8, "TIÊU CHÍ", styleContent);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 8, count = 8 + tieuChiChamDiemHD.size()));
		createCellTieuDe(row, count + 1, "TIÊU CHÍ", styleContent);
		createCellTieuDe(row, count + 2, "", styleContent);
		createCellTieuDe(row, count + 3, "", styleContent);
		createCellTieuDe(row, count + 4, "", styleContent);
		createCellTieuDe(row, count + 5, "", styleContent);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, count + 1, count = count + tieuChiChamDiemPB1.size() + 1));
		createCellTieuDe(row, count + 1, "TIÊU CHÍ", styleContent);
		createCellTieuDe(row, count + 2, "", styleContent);
		createCellTieuDe(row, count + 3, "", styleContent);
		createCellTieuDe(row, count + 4, "", styleContent);
		createCellTieuDe(row, count + 5, "", styleContent);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, count + 1, count = count + tieuChiChamDiemPB2.size() + 1));
		createCellTieuDe(row, count + 1, "TIÊU CHÍ", styleContent);
		createCellTieuDe(row, count + 2, "", styleContent);
		createCellTieuDe(row, count + 3, "", styleContent);
		createCellTieuDe(row, count + 4, "", styleContent);
		createCellTieuDe(row, count + 5, "", styleContent);
		sheet.addMergedRegion(
				new CellRangeAddress(3, 3, count + 1, count = count + tieuChiChamDiemHoiDong1.size() + 1));
		createCellTieuDe(row, count + 1, "TIÊU CHÍ", styleContent);
		createCellTieuDe(row, count + 2, "", styleContent);
		createCellTieuDe(row, count + 3, "", styleContent);
		createCellTieuDe(row, count + 4, "", styleContent);
		createCellTieuDe(row, count + 5, "", styleContent);
		sheet.addMergedRegion(
				new CellRangeAddress(3, 3, count + 1, count = count + tieuChiChamDiemHoiDong2.size() + 1));
		createCellTieuDe(row, count + 1, "TIÊU CHÍ", styleContent);
		createCellTieuDe(row, count + 2, "", styleContent);
		createCellTieuDe(row, count + 3, "", styleContent);
		createCellTieuDe(row, count + 4, "", styleContent);
		createCellTieuDe(row, count + 5, "", styleContent);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, count + 1, count = count + tieuChiChamDiemHoiDong3.size() + 1));

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
		createCellTieuDe(row, count++, "HD", styleHeader);
		createCellTieuDe(row, count++, "PB1", styleHeader);
		createCellTieuDe(row, count++, "PB2", styleHeader);
		createCellTieuDe(row, count++, "TBBM", styleHeader);
		createCellTieuDe(row, count++, "TVHD1", styleHeader);
		createCellTieuDe(row, count++, "TVHD2", styleHeader);
		createCellTieuDe(row, count++, "TVHD3", styleHeader);
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
			throws RuntimeException {
		List<TieuChiChamDiem> tieuChiChamDiems = new ArrayList<>();
		List<String> maTieuChiChamDiems = new ArrayList<>();
		

		PhieuChamMau pc = phieuChamMauService.layHet(vaiTro, hocKy.getMaHocKy()).get(0) ;
			maTieuChiChamDiems = Arrays
					.asList(pc.getTieuChiChamDiems().substring(1, pc.getTieuChiChamDiems().length() - 1).split(",\\s"));
		

		for (String tc : maTieuChiChamDiems) {
			createCellTieuDe(row, count++, stt ? tc : "LO" + tc, styleContent);
			tieuChiChamDiems.add(tieuChiChamDiemService.layTheoMa(tc));
		}
		createCellTieuDe(row, count++, stt ? "KQ" : "    ", styleContent);
		return tieuChiChamDiems;
	}

	private void phatSinhDiemTieuChi(Row row, List<TieuChiChamDiem> tieuChiChamDiems, CellStyle styleContent)
			throws RuntimeException {

		for (TieuChiChamDiem tc : tieuChiChamDiems) {
			createCellTieuDe(row, count++, tc.getDiemToiDa(), styleContent);
		}
		createCellTieuDe(row, count++, "10", styleContent);
	}

	private void writeDataLines() throws RuntimeException {
		int rowCount = 5;

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
			if (phieuChamService.layMaPhieuPhieuTheoMaSinhVienTenVaiTro(hocKy.getMaHocKy(), "CT", nhom.getMaNhom())
					.size() > 0) {
				Row row = sheet.createRow(rowCount++);
				row.setHeight((short) 550);
				int columnCount = 0;
				if (rowCount % 2 == 0) {
					int rowAfter = rowCount - 1;
					createCell(row, columnCount, nhom.getMaNhom(), style);
					sheet.addMergedRegion(new CellRangeAddress(rowAfter, rowCount, columnCount, columnCount));
					Row rowNext = sheet.createRow(rowCount);
					rowCount++;
					writeContentCell(nhom, columnCount, style, row, rowNext);
				}
			}
		}
	}

	private void writeContentCell(Nhom nhom, int columnCount, CellStyle style, Row row, Row rowNext) throws RuntimeException {
		List<String> mas = sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom());
		int nextColum = columnCount + 1;
		SinhVien sv = sinhVienService.layTheoMa(mas.get(0));
		PhieuCham phieuChamHD1SV1 = phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(0), "HD").get(0);
		if (phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(0), "CT").size() <= 0) {
			return;
		}
		if (mas.size() > 2) {
			if (phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(1), "CT").size() <= 0) {
				return;
			}
		}
		if (phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(0), "TV3").size() > 0) {
			PhieuCham phieuChamHoiDong1SV1 = phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(0), "CT").get(0);
			PhieuCham phieuChamHoiDong2SV1 = phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(0), "TK").get(0);
			PhieuCham phieuChamHoiDong3SV1 = phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(0), "TV3").get(0);
			createCell(row, nextColum++, mas.get(0), style);
			createCell(row, nextColum++, sv.getTenSinhVien(), style);
			createCell(row, nextColum++, sv.getEmail(), style);
			createCell(row, nextColum++, nhom.getDeTai().getMaDeTai(), style);
			createCell(row, nextColum++, nhom.getDeTai().getGiangVien().getTenGiangVien(), style);
			createCell(row, nextColum++, nhom.getDeTai().getTenDeTai(), style);
			createCell(row, nextColum++, phieuChamHD1SV1.getDiemPhieuCham() >= 5 ? "Yes" : "No", style);

			for (DiemThanhPhan dtp : phieuChamHD1SV1.getDsDiemThanhPhan()) {
				createCellTieuDe(row, nextColum++, dtp.getDiemThanhPhan(), style);
			}
			createCellTieuDe(row, nextColum++, phieuChamHD1SV1.getDiemPhieuCham(), style);

			for (PhieuCham pc : phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(0), "PB")) {
				for (DiemThanhPhan dtp : pc.getDsDiemThanhPhan()) {
					createCellTieuDe(row, nextColum++, dtp.getDiemThanhPhan(), style);
				}
				createCellTieuDe(row, nextColum++, pc.getDiemPhieuCham(), style);
			}
			for (DiemThanhPhan dtp : phieuChamHoiDong1SV1.getDsDiemThanhPhan()) {
				createCellTieuDe(row, nextColum++, dtp.getDiemThanhPhan(), style);
			}
			createCellTieuDe(row, nextColum++, phieuChamHoiDong1SV1.getDiemPhieuCham(), style);

			for (DiemThanhPhan dtp : phieuChamHoiDong2SV1.getDsDiemThanhPhan()) {
				createCellTieuDe(row, nextColum++, dtp.getDiemThanhPhan(), style);
			}
			createCellTieuDe(row, nextColum++, phieuChamHoiDong2SV1.getDiemPhieuCham(), style);

			for (DiemThanhPhan dtp : phieuChamHoiDong3SV1.getDsDiemThanhPhan()) {
				
				createCellTieuDe(row, nextColum++, dtp.getDiemThanhPhan(), style);
			}
			createCellTieuDe(row, nextColum++, phieuChamHoiDong3SV1.getDiemPhieuCham(), style);
			

			Double diemPB = (double) 0;
			diemPB += phieuChamHD1SV1.getDiemPhieuCham();
			createCellTieuDe(row, nextColum++, phieuChamHD1SV1.getDiemPhieuCham() +"", style);

			for (PhieuCham pc : phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(0), "PB")) {
				createCellTieuDe(row, nextColum++, pc.getDiemPhieuCham(), style);
				diemPB += pc.getDiemPhieuCham();
			}
			diemPB = diemPB / 3;
			createCellTieuDe(row, nextColum++, Math.floor(diemPB * 100) / 100, style);

			Double diemHD = (phieuChamHoiDong1SV1.getDiemPhieuCham() + phieuChamHoiDong2SV1.getDiemPhieuCham() + phieuChamHoiDong3SV1.getDiemPhieuCham()) / 3;
			createCellTieuDe(row, nextColum++, phieuChamHoiDong1SV1.getDiemPhieuCham(), style);
			createCellTieuDe(row, nextColum++, phieuChamHoiDong2SV1.getDiemPhieuCham(), style);
			createCellTieuDe(row, nextColum++, phieuChamHoiDong3SV1.getDiemPhieuCham(), style);
			
			createCellTieuDe(row, nextColum++, diemHD, style);

			Double kq = (diemHD + diemPB) / 2;
			Double diemBao = phieuChamHoiDong1SV1.getDsKetQua().get(0).getDiemBao() != null ? phieuChamHoiDong1SV1.getDsKetQua().get(0).getDiemBao() : 0 ;
			createCellTieuDe(row, nextColum++, Math.floor(kq * 100) / 100, style);
			createCellTieuDe(row, nextColum++, diemBao, style);
			createCellTieuDe(row, nextColum++, Math.floor((kq + diemBao) * 10) / 10, style);

			if (mas.size() > 1) {
				PhieuCham phieuChamHDSV2 = phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(1), "HD").get(0);
				PhieuCham phieuChamHoiDong1SV2 = phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(1), "CT").get(0);
				PhieuCham phieuChamHoiDong2SV2 = phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(1), "TK").get(0);
				PhieuCham phieuChamHoiDong3SV2 = phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(1), "TV3").get(0);
				int nextColum2 = columnCount + 1;
				SinhVien sv2 = sinhVienService.layTheoMa(mas.get(1));
				createCell(rowNext, nextColum2++, mas.get(1), style);
				createCell(rowNext, nextColum2++, sv2.getTenSinhVien(), style);
				createCell(rowNext, nextColum2++, sv2.getEmail(), style);
				createCell(rowNext, nextColum2++, nhom.getDeTai().getMaDeTai(), style);
				createCell(rowNext, nextColum2++, nhom.getDeTai().getGiangVien().getTenGiangVien(), style);
				createCell(rowNext, nextColum2++, nhom.getDeTai().getTenDeTai(), style);
				createCell(rowNext, nextColum2++, phieuChamHDSV2.getDiemPhieuCham() >= 5 ? "Yes" : "No", style);

				for (DiemThanhPhan dtp : phieuChamHDSV2.getDsDiemThanhPhan()) {
					createCellTieuDe(rowNext, nextColum2++, dtp.getDiemThanhPhan(), style);
				}
				createCellTieuDe(rowNext, nextColum2++, phieuChamHDSV2.getDiemPhieuCham()+"", style);

				for (PhieuCham pc : phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(1), "PB")) {
					for (DiemThanhPhan dtp : pc.getDsDiemThanhPhan()) {
						createCellTieuDe(rowNext, nextColum2++, dtp.getDiemThanhPhan(), style);
					}
					createCellTieuDe(rowNext, nextColum2++, pc.getDiemPhieuCham(), style);
				}
				for (DiemThanhPhan dtp : phieuChamHoiDong1SV2.getDsDiemThanhPhan()) {
					createCellTieuDe(rowNext, nextColum2++, dtp.getDiemThanhPhan(), style);
				}
				createCellTieuDe(rowNext, nextColum2++, phieuChamHoiDong1SV2.getDiemPhieuCham(), style);

				for (DiemThanhPhan dtp : phieuChamHoiDong2SV2.getDsDiemThanhPhan()) {
					createCellTieuDe(rowNext, nextColum2++, dtp.getDiemThanhPhan(), style);
				}
				createCellTieuDe(rowNext, nextColum2++, phieuChamHoiDong2SV2.getDiemPhieuCham(), style);
				
				for (DiemThanhPhan dtp : phieuChamHoiDong2SV2.getDsDiemThanhPhan()) {
					createCellTieuDe(rowNext, nextColum2++, dtp.getDiemThanhPhan(), style);
				}
				createCellTieuDe(rowNext, nextColum2++, phieuChamHoiDong3SV2.getDiemPhieuCham(), style);
				

				Double diemPB2 = (double) 0;
				diemPB2 += phieuChamHDSV2.getDiemPhieuCham();
				createCellTieuDe(rowNext, nextColum2++, phieuChamHDSV2.getDiemPhieuCham(), style);

				for (PhieuCham pc : phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(0), "PB")) {
					createCellTieuDe(rowNext, nextColum2++, pc.getDiemPhieuCham(), style);
					diemPB2 += pc.getDiemPhieuCham();
				}
				diemPB2 = diemPB2 / 3;
				createCellTieuDe(rowNext, nextColum2++,  Math.floor(diemPB2 * 100) / 100, style);

				Double diemHD2 = (phieuChamHoiDong1SV2.getDiemPhieuCham() + phieuChamHoiDong2SV2.getDiemPhieuCham() + phieuChamHoiDong3SV2.getDiemPhieuCham()) / 3;
				createCellTieuDe(rowNext, nextColum2++, phieuChamHoiDong1SV2.getDiemPhieuCham(), style);
				createCellTieuDe(rowNext, nextColum2++, phieuChamHoiDong2SV2.getDiemPhieuCham(), style);
				createCellTieuDe(rowNext, nextColum2++, phieuChamHoiDong3SV1.getDiemPhieuCham(), style);
				
				createCellTieuDe(rowNext, nextColum2++, diemHD, style);

				Double kq2 = (diemHD2 + diemPB2) / 2;
				Double diemBao2 = phieuChamHoiDong1SV1.getDsKetQua().get(0).getDiemBao() != null ? phieuChamHoiDong1SV1.getDsKetQua().get(0).getDiemBao() : 0 ;
				createCellTieuDe(rowNext, nextColum2++,  Math.floor(kq2 * 100) / 100, style);
				createCellTieuDe(rowNext, nextColum2++, diemBao2, style);
				createCellTieuDe(rowNext, nextColum2++, Math.floor((kq + diemBao2) * 10) / 10, style);
			} else {
				int nextColum2 = columnCount + 1;
				for (int i = 0; i < 47; i++) {
					createCell(rowNext, nextColum2++, "", style);

				}
			}
		} else {
			PhieuCham phieuChamHoiDong1SV1 = phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(0), "CT").get(0);
			PhieuCham phieuChamHoiDong2SV1 = phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(0), "TK").get(0);
			createCell(row, nextColum++, mas.get(0), style);
			createCell(row, nextColum++, sv.getTenSinhVien(), style);
			createCell(row, nextColum++, sv.getEmail(), style);
			createCell(row, nextColum++, nhom.getDeTai().getMaDeTai(), style);
			createCell(row, nextColum++, nhom.getDeTai().getGiangVien().getTenGiangVien(), style);
			createCell(row, nextColum++, nhom.getDeTai().getTenDeTai(), style);
			createCell(row, nextColum++, phieuChamHD1SV1.getDiemPhieuCham() >= 5 ? "Yes" : "No", style);

			for (DiemThanhPhan dtp : phieuChamHD1SV1.getDsDiemThanhPhan()) {
				createCellTieuDe(row, nextColum++, dtp.getDiemThanhPhan(), style);
			}
			createCellTieuDe(row, nextColum++, phieuChamHD1SV1.getDiemPhieuCham(), style);

			for (PhieuCham pc : phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(0), "PB")) {
				for (DiemThanhPhan dtp : pc.getDsDiemThanhPhan()) {
					createCellTieuDe(row, nextColum++, dtp.getDiemThanhPhan(), style);
				}
				createCellTieuDe(row, nextColum++, pc.getDiemPhieuCham(), style);
			}
			for (DiemThanhPhan dtp : phieuChamHoiDong1SV1.getDsDiemThanhPhan()) {
				createCellTieuDe(row, nextColum++, dtp.getDiemThanhPhan(), style);
			}
			createCellTieuDe(row, nextColum++, phieuChamHoiDong1SV1.getDiemPhieuCham(), style);

			for (DiemThanhPhan dtp : phieuChamHoiDong2SV1.getDsDiemThanhPhan()) {
				createCellTieuDe(row, nextColum++, dtp.getDiemThanhPhan(), style);
			}
			createCellTieuDe(row, nextColum++, phieuChamHoiDong2SV1.getDiemPhieuCham(), style);

			for (DiemThanhPhan dtp : phieuChamHoiDong2SV1.getDsDiemThanhPhan()) {
				
				createCellTieuDe(row, nextColum++, "", style);
			}
			createCellTieuDe(rowNext, nextColum++, "", style);
			

			Double diemPB = (double) 0;
			diemPB += phieuChamHD1SV1.getDiemPhieuCham();
			createCellTieuDe(row, nextColum++, phieuChamHD1SV1.getDiemPhieuCham() +"", style);

			for (PhieuCham pc : phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(0), "PB")) {
				createCellTieuDe(row, nextColum++, pc.getDiemPhieuCham(), style);
				diemPB += pc.getDiemPhieuCham();
			}
			diemPB = diemPB / 3;
			createCellTieuDe(row, nextColum++, Math.floor(diemPB * 100) / 100, style);

			Double diemHD = (phieuChamHoiDong1SV1.getDiemPhieuCham() + phieuChamHoiDong2SV1.getDiemPhieuCham()) / 2;
			createCellTieuDe(row, nextColum++, phieuChamHoiDong1SV1.getDiemPhieuCham(), style);
			createCellTieuDe(row, nextColum++, phieuChamHoiDong2SV1.getDiemPhieuCham(), style);
			createCellTieuDe(row, nextColum++, "", style);
			
			createCellTieuDe(row, nextColum++, diemHD, style);

			Double kq = (diemHD + diemPB) / 2;
			Double diemBao = phieuChamHoiDong1SV1.getDsKetQua().get(0).getDiemBao() != null ? phieuChamHoiDong1SV1.getDsKetQua().get(0).getDiemBao() : 0 ;
			createCellTieuDe(row, nextColum++, Math.floor(kq * 100) / 100, style);
			createCellTieuDe(row, nextColum++, diemBao, style);
			createCellTieuDe(row, nextColum++, Math.floor((kq + diemBao) * 10) / 10, style);

			if (mas.size() > 1) {
				PhieuCham phieuChamHDSV2 = phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(1), "HD").get(0);
				PhieuCham phieuChamHoiDong1SV2 = phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(1), "CT").get(0);
				PhieuCham phieuChamHoiDong2SV2 = phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(1), "TK").get(0);
				int nextColum2 = columnCount + 1;
				SinhVien sv2 = sinhVienService.layTheoMa(mas.get(1));
				createCell(rowNext, nextColum2++, mas.get(1), style);
				createCell(rowNext, nextColum2++, sv2.getTenSinhVien(), style);
				createCell(rowNext, nextColum2++, sv2.getEmail(), style);
				createCell(rowNext, nextColum2++, nhom.getDeTai().getMaDeTai(), style);
				createCell(rowNext, nextColum2++, nhom.getDeTai().getGiangVien().getTenGiangVien(), style);
				createCell(rowNext, nextColum2++, nhom.getDeTai().getTenDeTai(), style);
				createCell(rowNext, nextColum2++, phieuChamHDSV2.getDiemPhieuCham() >= 5 ? "Yes" : "No", style);

				for (DiemThanhPhan dtp : phieuChamHDSV2.getDsDiemThanhPhan()) {
					createCellTieuDe(rowNext, nextColum2++, dtp.getDiemThanhPhan(), style);
				}
				createCellTieuDe(rowNext, nextColum2++, phieuChamHDSV2.getDiemPhieuCham()+"", style);

				for (PhieuCham pc : phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(1), "PB")) {
					for (DiemThanhPhan dtp : pc.getDsDiemThanhPhan()) {
						createCellTieuDe(rowNext, nextColum2++, dtp.getDiemThanhPhan(), style);
					}
					createCellTieuDe(rowNext, nextColum2++, pc.getDiemPhieuCham(), style);
				}
				for (DiemThanhPhan dtp : phieuChamHoiDong1SV2.getDsDiemThanhPhan()) {
					createCellTieuDe(rowNext, nextColum2++, dtp.getDiemThanhPhan(), style);
				}
				createCellTieuDe(rowNext, nextColum2++, phieuChamHoiDong1SV2.getDiemPhieuCham(), style);

				for (DiemThanhPhan dtp : phieuChamHoiDong2SV2.getDsDiemThanhPhan()) {
					createCellTieuDe(rowNext, nextColum2++, dtp.getDiemThanhPhan(), style);
				}
				createCellTieuDe(rowNext, nextColum2++, phieuChamHoiDong2SV2.getDiemPhieuCham(), style);
				
				for (DiemThanhPhan dtp : phieuChamHoiDong2SV2.getDsDiemThanhPhan()) {
					createCellTieuDe(rowNext, nextColum2++, "", style);
				}
				createCellTieuDe(rowNext, nextColum2++, "", style);
				

				Double diemPB2 = (double) 0;
				diemPB2 += phieuChamHDSV2.getDiemPhieuCham();
				createCellTieuDe(rowNext, nextColum2++, phieuChamHDSV2.getDiemPhieuCham(), style);

				for (PhieuCham pc : phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(mas.get(0), "PB")) {
					createCellTieuDe(rowNext, nextColum2++, pc.getDiemPhieuCham(), style);
					diemPB2 += pc.getDiemPhieuCham();
				}
				diemPB2 = diemPB2 / 3;
				createCellTieuDe(rowNext, nextColum2++,  Math.floor(diemPB2 * 100) / 100, style);

				Double diemHD2 = (phieuChamHoiDong1SV2.getDiemPhieuCham() + phieuChamHoiDong2SV2.getDiemPhieuCham() ) / 2;
				createCellTieuDe(rowNext, nextColum2++, phieuChamHoiDong1SV2.getDiemPhieuCham(), style);
				createCellTieuDe(rowNext, nextColum2++, phieuChamHoiDong2SV2.getDiemPhieuCham(), style);
				createCellTieuDe(rowNext, nextColum2++, "", style);
				
				createCellTieuDe(rowNext, nextColum2++, diemHD, style);

				Double kq2 = (diemHD2 + diemPB2) / 2;
				Double diemBao2 = phieuChamHoiDong1SV1.getDsKetQua().get(0).getDiemBao() != null ? phieuChamHoiDong1SV1.getDsKetQua().get(0).getDiemBao() : 0 ;
				createCellTieuDe(rowNext, nextColum2++,  Math.floor(kq2 * 100) / 100, style);
				createCellTieuDe(rowNext, nextColum2++, diemBao2, style);
				createCellTieuDe(rowNext, nextColum2++, Math.floor((kq + diemBao2) * 10) / 10, style);
			} else {
				int nextColum2 = columnCount + 1;
				for (int i = 0; i < 47; i++) {
					createCell(rowNext, nextColum2++, "", style);

				}
			}
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
