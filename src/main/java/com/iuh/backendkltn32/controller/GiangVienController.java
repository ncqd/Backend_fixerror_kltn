package com.iuh.backendkltn32.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iuh.backendkltn32.dto.GiangVienDto;
import com.iuh.backendkltn32.dto.GiangVienDtoChuaCoSoDTTT;
import com.iuh.backendkltn32.dto.LoginRequest;
import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.Khoa;
import com.iuh.backendkltn32.entity.TaiKhoan;
import com.iuh.backendkltn32.importer.DeTaiImporter;
import com.iuh.backendkltn32.importer.GiangVienImporter;
import com.iuh.backendkltn32.service.GiangVienService;
import com.iuh.backendkltn32.service.HocKyService;
import com.iuh.backendkltn32.service.KhoaService;
import com.iuh.backendkltn32.service.TaiKhoanService;
import com.iuh.backendkltn32.service.VaiTroService;

@RestController
@RequestMapping("/api/giang-vien")
public class GiangVienController {

	@Autowired
	private TaiKhoanService taiKhoanService;

	@Autowired
	private VaiTroService vaiTroService;

	@Autowired
	private KhoaService khoaService;

	@Autowired
	private GiangVienService giangVienService;

	@Autowired
	private GiangVienImporter giangVienImporter;
	
	@Autowired
	private HocKyService hocKyService;

	@GetMapping("/thong-tin-ca-nhan/{maGiangVien}")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN')")
	public GiangVien hienThiThongTinCaNhan(@PathVariable String maGiangVien, @RequestBody LoginRequest loginRequest) {
		try {
			if (!loginRequest.getTenTaiKhoan().equals(maGiangVien)) {
				throw new RuntimeException("Khong Dung Ma Giang Vien");
			}
			GiangVien giangVien = giangVienService.layTheoMa(maGiangVien);

			return giangVien;
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping("/them-giang-vien")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public GiangVien themSGiangVienVaoHeThong(@RequestBody GiangVienDto giangVien) {

		try {
			Khoa khoa = khoaService.layTheoMa(giangVien.getMaKhoa());
			GiangVien giangVien2 = new GiangVien(giangVien.getMaGiangVien(), giangVien.getTenGiangVien(),
					giangVien.getSoDienThoai(), giangVien.getEmail(), giangVien.getCmnd(), giangVien.getHocVi(),
					giangVien.getNgaySinh(), giangVien.getNamCongTac(), khoa, giangVien.getAnhDaiDien(),
					giangVien.getGioiTinh());
			GiangVien ketQuaLuuGiangVien = giangVienService.luu(giangVien2);
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

			TaiKhoan taiKhoan = new TaiKhoan(giangVien.getMaGiangVien(), encoder.encode("1111"),
					vaiTroService.layTheoMa(2L));

			taiKhoanService.luu(taiKhoan);
			return ketQuaLuuGiangVien;
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;

	}

	@PutMapping("/cap-nhat-giang-vien")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public GiangVien caNhatGiangVienVaoHeThong(@RequestBody GiangVienDto giangVien) {

		try {
			Khoa khoa = khoaService.layTheoMa(giangVien.getMaKhoa());
			GiangVien giangVien2 = new GiangVien(giangVien.getMaGiangVien(), giangVien.getTenGiangVien(),
					giangVien.getSoDienThoai(), giangVien.getEmail(), giangVien.getCmnd(), giangVien.getHocVi(),
					giangVien.getNgaySinh(), giangVien.getNamCongTac(), khoa, giangVien.getAnhDaiDien(),
					giangVien.getGioiTinh());
			GiangVien ketQuaLuuGiangVien = giangVienService.capNhat(giangVien2);

			return ketQuaLuuGiangVien;
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;

	}

	@PostMapping("/them-giang-vien-excel")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public ResponseEntity<?> themGiangVienExcel(@RequestParam("file") MultipartFile file) throws RuntimeException {
		if (DeTaiImporter.isValidExcelFile(file)) {
			try {

				List<GiangVien> giangViens = giangVienImporter.addDataFDromExcel(file.getInputStream());
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				giangVienService.luuDanhSach(giangViens).stream().forEach(gv -> {
					try {
						TaiKhoan taiKhoan = new TaiKhoan(gv.getMaGiangVien(), encoder.encode("1111"),
								vaiTroService.layTheoMa(2L));
						taiKhoanService.luu(taiKhoan);
					} catch (Exception e) {
						throw new RuntimeException(e.getMessage());
					}
				});
				return ResponseEntity.ok(giangViens);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return null;
	}
	
}
