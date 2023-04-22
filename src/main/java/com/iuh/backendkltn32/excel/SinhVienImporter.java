package com.iuh.backendkltn32.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.iuh.backendkltn32.entity.DeTai;
import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.service.DeTaiService;
import com.iuh.backendkltn32.service.HocKyService;

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
	private DeTaiService deTaiService;

	public static boolean isValidExcelFile(MultipartFile file) {
		return Objects.equals(file.getContentType(),
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	}

	public List<SinhVien> addDataFDromExcel(InputStream inputStream) {
		List<SinhVien> sinhViens = new ArrayList<>();
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
			boolean isHasValue = true;
//			HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
			int size = 0;
			CellReference cellReference = new CellReference("B5");
			Row row = sheet.getRow(cellReference.getRow());
			Cell cell = row.getCell(cellReference.getCol());
			
			System.out.println("lay ra tu gia tri dat biet" + cell.getStringCellValue());
			while (rowIterator.hasNext() && size <= 5) {
				Row nextRow = rowIterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				
				while (cellIterator.hasNext() && isHasValue) {
					Cell nextCell = cellIterator.next();
					
					switch (nextCell.getCellType()) {
					case STRING:
						System.out.print(nextCell.getAddress() + " chu \n");
						size ++;
						break;
					case NUMERIC:
						System.out.print((int)nextCell.getNumericCellValue() + " so \n");
						break;
					}
				}
//				if (deTai.getTenDeTai() == null || deTai.getTenDeTai().isBlank() || deTai.getTenDeTai().isEmpty()) {
//					System.out.println("null;");
//					isHasValue = false;
//				} else {
//					deTais.add(deTai);
//					maDT = "00" + (Long.parseLong(maDT) + 1) + "";
//				}
			}
			workbook.close();
		}

		catch (IOException e) {
			e.getStackTrace();
		}
		System.out.println(sinhViens.size());
		return sinhViens;
	}
	
	public String xuLyDuLieuNoiChiDinh(XSSFSheet sheet, String noi) {
		CellReference cellReference = new CellReference(noi);
		Row row = sheet.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol());
		
		return cell.getStringCellValue();
	}

}