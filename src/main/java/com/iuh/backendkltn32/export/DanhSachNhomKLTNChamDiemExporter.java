package com.iuh.backendkltn32.export;

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

import com.iuh.backendkltn32.entity.DiemThanhPhan;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.PhanCong;
import com.iuh.backendkltn32.entity.PhieuCham;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.service.NhomService;
import com.iuh.backendkltn32.service.PhanCongService;
import com.iuh.backendkltn32.service.PhieuChamService;
import com.iuh.backendkltn32.service.SinhVienService;

public class DanhSachNhomKLTNChamDiemExporter {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private NhomService nhomService;
	private SinhVienService sinhVienService;
	private PhanCongService phanCongService;
	private HocKy hocKy;

	public DanhSachNhomKLTNChamDiemExporter(NhomService nhomService, SinhVienService sinhVienService,
			PhanCongService phanCongService, HocKy hocKy) {
		this.workbook = new XSSFWorkbook();
		this.nhomService = nhomService;
		this.sinhVienService = sinhVienService;
		this.hocKy = hocKy;
		this.phanCongService = phanCongService;
	}

	private void writeHeaderLine() throws Exception {
		sheet = workbook.createSheet("DS_Nhom_KLTN_RA_HD_POSTER");

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
		Row row = sheet.createRow(1);
		Long soHocKyTruoc = Long.parseLong(hocKy.getMaHocKy().substring(0, hocKy.getMaHocKy().length() - 1)) - 1;
		Long soHocKyNay = Long.parseLong(hocKy.getMaHocKy().substring(0, hocKy.getMaHocKy().length() - 1));
		createCell(row, 0,
				"DANH SÁCH THÀNH VIÊN CHẤM BÁO CÁO HỘI ĐỒNG & POSTER  \n"
						+ "KHÓA LUẬN TỐT NGHIỆP - HỌC KỲ I - 20"+soHocKyTruoc+"-20" +soHocKyNay+"   \n " + "NGÀNH KỸ THUẬT PHẦN MỀM  \n "
						+ "Thời gian: 8g - ngày 01/6/20"+soHocKyNay+" - địa điểm theo thông báo của khoa",
				styleName);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 17));

		row = sheet.createRow(2);
		createCell(row, 0, "Lưu ý: \n"
				+ "(1) Thời gian nộp quyển báo cáo (bản cứng & mềm): khoảng sau 1 tuần ngày 01/6/2023 - Ngày giờ địa điểm sẽ thông báo trên group KLTN \n"
				+ "(2) Bản cứng: In bìa xanh theo mẫu của khoa + đĩa CD chứa bản mềm, hướng dẫn và sản phẩm, và có đầy đủ chữ ký của GVHD, 2 thành viên trong hội đồng báo cáo --> trong buổi báo cáo các bạn in rời các tờ giấy nhận xét và nhờ các GV nhận xét và ký tên.",
				style);
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 17));

		row = sheet.createRow(3);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setBorderRight(BorderStyle.MEDIUM);
		style.setBorderLeft(BorderStyle.MEDIUM);
		style.setBorderTop(BorderStyle.MEDIUM);
		style.setBorderBottom(BorderStyle.MEDIUM);
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
//		createCell(row, 15, "Hội đồng", style);
		createCell(row, 15, "TV_HD1", style);
		createCell(row, 16, "TV_HD2", style);
		createCell(row, 17, "ThuKy", style);
	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		Cell cell = row.createCell(columnCount);
		sheet.setColumnWidth(columnCount, 20 * 256);
		sheet.setDefaultRowHeightInPoints(30);
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
	
	private void createCellContent(Row row, int columnCount, Object value, CellStyle style) {
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

	private void writeDataLines() throws RuntimeException {
		int rowCount = 4;

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
		
		for (Nhom nhom : nhomService.layNhomRaDuocPBHD(hocKy.getMaHocKy())) {
			if (nhomService.layNhomRaDuocPBPoster(hocKy.getMaHocKy()).contains(nhom)) {
				break;
			}
			Row row = sheet.createRow(rowCount++);
			row.setHeight((short) 550);
			int columnCount = 0;
			createCellContent(row, columnCount++, nhom.getMaNhom(), style);
			List<String> mas = sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom());
			for (String ma : mas) {
				SinhVien sv = sinhVienService.layTheoMa(ma);
				createCellContent(row, columnCount++, sv.getMaSinhVien(), style);
				createCellContent(row, columnCount++, sv.getTenSinhVien(), style);
				createCellContent(row, columnCount++, sv.getEmail(), style);
			}
			if (mas.size() <= 1) {
				createCellContent(row, columnCount++, "", style);
				createCellContent(row, columnCount++, "", style);
				createCellContent(row, columnCount++, "", style);
			}

			createCellContent(row, columnCount++, nhom.getDeTai().getMaDeTai(), style);
			createCellContent(row, columnCount++, nhom.getDeTai().getGiangVien().getTenGiangVien(), style);
			createCellContent(row, columnCount++, nhom.getDeTai().getTenDeTai(), style);
			createCellContent(row, columnCount++, "YES", style);
			createCellContent(row, columnCount++, "       ", style);
			for (PhanCong phanCong : phanCongService.layPhanCongTheoMaNhom(nhom)) {
				if (phanCong.getViTriPhanCong().equals("chu tich") ||
						phanCong.getViTriPhanCong().equals("thu ky") || 
						phanCong.getViTriPhanCong().equals("thanh vien 3") ) {
					break;
				}
				createCellContent(row, columnCount++, phanCong.getGiangVien().getTenGiangVien() + "\n" + phanCong.getGiangVien().getEmail(), style);
			}
			createCellContent(row, columnCount++, "Hội Đồng", style);
			for (PhanCong phanCong : phanCongService.layPhanCongTheoMaNhom(nhom)) {
				if (phanCong.getViTriPhanCong().equals("chu tich") ||
						phanCong.getViTriPhanCong().equals("thu ky") || 
						phanCong.getViTriPhanCong().equals("thanh vien 3") ) {
					createCellContent(row, columnCount++, phanCong.getGiangVien().getTenGiangVien() + "\n" + phanCong.getGiangVien().getEmail(), style);
				}
			}
		}
		
		
		for (Nhom nhom : nhomService.layNhomRaDuocPBPoster(hocKy.getMaHocKy())) {
			
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
			createCell(row, columnCount++, "YES", style);
			createCell(row, columnCount++, "       ", style);
			for (PhanCong phanCong : phanCongService.layPhanCongTheoMaNhom(nhom)) {
				if (phanCong.getViTriPhanCong().equals("chu tich") ||
						phanCong.getViTriPhanCong().equals("thu ky") || 
						phanCong.getViTriPhanCong().equals("thanh vien 3") ) {
					break;
				}
				createCell(row, columnCount++, phanCong.getGiangVien().getTenGiangVien() + "\n" + phanCong.getGiangVien().getEmail(), style);
			}
			createCell(row, columnCount++, "Poster", style);
			for (PhanCong phanCong : phanCongService.layPhanCongTheoMaNhom(nhom)) {
				if (phanCong.getViTriPhanCong().equals("chu tich") ||
						phanCong.getViTriPhanCong().equals("thu ky") || 
						phanCong.getViTriPhanCong().equals("thanh vien 3") ) {
					createCell(row, columnCount++, phanCong.getGiangVien().getTenGiangVien() + "\n" + phanCong.getGiangVien().getEmail(), style);
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
