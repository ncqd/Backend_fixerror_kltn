package com.iuh.backendkltn32.importer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.Khoa;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Component
public class GiangVienImporter {

	public static boolean isValidExcelFile(MultipartFile file) {
		return Objects.equals(file.getContentType(),
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	}

	public List<GiangVien> addDataFDromExcel(InputStream inputStream) throws Exception {
		List<GiangVien> giangViens = new ArrayList<>();
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);

			
			Khoa khoa = new Khoa("1", "CNTT");

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
			boolean isHasValue = true;

			while (rowIterator.hasNext() && isHasValue) {
				Row nextRow = rowIterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				GiangVien giangVien = new GiangVien();
				giangVien.setAnhDaiDien("");
				giangVien.setEmail("a@gmail.com");
				giangVien.setKhoa(khoa);
				String tenGiangVien = "";
				
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
							giangVien.setMaGiangVien((int) nextCell.getNumericCellValue() + "");
						} else {
							giangVien.setMaGiangVien(nextCell.getStringCellValue());
						}

						break;
					case 2:
						tenGiangVien = nextCell.getStringCellValue();
						break;
					case 3:
						giangVien.setTenGiangVien(tenGiangVien + " " + nextCell.getStringCellValue());;
						break;
					case 4:
						giangVien.setGioiTinh(nextCell.getStringCellValue().equals("Nam") ? 1 : 0);
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
						giangVien.setNgaySinh(date);
						break;
					case 6:
						giangVien.setSoDienThoai(nextCell.getStringCellValue());
						break;
					case 7:
						giangVien.setNamCongTac((int)nextCell.getNumericCellValue());
						break;
					case 8:
						giangVien.setHocVi(nextCell.getStringCellValue());
						break;
					case 9:
						giangVien.setCmnd(nextCell.getStringCellValue());
						break;
					}
					
					columnIndex++;
					
				}
				
				giangViens.add(giangVien);
			}
			workbook.close();
		}

		catch (IOException e) {
			e.getStackTrace();
		}
//		System.out.println(sinhViens.size());
		return giangViens;
	}

	public String xuLyDuLieuNoiChiDinh(XSSFSheet sheet, String noi) {
		CellReference cellReference = new CellReference(noi);
		Row row = sheet.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol());

		return cell.getStringCellValue();
	}

}