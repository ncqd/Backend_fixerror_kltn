package com.iuh.backendkltn32.export;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

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

import com.iuh.backendkltn32.entity.LopHocPhan;
import com.iuh.backendkltn32.entity.PhieuCham;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.service.NhomService;
import com.iuh.backendkltn32.service.PhieuChamService;
import com.iuh.backendkltn32.service.SinhVienService;

public class SinhVienExcelExporoter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private SinhVienService sinhVienService;
	private PhieuChamService phieuChamService;
	private LopHocPhan lopHocPhan;

	public SinhVienExcelExporoter(SinhVienService sinhVienService, PhieuChamService phieuChamService,
			LopHocPhan lopHocPhan) {
		workbook = new XSSFWorkbook();
		this.sinhVienService = sinhVienService;
		this.phieuChamService = phieuChamService;
		this.lopHocPhan = lopHocPhan;
	}

	private void writeHeaderLine() throws Exception {
		sheet = workbook.createSheet(lopHocPhan.getMaLopHocPhan() + " - " + lopHocPhan.getTenLopHocPhan());

		Row row = sheet.createRow(0);
		CellStyle style = workbook.createCellStyle();
		CellStyle styleHeader = workbook.createCellStyle();
		CellStyle styleTable = workbook.createCellStyle();
		CellStyle styleContent = workbook.createCellStyle();

		style.setWrapText(true);
		XSSFFont font = workbook.createFont();
		XSSFFont fontHeader = workbook.createFont();
		XSSFFont fontTable = workbook.createFont();
		XSSFFont fontContent = workbook.createFont();

		InputStream inputStream = new FileInputStream(
				"D:\\test\\backend-kltn-32\\src\\main\\resources\\file\\logo-iuh-xoa-phong.png");
		byte[] logoIuh = IOUtils.toByteArray(inputStream);

		int pictureIdx = workbook.addPicture(logoIuh, Workbook.PICTURE_TYPE_PNG);

		// close the input stream
		inputStream.close();
		// Returns an object that handles instantiating concrete classes
		CreationHelper helper = workbook.getCreationHelper();
		// Creates the top-level drawing patriarch.
		Drawing drawing = sheet.createDrawingPatriarch();

		// Create an anchor that is attached to the worksheet
		ClientAnchor anchor = helper.createClientAnchor();

		// create an anchor with upper left cell _and_ bottom right cell
		anchor.setCol1(0); // Column B
		anchor.setRow1(0); // Row 3
		anchor.setCol2(2); // Column C
		anchor.setRow2(4); // Row 4
		sheet.addMergedRegion(new CellRangeAddress(0, 3, 0, 1));

		// Creates a picture
		drawing.createPicture(anchor, pictureIdx);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);

		row = sheet.createRow(0);
		createCell(row, 2,
				"BỘ CÔNG THƯƠNG \n TRƯỜNG ĐẠI HỌC CÔNG NGHIỆP TP.HCM \n -------------------------------------- ",
				style);
		sheet.addMergedRegion(new CellRangeAddress(0, 3, 2, 6));
		
		fontHeader.setBold(true);
		fontHeader.setFontHeightInPoints((short) 16);
		fontHeader.setFontName("Times New Roman");
		styleHeader.setFont(fontHeader);
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);

		row = sheet.createRow(4);
		createCell(row, 0, "DANH SÁCH SINH VIÊN LỚP HỌC PHẦN ", styleHeader);
		sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 12));


		fontTable.setBold(true);
		fontTable.setFontHeightInPoints((short) 11);
		fontHeader.setFontName("Times New Roman");
		styleTable.setFont(fontTable);
		styleTable.setAlignment(HorizontalAlignment.CENTER);
		styleTable.setVerticalAlignment(VerticalAlignment.CENTER);
		
		fontContent.setBold(true);
		fontContent.setFontHeightInPoints((short) 11);
		fontContent.setFontName("Times New Roman");
		styleContent.setFont(fontContent);
		
		row = sheet.createRow(5);
		createCell(row, 1, "Ngành: Kỹ thuật phần mềm", styleContent);
		sheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 4));
		createCell(row, 7, "Đợt: HK1 (2022-2023)", styleContent);
		
		row = sheet.createRow(6);
		createCell(row, 1, "Môn học phần: " + lopHocPhan.getHocPhanKhoaLuanTotNghiep().getMaHocPhan() + " - " +
				lopHocPhan.getHocPhanKhoaLuanTotNghiep().getTenHocPhan(), styleContent);
		sheet.addMergedRegion(new CellRangeAddress(6, 6, 1, 4));
		createCell(row, 7, "Năm học: 2022-2023", styleContent);
		
		row = sheet.createRow(7);
		createCell(row, 1, "Lớp học: "+ lopHocPhan.getMaLopHocPhan() +" - " + lopHocPhan.getTenLopHocPhan(), styleContent);
		sheet.addMergedRegion(new CellRangeAddress(7, 7, 1, 4));
		createCell(row, 7, "Bậc đào tạo: Đại học", styleContent);
		
		row = sheet.createRow(8);
		createCell(row, 1, "Chuyên ngành: Kỹ thuật phần mềm - 7480103", styleContent);
		sheet.addMergedRegion(new CellRangeAddress(8, 8, 1, 4));
		createCell(row, 7, "Loại đào tạo: Tiên tiến", styleContent);

		row = sheet.createRow(9);
		createCell(row, 0, "STT", styleTable);
		createCell(row, 1, "Mã sinh viên", styleTable);
		createCell(row, 2, "Họ đệm", styleTable);
		createCell(row, 3, "Tên", styleTable);
		createCell(row, 4, "E-mail", styleTable);
		createCell(row, 5, "Giới Tính", styleTable);
		createCell(row, 6, "Ngày sinh", styleTable);
		createCell(row, 7, "Số Điện Thoại", styleTable);
		createCell(row, 8, "Nhóm", styleTable);
		createCell(row, 9, "Mã Lớp", styleTable);
		createCell(row, 10, "Điểm", styleTable);
		createCell(row, 11, "Ký Tên", styleTable);
		createCell(row, 12, "Ghi Chú", styleTable);

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
		int rowCount = 10;
		
		CellStyle styleContent = workbook.createCellStyle();
		XSSFFont fontContent = workbook.createFont();
		
		fontContent.setFontHeightInPoints((short) 11);
		fontContent.setFontName("Times New Roman");
		styleContent.setFont(fontContent);
		styleContent.setAlignment(HorizontalAlignment.CENTER);
		styleContent.setVerticalAlignment(VerticalAlignment.CENTER);
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		int stt = 1;
		for (SinhVien sinhVien : sinhVienService.layTatCaSinhVienTheoLopHocPhan(lopHocPhan)) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			List<String> hoTenSinhVien = Arrays.asList(sinhVien.getTenSinhVien().split("\\s"));
			String ten = hoTenSinhVien.get(hoTenSinhVien.size() - 1);
			StringBuffer hoDem = new StringBuffer();
			hoTenSinhVien.subList(0, hoTenSinhVien.size() - 1).forEach(ho -> {
				hoDem.append(ho);
			});
			createCell(row, columnCount++, stt++, styleContent);
			createCell(row, columnCount++, sinhVien.getMaSinhVien(), styleContent);
			createCell(row, columnCount++, hoDem, styleContent);
			createCell(row, columnCount++, ten, styleContent);
			createCell(row, columnCount++, sinhVien.getEmail(), styleContent);
			createCell(row, columnCount++, sinhVien.getGioiTinh() == 1 ? "Nam" : "Nữ", styleContent);
			createCell(row, columnCount++, format.format(sinhVien.getNgaySinh()), styleContent);
			createCell(row, columnCount++, sinhVien.getDienThoai(), styleContent);
			createCell(row, columnCount++, sinhVien.getNhom() == null ? "" : sinhVien.getNhom().getMaNhom(), styleContent);
			createCell(row, columnCount++, sinhVien.getLopDanhNghia().getMaLopDanhNghia().toString(), styleContent);
			if (phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(sinhVien.getMaSinhVien(), "CT").size() > 0) {
				Double diemHD = phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(sinhVien.getMaSinhVien(), "HD").get(0)
						.getDiemPhieuCham();
				Double diemPB = (double) 0;
				Double diemHoiDong = (double) 0;
				for (PhieuCham p : phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(sinhVien.getMaSinhVien(), "PB")) {
					diemPB += p.getDiemPhieuCham();
				}
				diemPB = (diemHD + diemPB) / 3;
				diemHoiDong += phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(sinhVien.getMaSinhVien(), "HD").get(0)
						.getDiemPhieuCham();
				diemHoiDong += phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(sinhVien.getMaSinhVien(), "HD").get(0)
						.getDiemPhieuCham();
				diemHoiDong += phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(sinhVien.getMaSinhVien(), "HD").get(0)
						.getDiemPhieuCham();
				diemHoiDong = diemHoiDong / 3;
				diemHoiDong = (diemPB + diemHD) / 2;
				createCell(row, columnCount++, diemHoiDong, styleContent);
			} else {
				createCell(row, columnCount++, " ", styleContent);
			}
			createCell(row, columnCount++, " ", styleContent);
			createCell(row, columnCount++, " ", styleContent);

		}
		Row row = sheet.createRow(rowCount+2);
		createCell(row, 1, "Sĩ số: " + (stt-1), styleContent);
		java.sql.Timestamp today = new java.sql.Timestamp(System.currentTimeMillis());
		createCell(row, 4, "  , ngày " + today.getDate() +" tháng " + (today.getMonth() + 1) + " năm " + (today.getYear() + 1900), styleContent);
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