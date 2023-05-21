package com.iuh.backendkltn32.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iuh.backendkltn32.dto.DangKyDeTaiRequest;
import com.iuh.backendkltn32.dto.DeTaiDto;
import com.iuh.backendkltn32.dto.DeTaiRes;
import com.iuh.backendkltn32.dto.LayDeTaiRquestDto;
import com.iuh.backendkltn32.entity.DeTai;
import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.KeHoach;
import com.iuh.backendkltn32.entity.TaiKhoan;
import com.iuh.backendkltn32.export.FileMauExcelDeTai;
import com.iuh.backendkltn32.export.FileMauGiangVienExporter;
import com.iuh.backendkltn32.importer.DeTaiImporter;
import com.iuh.backendkltn32.jms.JmsListenerConsumer;
import com.iuh.backendkltn32.jms.JmsPublishProducer;
import com.iuh.backendkltn32.service.DeTaiService;
import com.iuh.backendkltn32.service.GiangVienService;
import com.iuh.backendkltn32.service.HocKyService;
import com.iuh.backendkltn32.service.KeHoachService;
import com.iuh.backendkltn32.service.TaiKhoanService;

@RestController
@RequestMapping("/api/de-tai")
public class DeTaiController {

	@Autowired
	private DeTaiService deTaiService;

	@Autowired
	private JmsListenerConsumer listenerConsumer;

	@Autowired
	private HocKyService hocKyService;

	@Autowired
	private GiangVienService giangVienService;

	@Autowired
	private JmsPublishProducer producer;

	@Autowired
	private KeHoachService keHoachService;

	@Autowired
	private TaiKhoanService taiKhoanService;

	@Autowired
	private DeTaiImporter deTaiImporter;

	@PostMapping("/them-de-tai/{maGiangVien}")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY')")
	public DeTai themDeTai(@RequestBody DeTai deTai, @PathVariable("maGiangVien") String maGiangVien) throws RuntimeException {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		TaiKhoan taiKhoan = taiKhoanService.layTheoMa(maGiangVien);
		List<KeHoach> keHoachs = keHoachService.layTheoTenVaMaHocKyVaiTro(hocKy.getMaHocKy(), "Lịch thêm đề tài",
				taiKhoan.getVaiTro().getTenVaiTro().name());
		if (keHoachs.size() > 0) {
			KeHoach keHoach = keHoachs.get(0);
			if (keHoach.getThoiGianBatDau().getTime() > System.currentTimeMillis()) {
				throw new RuntimeException("Chưa đến thời gian để đăng ký đề tài");
			} else if (keHoach.getThoiGianKetThuc().getTime() < System.currentTimeMillis()) {
				throw new RuntimeException("Thời gian đăng ký đề tài đã hết");
			}

			try {
				GiangVien giangVien = giangVienService.layTheoMa(maGiangVien);
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
				deTai.setMaDeTai("DT" + maDT);
				deTai.setGiangVien(giangVien);
				deTai.setHocKy(hocKy);
				deTai.setTrangThai(0);
				System.out.println("giang-vien-controller - them de tai - " + hocKy);

				DeTai ketQuaLuu = deTaiService.luu(deTai);

				return ketQuaLuu;
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

			return null;
		} else {
			throw new RuntimeException("Chưa có kế hoạch đăng ký đề tài");
		}

	}

	@DeleteMapping("/xoa-de-tai/{maDeTai}")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY')")
	public String xoaDeTai(@PathVariable String maDeTai) throws RuntimeException {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		List<KeHoach> keHoachs = keHoachService.layTheoTenVaMaHocKyVaiTro(hocKy.getMaHocKy(), "Lịch thêm đề tài",
				"ROLE_GIANGVIEN");
		if (keHoachs.size() > 0) {
			KeHoach keHoach = keHoachs.get(0);
			if (keHoach.getThoiGianBatDau().getTime() > System.currentTimeMillis()) {
				throw new RuntimeException("Chưa đến thời gian để đăng ký đề tài");
			} else if (keHoach.getThoiGianKetThuc().getTime() < System.currentTimeMillis()) {
				throw new RuntimeException("Thời gian đăng ký đề tài đã hết");
			}

			try {
				String ketQuaXoaDeTai = deTaiService.xoa(maDeTai);
				return ketQuaXoaDeTai;
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			return null;
		} else {
			throw new RuntimeException("Chưa có kế hoạch đăng ký đề tài");
		}

	}

	@PutMapping("/sua-de-tai")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY')")
	public DeTai suaDeTai(@RequestBody DeTai deTai) throws RuntimeException {

		HocKy hocKy1 = hocKyService.layHocKyCuoiCungTrongDS();
		List<KeHoach> keHoachs = keHoachService.layTheoTenVaMaHocKyVaiTro(hocKy1.getMaHocKy(), "Lịch thêm đề tài",
				"ROLE_GIANGVIEN");
		if (keHoachs.size() > 0) {
			KeHoach keHoach = keHoachs.get(0);
			if (keHoach.getThoiGianBatDau().getTime() > System.currentTimeMillis()) {
				throw new RuntimeException("Chưa đến thời gian để đăng ký đề tài");
			} else if (keHoach.getThoiGianKetThuc().getTime() < System.currentTimeMillis()) {
				throw new RuntimeException("Thời gian đăng ký đề tài đã hết");
			}

			try {
				HocKy hocKy = hocKyService.layTheoMa(deTai.getHocKy().getMaHocKy());
				deTai.setHocKy(hocKy);
				deTai.setTrangThai(0);
				DeTai ketQuaLuu = deTaiService.capNhat(deTai);

				return ketQuaLuu;
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

			return null;
		} else {
			throw new RuntimeException("Chưa có kế hoạch đăng ký đề tài");
		}

	}

	@PostMapping("/lay-ds-de-tai-theo-nam-hk")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY')")
	public List<DeTai> layDanhSachDeTaiTheoNamHocKyTrangThai(@RequestBody LayDeTaiRquestDto layDeTaiRquestDto) {
		try {

			List<DeTai> dsDeTai = new ArrayList<>();
			HocKy hocKy = new HocKy(layDeTaiRquestDto.getMaHocKy(), layDeTaiRquestDto.getSoHocKy(), null, null, null);
			if (hocKy.getMaHocKy() == null) {
				hocKy = hocKyService.layHocKyCuoiCungTrongDS();
			}
			if (layDeTaiRquestDto.getTrangThai() == null) {
				if (layDeTaiRquestDto.getMaGiangVien() != null) {
					dsDeTai = deTaiService.layDsDeTaiTheoNamHocKy(hocKy.getMaHocKy(), hocKy.getSoHocKy(),
							layDeTaiRquestDto.getMaGiangVien());
				} else {
					dsDeTai = deTaiService.layDsDeTaiTheoNamHocKy(hocKy.getMaHocKy(), hocKy.getSoHocKy());
				}

			} else {
				if (layDeTaiRquestDto.getMaGiangVien() != null) {
					dsDeTai = deTaiService.layDsDeTaiTheoNamHocKyTheoTrangThai(hocKy.getMaHocKy(), hocKy.getSoHocKy(),
							layDeTaiRquestDto.getMaGiangVien(), layDeTaiRquestDto.getTrangThai());
				} else {
					dsDeTai = deTaiService.layDsDeTaiTheoTrangThaiKhongMaGV(hocKy.getMaHocKy(), hocKy.getSoHocKy(),
							layDeTaiRquestDto.getTrangThai());
				}
			}

			return dsDeTai;
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping("/xem-de-tai-da-duyet")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> xemDsDeTaiDaDuocDuyet(@RequestBody LayDeTaiRquestDto layDeTaiRquestDto) {
		try {
			List<DeTaiDto> dsDeTaiDtos = new ArrayList<>();
			if (layDeTaiRquestDto.getMaGiangVien() == null) {
				List<DeTai> dsDeTai = deTaiService.layDsDeTaiTheoNamHocKyDaDuyet(layDeTaiRquestDto.getMaHocKy(),
						layDeTaiRquestDto.getSoHocKy());

				for (DeTai deTai : dsDeTai) {
					Integer soNhomDaDKDeTai = deTaiService.laySoNhomDaDangKyDeTai(deTai.getMaDeTai());
					dsDeTaiDtos.add(new DeTaiDto(deTai,
							soNhomDaDKDeTai == deTai.getGioiHanSoNhomThucHien() ? true : false, deTai.getGiangVien()));
				}
			} else {
				List<DeTai> dsDeTai = deTaiService.layDsDeTaiTheoNamHocKyTheoTrangThai(layDeTaiRquestDto.getMaHocKy(),
						layDeTaiRquestDto.getSoHocKy(), layDeTaiRquestDto.getMaGiangVien(),
						layDeTaiRquestDto.getTrangThai());

				for (DeTai deTai : dsDeTai) {
					Integer soNhomDaDKDeTai = deTaiService.laySoNhomDaDangKyDeTai(deTai.getMaDeTai());
					dsDeTaiDtos.add(new DeTaiDto(deTai,
							soNhomDaDKDeTai == deTai.getGioiHanSoNhomThucHien() ? true : false, deTai.getGiangVien()));
				}
			}

			return ResponseEntity.ok(dsDeTaiDtos);
		} catch (RuntimeException e) {
			e.printStackTrace();
			return ResponseEntity.ok("Have Error");
		}
	}

	@PostMapping("/dang-ky-de-tai")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN') or hasAuthority('ROLE_GIANGVIEN')")
	public ResponseEntity<?> dangKyDeTai(@RequestBody DangKyDeTaiRequest request) throws RuntimeException {
		HocKy hocKy1 = hocKyService.layHocKyCuoiCungTrongDS();
		List<KeHoach> keHoachs = keHoachService.layTheoTenVaMaHocKyVaiTro(hocKy1.getMaHocKy(), "Lịch đăng ký đề tài",
				request.getVaiTro());
		keHoachs.addAll(keHoachService.layTheoTenVaMaHocKyVaiTro(hocKy1.getMaHocKy(), "Lịch gán đề tài cho nhóm",
				request.getVaiTro()));
		if (keHoachs.size() > 0) {
			for (KeHoach keHoach : keHoachs) {
				if (keHoach.getThoiGianBatDau().getTime() > System.currentTimeMillis()) {
					throw new RuntimeException("Chưa đến thời gian để đăng ký đề tài");
				} else if (keHoach.getThoiGianKetThuc().getTime() < System.currentTimeMillis()) {
					throw new RuntimeException("Thời gian đăng ký đề tài đã hết");
				}
			}
			try {
				producer.sendMessageOnDeTaiChanel(request);
				return listenerConsumer.listenerDeTaiChannel();
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}

		} else {
			throw new RuntimeException("Chưa có kế hoạch đăng ký đề tài");
		}
	}

	@PostMapping("/them-de-tai-excel/{maGiangVien}")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY')")
	public ResponseEntity<?> importExcel(@PathVariable("maGiangVien") String maGiangVien,
			@RequestPart("file") MultipartFile file) {
		if (DeTaiImporter.isValidExcelFile(file)) {
			try {

				GiangVien giangVien = giangVienService.layTheoMa(maGiangVien);
				List<DeTai> deTais = deTaiImporter.addDataFDromExcel(file.getInputStream(), giangVien);
				deTaiService.luuDanhSach(deTais);
				return ResponseEntity.ok(deTais);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return null;
	}
	
	@GetMapping("/lay-theo-ma/{maDeTai}")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY')")
	public ResponseEntity<?> layDeTaiTheoMa(@PathVariable("maDeTai") String maDeTai) {
		System.out.println(maDeTai);
		DeTai deTai1 = deTaiService.layTheoMa(maDeTai.trim());
//		DeTaiRes deTai = new DeTaiRes(deTai1.getMaDeTai(), deTai1.getTenDeTai(), deTai1.getMucTieuDeTai(),deTai1.getSanPhamDuKien(), 
//				deTai1.getMoTa(), deTai1.getYeuCauDauVao(), deTai1.getTrangThai(),deTai1.getGioiHanSoNhomThucHien(), deTai1.getGiangVien(), deTai1.getHocKy().getMaHocKy());
		return ResponseEntity.ok(deTai1);
	}
	
	@GetMapping("/xuat-file-mau-de-tai")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN')")
	public void xuatFileMauDeTai(HttpServletResponse response) throws RuntimeException {
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=ketqua_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		FileMauExcelDeTai fileMauExcelDeTai = new FileMauExcelDeTai();
		try {
			fileMauExcelDeTai.export(response);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
}
