package com.iuh.backendkltn32.importer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.iuh.backendkltn32.entity.TieuChiChamDiem;

@Component
public class TieuChiChamDiemImporter {

	public static boolean isValidExcelFile(MultipartFile file) {
		return Objects.equals(file.getContentType(),
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	}

	public List<TieuChiChamDiem> addDataFDromExcel(InputStream inputStream) {
		List<TieuChiChamDiem> tieuChiChamDiems = new ArrayList<>();
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			rowIterator.next();
			boolean isHasValue = true;

			while (rowIterator.hasNext() && isHasValue) {
				Row nextRow = rowIterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				TieuChiChamDiem tieuChiChamDiem = new TieuChiChamDiem();

				while (cellIterator.hasNext() && isHasValue) {
					Cell nextCell = cellIterator.next();
					int columnIndex = nextCell.getColumnIndex();
					System.out.println(columnIndex);
					switch (columnIndex) {
					case 0:
						break;
					case 1:
						tieuChiChamDiem.setTenChuanDauRa(nextCell.getStringCellValue());
						break;
					case 2:
						if (nextCell.getCellType() == CellType.NUMERIC) {
							tieuChiChamDiem.setDiemToiDa(nextCell.getNumericCellValue());
						} else {
							tieuChiChamDiem.setDiemToiDa(Double.parseDouble(nextCell.getStringCellValue()));
						}
						break;
					}
					columnIndex++;
				}
				if (tieuChiChamDiem.getTenChuanDauRa() == null || tieuChiChamDiem.getTenChuanDauRa().isBlank()
						|| tieuChiChamDiem.getTenChuanDauRa().isEmpty()) {
					System.out.println("null;");
					isHasValue = false;
				} else {
					tieuChiChamDiems.add(tieuChiChamDiem);
				}
			}
			workbook.close();
		} catch (Exception e) {
			e.getStackTrace();
		}

		return tieuChiChamDiems;
	}

}
