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
import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.SinhVien;
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

	@GetMapping("/xem-de-tai-da-duyet")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> xemDsDeTaiDaDuocDuyet() {
		try {
			List<DeTai> dsDeTai = deTaiService.layDsDeTaiTheoNamHocKyDaDuyet("HK2 (2022-2023)");

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

		System.out.println("SinhVienControllẻ - dsSinhVienTrongHocKy - " + namHoc);

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

	@PostMapping("/dang-ky-nhom")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> dangKyNhom(@RequestBody DangKyNhomRequest request) {
		try {

			SinhVien sv1 = sinhVienService.layTheoMa(request.getMaSinhVien1());

			SinhVien sv2 = null;
			String maSv2 = request.getMaSinhVien2();

			if (maSv2 != null && !maSv2.isEmpty() && !maSv2.equals("")) {

				sv2 = sinhVienService.layTheoMa(request.getMaSinhVien2());
				if (sv1.getNhom() != null || sv2.getNhom() != null) {
					return ResponseEntity.ok("Khong the dang ky nhom");
				}

				List<Nhom> dsNhom = nhomService.layTatCaNhom(request.getHocKy(), request.getNamHoc());
				Integer maNhomMoi = 1;
				if (!dsNhom.isEmpty() || dsNhom != null) {
					maNhomMoi = dsNhom.size() + 1;
				}

				Nhom nhom = nhomService.luu(new Nhom(maNhomMoi + "", "Nhom " + maNhomMoi, null, sv2 == null ? 0 : 1));
				sv1.setNhom(nhom);
				sinhVienService.capNhat(sv1);
				sv2.setNhom(nhom);

				sinhVienService.capNhat(sv2);
				return ResponseEntity.ok(nhomService.layTheoMa(maNhomMoi + ""));
			}
			if (sv1.getNhom() != null) {
				return ResponseEntity.ok("Khong the dang ky nhom");
			}

			List<Nhom> dsNhom = nhomService.layTatCaNhom(request.getHocKy(), request.getNamHoc());
			Integer maNhomMoi = 1;
			if (!dsNhom.isEmpty() || dsNhom != null) {
				maNhomMoi = dsNhom.size() + 1;
			}
			Nhom nhom = nhomService.luu(new Nhom(maNhomMoi + "", "Nhom " + maNhomMoi, null, sv2 == null ? 0 : 1));
			sv1.setNhom(nhom);
			sinhVienService.capNhat(sv1);
			return ResponseEntity.ok(nhomService.layTheoMa(maNhomMoi + ""));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok("Have Error");
		}
	}

	@PostMapping("/dang-ky-de-tai")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> dangKyDeTai(@RequestBody DangKyDeTaiRequest request) {

		try {

			DeTai deTai = deTaiService.layTheoMa(request.getMaDeTai());
			Integer soNhomDaDKDeTai = deTaiService.laySoNhomDaDangKyDeTai(request.getMaDeTai());

			if (soNhomDaDKDeTai >= deTai.getGioiHanSoNhomThucHien()) {
				return ResponseEntity.ok("Khong the dang ky de tai nay");
			}

			Nhom nhomDangKy = nhomService.layTheoMa(request.getMaNhom());
			nhomDangKy.setDeTai(deTai);
			nhomService.capNhat(nhomDangKy);

			return ResponseEntity.ok(nhomService.layTheoMa(request.getMaNhom()));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.ok("Have Error");
		}

	}

}
