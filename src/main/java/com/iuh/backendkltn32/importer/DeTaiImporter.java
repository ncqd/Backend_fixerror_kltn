package com.iuh.backendkltn32.importer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.iuh.backendkltn32.entity.DeTai;
import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.service.DeTaiService;
import com.iuh.backendkltn32.service.HocKyService;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Component
public class DeTaiImporter {
	

	@Autowired
	private HocKyService hocKyService;
	
	@Autowired
	private DeTaiService deTaiService;
	
	public static boolean isValidExcelFile(MultipartFile file) {
		return Objects.equals(file.getContentType(),
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	}

	public List<DeTai> addDataFDromExcel(InputStream inputStream, GiangVien giangVien) {
		List<DeTai> deTais = new ArrayList<>();
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			rowIterator.next();
			boolean isHasValue = true;
			HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
			DeTai deTaiCuoiTrongHK = deTaiService.getDeTaiCuoiCungTrongHocKy(hocKy.getMaHocKy(),
					hocKy.getSoHocKy());

			String maDT = "001";

			if (deTaiCuoiTrongHK == null) {
				maDT = "001";
			} else {
				Long soMaDT = Long.parseLong(deTaiCuoiTrongHK.getMaDeTai().substring(2)) + 1;
				System.out.println("chua ra so" + deTaiCuoiTrongHK.getMaDeTai().substring(2));
				if (soMaDT < 10) {
					maDT = "00" + soMaDT;
				} else if (soMaDT >= 10 && soMaDT < 100) {
					maDT = "0" + soMaDT;
				} else {
					maDT = "" + soMaDT;
				}
			}
			while (rowIterator.hasNext() && isHasValue) {
				Row nextRow = rowIterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				DeTai deTai = new DeTai();
				
				deTai.setMaDeTai("DT" + maDT);
				deTai.setGiangVien(giangVien);
				deTai.setGioiHanSoNhomThucHien(2);
				deTai.setHocKy(hocKy);
				deTai.setTrangThai(0);
				deTai.setDoKhoDeTai(0);

				while (cellIterator.hasNext() && isHasValue) {
					Cell nextCell = cellIterator.next();
					int columnIndex = nextCell.getColumnIndex();
					switch (columnIndex) {
					case 0:
						break;
					case 1:
						deTai.setTenDeTai(nextCell.getStringCellValue());
						break;
					case 2:
						deTai.setMucTieuDeTai(nextCell.getStringCellValue());
						break;
					case 3:
						deTai.setSanPhamDuKien(nextCell.getStringCellValue());
						break;
					case 4:
						deTai.setMoTa(nextCell.getStringCellValue());
						break;
					case 5:
						deTai.setYeuCauDauVao(nextCell.getStringCellValue());
						break;
					default:
						break;
					}
					columnIndex++;
				}
//				deTais.add(deTai);
				if (deTai.getTenDeTai() == null || deTai.getTenDeTai().isBlank() || deTai.getTenDeTai().isEmpty()) {
					System.out.println("null;");
					isHasValue = false;
				} else {
					deTais.add(deTai);
					maDT = "00" + (Long.parseLong(maDT) + 1) + "";
				}
			}
			workbook.close();
		}

		catch (Exception e) {
			e.getStackTrace();
		}
		System.out.println(deTais.size());
		return deTais;
	}

}