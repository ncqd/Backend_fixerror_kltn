package com.iuh.backendkltn32.export;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFCheckBox;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;

import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.PhieuChamMau;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.entity.TieuChiChamDiem;
import com.iuh.backendkltn32.service.PhieuChamMauService;
import com.iuh.backendkltn32.service.SinhVienService;
import com.iuh.backendkltn32.service.TieuChiChamDiemService;

public class PhieuChamExporter {

	private XWPFDocument document;
	private Nhom nhom;
	private String vaiTro;
	private SinhVienService sinhVienService;
	private GiangVien giangVien;
	private List<TieuChiChamDiem> dsTieuChiChamDiems;

	public PhieuChamExporter(Nhom nhom, String vaiTro, SinhVienService sinhVienService, GiangVien giangVien,
			List<TieuChiChamDiem> dsTieuChiChamDiems) {
		document = new XWPFDocument();
		this.nhom = nhom;
		this.vaiTro = vaiTro;
		this.sinhVienService = sinhVienService;
		this.giangVien = giangVien;
		this.dsTieuChiChamDiems = dsTieuChiChamDiems;
	}

	public void export(HttpServletResponse response) throws Exception {
		writeHeaderLine();
		writeDataLines();

		ServletOutputStream outputStream = response.getOutputStream();
		document.write(outputStream);
		document.close();

		outputStream.close();

	}

	private void writeHeaderLine() {
		XWPFParagraph p1 = document.createParagraph();
		p1.setAlignment(ParagraphAlignment.CENTER);

		XWPFRun run1 = p1.createRun();
		run1.setBold(true);
		run1.setFontSize(12);
		run1.setFontFamily("Times New Roman");
		run1.setText("TRƯỜNG ĐH CÔNG NGHIỆP TP. HCM");
		run1.addBreak();

		XWPFRun run2 = p1.createRun();
		run2.setFontSize(12);
		run2.setFontFamily("Times New Roman");
		run2.setText("KHOA CÔNG NGHỆ THÔNG TIN");
		run2.addBreak();
		run2.setText("=======//======");
		run2.addBreak();

		XWPFRun run4 = p1.createRun();
		run4.setBold(true);
		run4.setFontSize(12);
		run4.setFontFamily("Times New Roman");
		run4.setText("PHIẾU CHẤM ĐIỂM KHÓA LUẬN TỐT NGHIỆP");
	}

	private void writeDataLines() throws InvalidFormatException, IOException, URISyntaxException {
		XWPFParagraph p2 = document.createParagraph();
		p2.setAlignment(ParagraphAlignment.LEFT);

		XWPFParagraph p3 = document.createParagraph();
		p3.setAlignment(ParagraphAlignment.LEFT);

		XWPFRun run1 = p2.createRun();
		run1.setBold(true);
		run1.setFontSize(12);
		run1.setFontFamily("Times New Roman");
		run1.setText("1. Tên đề tài: ");

		XWPFRun run2 = p2.createRun();
		run2.setFontSize(12);
		run2.setFontFamily("Times New Roman");
		run2.setText(nhom.getDeTai().getTenDeTai());
		run2.addBreak();
		run2.addBreak();

		XWPFRun run3 = p2.createRun();
		run3.setBold(true);
		run3.setFontSize(12);
		run3.setFontFamily("Times New Roman");
		run3.setText("2. Nhóm thực hiện: ");

		XWPFRun run4 = p3.createRun();
		run4.setFontSize(12);
		run4.setFontFamily("Times New Roman");
		p3.setIndentationLeft(400);

		int sttSv = 1;
		for (String maSV : sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom())) {
			SinhVien sinhVien = sinhVienService.layTheoMa(maSV);
			run4.setText("Họ tên sinh viên " + sttSv + ": " + sinhVien.getTenSinhVien());
			if (sttSv < sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom()).size()) {
				run4.addBreak();
			}
			sttSv++;
		}

		XWPFParagraph p4 = document.createParagraph();
		p4.setAlignment(ParagraphAlignment.LEFT);

		XWPFRun run5 = p4.createRun();
		run5.setBold(true);
		run5.setFontSize(12);
		run5.setFontFamily("Times New Roman");
		run5.setText("3. Họ và tên người chấm điểm: ");

		XWPFRun run6 = p4.createRun();
		run6.setFontSize(12);
		run6.setFontFamily("Times New Roman");
		run6.setText(giangVien.getTenGiangVien());

		XWPFParagraph p5 = document.createParagraph();
		p5.setAlignment(ParagraphAlignment.LEFT);

		XWPFRun run7 = p5.createRun();
		run7.setFontSize(12);
		run7.setFontFamily("Times New Roman");
		run7.setText("4. Vai trò của người đánh giá:   ");

		Path checkboxPath = Paths.get(ClassLoader.getSystemResource("file\\checkbox.png").toURI());
		Path unCheckboxPath = Paths.get(ClassLoader.getSystemResource("file\\unchecked.png").toURI());

		switch (vaiTro) {
		case "HD":
			run7.addPicture(Files.newInputStream(checkboxPath), XWPFDocument.PICTURE_TYPE_PNG,
					checkboxPath.getFileName().toString(), Units.toEMU(10), Units.toEMU(10));
			run7.setText("   GV hướng dẫn   ");
			run7.addPicture(Files.newInputStream(unCheckboxPath), XWPFDocument.PICTURE_TYPE_PNG,
					unCheckboxPath.getFileName().toString(), Units.toEMU(10), Units.toEMU(10));
			run7.setText("   Phản  biện   ");

			run7.addPicture(Files.newInputStream(unCheckboxPath), XWPFDocument.PICTURE_TYPE_PNG,
					unCheckboxPath.getFileName().toString(), Units.toEMU(10), Units.toEMU(10));
			run7.setText("   Thành viên HĐ");
			break;
		case "PB":
			run7.addPicture(Files.newInputStream(unCheckboxPath), XWPFDocument.PICTURE_TYPE_PNG,
					unCheckboxPath.getFileName().toString(), Units.toEMU(10), Units.toEMU(10));
			run7.setText("   GV hướng dẫn   ");
			run7.addPicture(Files.newInputStream(checkboxPath), XWPFDocument.PICTURE_TYPE_PNG,
					checkboxPath.getFileName().toString(), Units.toEMU(10), Units.toEMU(10));
			run7.setText("   Phản  biện   ");

			run7.addPicture(Files.newInputStream(unCheckboxPath), XWPFDocument.PICTURE_TYPE_PNG,
					unCheckboxPath.getFileName().toString(), Units.toEMU(10), Units.toEMU(10));
			run7.setText("   Thành viên HĐ");
			break;
		case "HoiDong":
			run7.addPicture(Files.newInputStream(unCheckboxPath), XWPFDocument.PICTURE_TYPE_PNG,
					unCheckboxPath.getFileName().toString(), Units.toEMU(10), Units.toEMU(10));
			run7.setText("   GV hướng dẫn   ");
			run7.addPicture(Files.newInputStream(unCheckboxPath), XWPFDocument.PICTURE_TYPE_PNG,
					unCheckboxPath.getFileName().toString(), Units.toEMU(10), Units.toEMU(10));
			run7.setText("   Phản  biện   ");

			run7.addPicture(Files.newInputStream(checkboxPath), XWPFDocument.PICTURE_TYPE_PNG,
					checkboxPath.getFileName().toString(), Units.toEMU(10), Units.toEMU(10));
			run7.setText("   Thành viên HĐ");
			break;

		}
		

		XWPFParagraph p6 = document.createParagraph();
		p6.setAlignment(ParagraphAlignment.CENTER);

		XWPFRun run8 = p6.createRun();
		run8.setBold(true);
		run8.setFontSize(14);
		run8.setFontFamily("Times New Roman");
		run8.setText("NỘI DUNG ĐÁNH GIÁ ");
		run8.addBreak();

		// create table
		XWPFTable table = document.createTable();

		XWPFTableRow tableRowOne = table.getRow(0);
		tableRowOne.addNewTableCell();
		tableRowOne.addNewTableCell();
		tableRowOne.addNewTableCell();
		tableRowOne.addNewTableCell();
		tableRowOne.addNewTableCell();

		XWPFParagraph p9 = tableRowOne.getCell(0).addParagraph();
		XWPFRun run9 = p9.createRun();
		run9.setFontSize(12);
		run9.setFontFamily("Times New Roman");
		run9.setText("LO");

		p9 = tableRowOne.getCell(1).addParagraph();
		run9 = p9.createRun();
		run9.setFontSize(12);
		run9.setFontFamily("Times New Roman");
		run9.setText("Nội dung");

		p9 = tableRowOne.getCell(2).addParagraph();
		run9 = p9.createRun();
		run9.setFontSize(12);
		run9.setFontFamily("Times New Roman");
		run9.setText("Điểm tối đa");

		p9 = tableRowOne.getCell(3).addParagraph();
		run9 = p9.createRun();
		run9.setFontSize(12);
		run9.setFontFamily("Times New Roman");
		run9.setText("Điểm đánh giá Sinh viên 1");

		p9 = tableRowOne.getCell(4).addParagraph();
		run9 = p9.createRun();
		run9.setFontSize(12);
		run9.setFontFamily("Times New Roman");
		run9.setText("Điểm đánh giá Sinh viên 2");

		p9 = tableRowOne.getCell(5).addParagraph();
		run9 = p9.createRun();
		run9.setFontSize(12);
		run9.setFontFamily("Times New Roman");
		run9.setText("CÁC Ý KIẾN NHẬN XÉT");

		for (TieuChiChamDiem tc : dsTieuChiChamDiems) {
			XWPFTableRow tableRow = table.createRow();

			XWPFParagraph p10 = tableRow.getCell(0).addParagraph();
			XWPFRun run10 = p10.createRun();
			run10.setFontSize(12);
			run10.setFontFamily("Times New Roman");
			run10.setText(tc.getMaChuanDauRa() + "");

			p10 = tableRow.getCell(1).addParagraph();
			run10 = p10.createRun();
			run10.setFontSize(12);
			run10.setFontFamily("Times New Roman");
			run10.setText(tc.getTenChuanDauRa());

			p10 = tableRow.getCell(2).addParagraph();
			run10 = p10.createRun();
			run10.setFontSize(12);
			run10.setFontFamily("Times New Roman");
			run10.setText(tc.getDiemToiDa() + "");

			tableRow.getCell(3).setText("");
			tableRow.getCell(4).setText("");
			tableRow.getCell(5).setText("");
		}

		XWPFParagraph p11 = document.createParagraph();
		p11.setAlignment(ParagraphAlignment.LEFT);

		XWPFRun run11 = p11.createRun();
		run11.addBreak();
		run11.addBreak();
		run11.setFontSize(12);
		run11.setBold(true);
		run11.setFontFamily("Times New Roman");
		run11.setText("Các ý kiến khác: ");
		run11.addBreak();

		XWPFRun run12 = p11.createRun();
		for (int i = 0; i < 918; i++) {
			run12.setText(".");
		}

		XWPFParagraph p12 = document.createParagraph();
		p12.setAlignment(ParagraphAlignment.RIGHT);

		XWPFRun run13 = p12.createRun();
		run13.setFontSize(12);
		run13.setFontFamily("Times New Roman");
		run13.setText("TP. Hồ Chí Minh, ngày      tháng       năm                                    ");

		XWPFParagraph p13 = document.createParagraph();
		p13.setAlignment(ParagraphAlignment.RIGHT);
		p13.setIndentFromRight(700);

		XWPFRun run14 = p13.createRun();
		run14.setFontSize(12);
		run14.setBold(true);
		run14.setFontFamily("Times New Roman");
		run14.setText("Người chấm điểm");

	}

}
