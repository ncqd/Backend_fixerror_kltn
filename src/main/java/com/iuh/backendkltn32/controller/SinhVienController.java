package com.iuh.backendkltn32.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.dto.DangKyDeTaiRequest;
import com.iuh.backendkltn32.dto.DangKyNhomRequest;
import com.iuh.backendkltn32.dto.DeTaiDto;
import com.iuh.backendkltn32.dto.LoginRequest;
import com.iuh.backendkltn32.dto.NhomSinhVienDto;
import com.iuh.backendkltn32.entity.DeTai;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.jms.JmsListenerConsumer;
import com.iuh.backendkltn32.jms.JmsPublishProducer;
import com.iuh.backendkltn32.service.DeTaiService;
import com.iuh.backendkltn32.service.HocKyService;
import com.iuh.backendkltn32.service.NhomService;
import com.iuh.backendkltn32.service.SinhVienService;
import com.iuh.backendkltn32.utils.Constants;

@RestController
@RequestMapping("/api/sinh-vien")
public class SinhVienController {

	@Autowired
	private SinhVienService sinhVienService;

	@Autowired
	private DeTaiService deTaiService;

	@Autowired
	private NhomService nhomService;

	@Autowired
	private JmsPublishProducer producer;

	@Autowired
	private HocKyService hocKyService;
	
	@Autowired
	private JmsListenerConsumer listenerConsumer;

	@GetMapping("/thong-tin-ca-nhan/{maSinhVien}")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public SinhVien hienThiThongTinCaNhan(@PathVariable String maSinhVien, @RequestBody LoginRequest loginRequest) {
		try {
			if (!loginRequest.getTenTaiKhoan().equals(maSinhVien)) {
				throw new Exception("Khong Dung Ma Sinh Vien");
			}
			SinhVien sinhVien = sinhVienService.layTheoMa(maSinhVien);

			return sinhVien;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping("/xem-de-tai-da-duyet/{maHocKy}/{soHocKy}")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> xemDsDeTaiDaDuocDuyet(@PathVariable("maHocKy") String maHocKy,@PathVariable("soHocKy") String soHocKy) {
		try {
			List<DeTai> dsDeTai = deTaiService.layDsDeTaiTheoNamHocKyDaDuyet(maHocKy, soHocKy);

			List<DeTaiDto> dsDeTaiDtos = new ArrayList<>();

			for (DeTai deTai : dsDeTai) {
				Integer soNhomDaDKDeTai = deTaiService.laySoNhomDaDangKyDeTai(deTai.getMaDeTai());
				dsDeTaiDtos
						.add(new DeTaiDto(deTai, soNhomDaDKDeTai == deTai.getGioiHanSoNhomThucHien() ? true : false));
			}

			return ResponseEntity.ok(dsDeTai);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.ok("Have Error");
		}
	}

	@GetMapping("/thong-tin-tong-sinh-vien/{namHocKy}")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> dsSinhVienTrongHocKy(@PathVariable("namHocKy") String namHocKy) {

		Integer hocKy = Integer.parseInt(namHocKy.substring(2, 3));
		String namHoc = namHocKy.substring(10, 14);

		System.out.println("SinhVienController - dsSinhVienTrongHocKy - " + namHoc);

		List<Nhom> nhoms = nhomService.layTatCaNhom(hocKy, namHoc);

		if (nhoms.isEmpty()) {
			return ResponseEntity.ok(new ArrayList<>());
		}

		List<NhomSinhVienDto> nhomSinhVien = new ArrayList<>();

		for (Nhom nhom : nhoms) {
			List<String> sinhViens = sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom());
			NhomSinhVienDto nhomSinhVienTemp = new NhomSinhVienDto(nhom.getMaNhom(), sinhViens, nhom.getTinhTrang());
			nhomSinhVien.add(nhomSinhVienTemp);
		}

		return ResponseEntity.ok(nhomSinhVien);

	}

	/*
	 * // case1: tao moi -> check: co nhom chua ???? // TH1: -> chua co //TH2: -> co
	 * roi -> XONG XONG XONG
	 */
	@PostMapping("/dang-ky-nhom")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> dangKyNhom(@RequestBody DangKyNhomRequest request) throws Exception {
		producer.sendMessageOnNhomChanel(request);
		return listenerConsumer.listenerNhomChannel();
	}

	@PostMapping("/roi-nhom")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> roiKhoiNhomDaDK(@RequestBody DangKyNhomRequest request) {
		try {
			SinhVien sinhVien = sinhVienService.layTheoMa(request.getDsMaSinhVien().get(0));
			if (sinhVien.getNhom() == null) {
				return ResponseEntity.status(500).body("Chua co nhom de roi");
			}
			if (request.getMaNhom() == null) {
				return ResponseEntity.status(500).body("Ma nhom bang null");
			}
			sinhVien.setNhom(null);
			sinhVienService.capNhat(sinhVien);
			if (nhomService.laySoSinhVienTrongNhomTheoMa(request.getMaNhom()) == null) {
				nhomService.xoa(request.getMaNhom());
			}
			return ResponseEntity.ok(sinhVien);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(e.getMessage());
		}
	}

	private String taoMaMoi() {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		Nhom nhom = nhomService
				.layNhomTheoThoiGianHienThuc(Constants.NHOM + "" + hocKy.getMaHocKy() + hocKy.getSoHocKy());
		if (nhom != null) {
			Integer soNhomHienHanh = Integer.parseInt(nhom.getMaNhom().substring(5)) + 1;
			String maNhom = null;
			if (soNhomHienHanh < 9) {
				maNhom = "00" + soNhomHienHanh;
			} else if (soNhomHienHanh >= 9 && soNhomHienHanh < 100) {
				maNhom = "0" + soNhomHienHanh;
			} else {
				maNhom = "" + soNhomHienHanh;
			}

			return Constants.NHOM + "" + hocKy.getMaHocKy() + hocKy.getSoHocKy() + maNhom;
		}
		return Constants.NHOM + "" + hocKy.getMaHocKy() + hocKy.getSoHocKy() + "001";
	}

	@PostMapping("/dang-ky-de-tai")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> dangKyDeTai(@RequestBody DangKyDeTaiRequest request) {

		try {
			producer.sendMessageOnDeTaiChanel(request);

			return ResponseEntity.ok(null);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok("Have Error");
		}

	}
	
	@GetMapping("/xem-cac-nhom/{hocKy}/{namHoc}")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> xemCacNhom(@PathVariable("hocKy") String hocKy, @PathVariable("namHoc") String namHoc) {

		try {
			Integer soHocKy = Integer.parseInt(hocKy);
			System.out.println("SinhVienController - xemCacNhom - " + hocKy);
			List<Nhom> nhoms = nhomService.layTatCaNhom(soHocKy, namHoc);

			return ResponseEntity.ok(nhoms);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok("Have Error");
		}

	}

}
