package com.iuh.backendkltn32.export;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class FileMauExcelDeTai {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	public FileMauExcelDeTai() {
		super();
		this.workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() throws Exception {
		sheet = workbook.createSheet("MauTieuChi");

		Row row = sheet.createRow(1);
		sheet.setColumnWidth(2, 50);

		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		XSSFFont font = workbook.createFont();

		row = sheet.createRow(0);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setBorderRight(BorderStyle.MEDIUM);
		font.setFontHeight(15);
		font.setFontHeightInPoints((short) 12);
		style.setFont(font);
		createCell(row, 0, "#", style);
		createCell(row, 1, "Tên đề tài", style);
		createCell(row, 2, "MỤC TIÊU ĐỀ TÀI", style);
		createCell(row, 3, "DỰ KIẾN SẢN PHẨM NGHIÊN CỨU CỦA ĐỀ TÀI VÀ KHẢ NĂNG ỨNG DỤNG", style);
		createCell(row, 4, "Mô tả", style);
		createCell(row, 5, "Yêu cầu đầu vào", style);
		createCell(row, 6, "Yêu cầu đầu ra (Output Standards)", style);
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

	private void writeDataLines() throws RuntimeException {
		int rowCount = 1;

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

			Row row = sheet.createRow(rowCount++);
			row.setHeight((short) 550);
			int columnCount = 0;
			createCell(row, columnCount++, "1", style);
			createCell(row, columnCount++, "\"Viết chương trình ứng dụng Hệ thống quản lý phòng khám.\r\n"
					+ "(tối đa 1 nhóm)\"\r\n", style);
			createCell(row, columnCount++, "Vận dụng kiến thức đã học, nghiên cứu tài liệu để viết ứng dụng có tính thực tế.\r\n", style);
			createCell(row, columnCount++, "Ứng dụng phải có tính thực tế và áp dụng được\r\n", style);
			createCell(row, columnCount++, "\"- Đặt lịch khám online hoặc trực tiếp tại phòng khám.\r\n"
					+ "- Tiếp nhận bệnh nhân, phân bổ bệnh nhân đến khám vào các phòng khám, theo thứ tự ưu tiên.\r\n"
					+ "- Thăm khám, chuẩn đoán.\r\n"
					+ "- Chỉ định xét nghiệm (nếu có)\r\n"
					+ "- Kê đơn thuốc.\r\n"
					+ "- Hẹn lịch tái khám.\r\n"
					+ "- ...\r\n"
					+ "- Bán thuốc theo đơn thuốc.\r\n"
					+ "- Bán thuốc cho khách vãng lai\r\n"
					+ "- ...\r\n"
					+ "- Chức năng quản lý, tìm kiếm, tra cứu lịch sử bệnh của bệnh nhân, thống kê, báo cáo ....\r\n"
					+ "- Ràng buộc toàn vẹn dữ liệu\r\n"
					+ "", style);
			createCell(row, columnCount++, "\"Yêu cầu đối với sinh viên\r\n"
					+ "- Kiến thức:\r\n"
					+ " Nắm vững kiến thức về lập trình hướng đối tượng và mô hình hóa dữ liệu.\r\n"
					+ " Nắm vững kiến thức về phân tích thiết kế hệ thống.\r\n"
					+ " Lập trình: Java 8+, Hibernate Framework, (Java Servlet, JSP, JSF hoặc Spring Framework), Web Servers, Log4j, CSS3, HTML5, JavaScript, jQuery, Bootstrap4 ….\r\n"
					+ " Nắm vững kiến thức về SQL hoặc NoSQL.\r\n"
					+ " Biết về miroservices và hệ thống phân tán.\r\n"
					+ " Biết về web service và REST.\r\n"
					+ " Đã hoặc đang học các môn học: LT Phân tán Java, Kiến trúc và Thiết kế Phần mềm, LT WWW Java.\r\n"
					+ "- Kỹ năng:\r\n"
					+ " Đọc tài liệu tiếng anh, tìm kiếm đánh giá tài liệu.\r\n"
					+ " Sử dụng thành thạo phần mềm Enterprise Architect.\r\n"
					+ " Làm việc nhóm.\r\n"
					+ "- Thái độ:\r\n"
					+ " Làm việc nghiêm túc, không sao chép bài làm của người khác ở bất cứ hình thức nào.\r\n"
					+ " Gặp và báo cáo với giảng viên hướng dẫn hằng tuần. Nghỉ quá 3 tuần không lý do, giảng viên từ chối hướng dẫn tiếp.\"\r\n"
					+ "", style);
			createCell(row, columnCount++, "A.Sinh viên tham gia đề tài\r\n"
					+ "1) Nắm vững kiến thức, kỹ năng lập trình Java\r\n"
					+ "2) Có kiến thức, kỹ năng triển khai dự án phần mềm đầy đủ qui trình từ Requirement => Design => Coding => Unit Testing \n"
					+ "3) Có năng lực đầy đủ về Lập kế hoạch, theo dõi tiến độ, phân tích các vấn đề phát sinh trong quá trình thực hiện dự án. \n"
					+ "4) Có kiến thức về AI \n"
					+ "\n"
					+ "B.Sản phẩm \n"
					+ "1) Có tài liệu mô tả Yêu cầu dự án.\r\n"
					+ "2) Có tài liệu mô tả Thiết kế kiến trúc của dự án.\r\n"
					+ "3) Có tài liệu mô tả Thiết kế chi tiết cho chức năng chính của dự án.\r\n"
					+ "4) Có mã nguồn được lưu vết (tracking) định kỳ trên hệ thống quản lý phiên bản (version control) như Subversion hoặc Git; Mã nguồn trình bày rõ ràng, dễ hiểu theo tiêu chuẩn đã được thống nhất.\r\n"
					+ "5) Có tài liệu mô tả tình huống test và kết quả test cho chức năng chính của dự án.\r\n"
					+ "6) Có tài liệu mô tả cách biên dịch, đóng gói và hướng dẫn nâng cấp mã nguồn cho dự án.", style);
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
