package com.iuh.backendkltn32.importer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.HocPhanKhoaLuanTotNghiep;
import com.iuh.backendkltn32.entity.LopDanhNghia;
import com.iuh.backendkltn32.entity.LopHocPhan;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.service.HocKyService;
import com.iuh.backendkltn32.service.HocPhanKhoaLuanTotNghiepService;
import com.iuh.backendkltn32.service.LopDanhNghiaService;
import com.iuh.backendkltn32.service.LopHocPhanService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Component
public class SinhVienImporter {

	@Autowired
	private HocKyService hocKyService;

	@Autowired
	private LopHocPhanService lopHocPhanService;

	@Autowired
	private HocPhanKhoaLuanTotNghiepService hocPhanKhoaLuanTotNghiepService;

	@Autowired
	private LopDanhNghiaService lopDanhNghiaService;

	public static boolean isValidExcelFile(MultipartFile file) {
		return Objects.equals(file.getContentType(),
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	}

	public List<SinhVien> addDataFDromExcel(InputStream inputStream) throws Exception {
		List<SinhVien> sinhViens = new ArrayList<>();
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter dataFormatter = new DataFormatter();

			String maHP = xuLyDuLieuNoiChiDinh(sheet, "B6").substring(14, 25);
			HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();

			HocPhanKhoaLuanTotNghiep hpkl = hocPhanKhoaLuanTotNghiepService.layTheoMa(maHP);
			if (hpkl == null) {
				hpkl = new HocPhanKhoaLuanTotNghiep();
				hpkl.setMaHocPhan(maHP);
				hpkl.setSoTinChi("5");
				hpkl.setTenHocPhan("Khóa Luận Tốt Nghiệp");
				hpkl.setHocPhantienQuyet(true);
				hpkl.setHocKy(hocKy);
				hpkl = hocPhanKhoaLuanTotNghiepService.luu(hpkl);
			}

			String maLopHocPhan = xuLyDuLieuNoiChiDinh(sheet, "B5").substring(9, 21);
			String tenLopHocPhan = xuLyDuLieuNoiChiDinh(sheet, "B5").substring(24);
			LopHocPhan lopHocPhan = lopHocPhanService.layTheoMa(maLopHocPhan);
			if (lopHocPhan == null) {
				lopHocPhan = new LopHocPhan();
				lopHocPhan.setMaLopHocPhan(maLopHocPhan);
				lopHocPhan.setGhiChu("Chưa có");
				lopHocPhan.setHocPhanKhoaLuanTotNghiep(hpkl);
				lopHocPhan.setPhong("Chưa có");
				lopHocPhan.setTenLopHocPhan(tenLopHocPhan);
				lopHocPhan.setThoiGianBatDau(null);
				lopHocPhan.setThoiGianKetThuc(null);
				lopHocPhan = lopHocPhanService.luu(lopHocPhan);
			}
			Iterator<Row> rowIterator = sheet.iterator();
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
			boolean isHasValue = true;

			while (rowIterator.hasNext() && isHasValue) {
				Row nextRow = rowIterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				SinhVien sinhVien = new SinhVien();
				sinhVien.setLopHocPhan(lopHocPhan);
				sinhVien.setAnhDaiDien("");
				sinhVien.setEmail("a@gmail.com");
				sinhVien.setNamNhapHoc(2023);
				sinhVien.setNoiSinh("Ho Chi Minh");
				String tenSinhVien = "";
				
				if (nextRow.getCell(3).getCellType() == CellType.BLANK) {
					System.out.println("null;");
					isHasValue = false;
					break;
				}
				while (cellIterator.hasNext() && isHasValue) {
					Cell nextCell = cellIterator.next();
					int columnIndex = nextCell.getColumnIndex();
					
					switch (columnIndex) {
					case 0:
						break;
					case 1:
						if (nextCell.getCellType() == CellType.NUMERIC) {
							sinhVien.setMaSinhVien((int) nextCell.getNumericCellValue() + "");
						} else {
							sinhVien.setMaSinhVien(nextCell.getStringCellValue());
						}

						break;
					case 2:
						tenSinhVien = nextCell.getStringCellValue();
						break;
					case 3:
						sinhVien.setTenSinhVien(tenSinhVien + " " + nextCell.getStringCellValue());
						break;
					case 4:
						sinhVien.setGioiTinh(nextCell.getStringCellValue().equals("Nam") ? 1 : 0);
						break;
					case 5:
						Integer ngay = Integer.parseInt(nextCell.getStringCellValue().substring(0, 2));
						Integer thang = Integer.parseInt(nextCell.getStringCellValue().substring(3, 5));
						Integer nam = Integer.parseInt(nextCell.getStringCellValue().substring(6));
						
						java.util.Date date = new java.util.Date();
						date.setYear(nam - 1900);
						date.setMonth(thang - 1);
						date.setDate(ngay);
//						System.out.println(date);
						sinhVien.setNgaySinh(date);
						break;
					case 6:
						sinhVien.setDienThoai(nextCell.getStringCellValue());
						break;
					case 7:
						break;
					case 8:
						LopDanhNghia lopDanhNghia = lopDanhNghiaService.layTheoMa(
								nextCell.getStringCellValue() == null ? "DHKTPM15ATT" : nextCell.getStringCellValue());
						if (lopDanhNghia == null) {
							lopDanhNghia = new LopDanhNghia(nextCell.getStringCellValue(), hocKy.getMaHocKy(),
									Integer.parseInt(hocKy.getSoHocKy()), "Chưa có", 50);
							lopDanhNghia = lopDanhNghiaService.luu(lopDanhNghia);
						}
						sinhVien.setLopDanhNghia(lopDanhNghia);
						break;
					}

					columnIndex++;
					
				}
				
				sinhViens.add(sinhVien);
			}
			workbook.close();
		}

		catch (IOException e) {
			e.getStackTrace();
		}
//		System.out.println(sinhViens.size());
		return sinhViens;
	}

	public String xuLyDuLieuNoiChiDinh(XSSFSheet sheet, String noi) {
		CellReference cellReference = new CellReference(noi);
		Row row = sheet.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol());

		return cell.getStringCellValue();
	}

}