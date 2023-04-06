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
import com.iuh.backendkltn32.dto.LayDeTaiRquestDto;
import com.iuh.backendkltn32.dto.LoginRequest;
import com.iuh.backendkltn32.entity.DeTai;
import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.jms.JmsPublishProducer;
import com.iuh.backendkltn32.service.DeTaiService;
import com.iuh.backendkltn32.service.NhomService;
import com.iuh.backendkltn32.service.SinhVienService;

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

//	@Autowired
//	private HocKyService hocKyService;
	
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
	
	@GetMapping("/xem-cac-nhom")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> xemCacNhom(LayDeTaiRquestDto request) {

		try {
			System.out.println("SinhVienController - xemCacNhom - " + request.getMaHocKy() + request.getSoHocKy());
			List<Nhom> nhoms = nhomService.layTatCaNhom(request.getMaHocKy() , request.getSoHocKy());

			return ResponseEntity.ok(nhoms);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok("Have Error");
		}

	}
}
