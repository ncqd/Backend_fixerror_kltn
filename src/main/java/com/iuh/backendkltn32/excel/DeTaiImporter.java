package com.iuh.backendkltn32.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.iuh.backendkltn32.entity.DeTai;

import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class DeTaiImporter {
	public static boolean isValidExcelFile(MultipartFile file) {
		return Objects.equals(file.getContentType(),
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	}

	public static List<DeTai> addDataFDromExcel(InputStream inputStream) {
		List<DeTai> deTais = new ArrayList<>();
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			rowIterator.next();
			boolean isHasValue = true;
			while (rowIterator.hasNext() && isHasValue) {
				Row nextRow = rowIterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				DeTai deTai = new DeTai();

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
				if (deTai.getTenDeTai() == null) {
					System.out.println("null;");
					isHasValue = false;
				} else {
					deTais.add(deTai);
				}
			}
			workbook.close();
		}

		catch (IOException e) {
			e.getStackTrace();
		}
		System.out.println(deTais.size());
		return deTais;
	}

}