package com.iuh.backendkltn32.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iuh.backendkltn32.dto.LayDeTaiRquestDto;
import com.iuh.backendkltn32.dto.LoginRequest;
import com.iuh.backendkltn32.dto.SinhVienDto;
import com.iuh.backendkltn32.entity.LopDanhNghia;
import com.iuh.backendkltn32.entity.LopHocPhan;
import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.entity.TaiKhoan;
import com.iuh.backendkltn32.importer.DeTaiImporter;
import com.iuh.backendkltn32.importer.SinhVienImporter;
import com.iuh.backendkltn32.service.LopDanhNghiaService;
import com.iuh.backendkltn32.service.LopHocPhanService;
import com.iuh.backendkltn32.service.NhomService;
import com.iuh.backendkltn32.service.SinhVienService;
import com.iuh.backendkltn32.service.TaiKhoanService;
import com.iuh.backendkltn32.service.VaiTroService;

@RestController
@RequestMapping("/api/sinh-vien")
public class SinhVienController {

	@Autowired
	private SinhVienService sinhVienService;

	@Autowired
	private VaiTroService vaiTroService;
	
	@Autowired
	private TaiKhoanService taiKhoanService;
	
	@Autowired
	private NhomService nhomService;

	@Autowired
	private LopDanhNghiaService lopDanhNghiaService;
	
	@Autowired
	private SinhVienImporter sinhVienImporter;
	
	@Autowired
	private LopHocPhanService lopHocPhanService;
	
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
	
	@PostMapping("/them-sinh-vien")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public SinhVien themSinhVienVaoHeThong(@RequestBody SinhVienDto sinhVien) {

		try {
			
			LopDanhNghia lopDanhNghia = lopDanhNghiaService.layTheoMa(sinhVien.getMaLopDanhNghia());
			
			LopHocPhan lopHocPhan = lopHocPhanService.layTheoMa(sinhVien.getMaLopHocPhan());
			
			SinhVien sinhVien2 = new SinhVien(sinhVien.getMaSinhVien(), sinhVien.getTenSinhVien(), 
					sinhVien.getNoiSinh(), sinhVien.getDienThoai(), sinhVien.getEmail(), sinhVien.getNgaySinh(), 
					sinhVien.getNamNhapHoc(), sinhVien.getGioiTinh(), sinhVien.getAnhDaiDien(), lopDanhNghia, lopHocPhan);
			SinhVien ketQuaLuuSinhVien = sinhVienService.luu(sinhVien2);
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

			TaiKhoan taiKhoan = new TaiKhoan(sinhVien.getMaSinhVien(), encoder.encode("1111"),
					vaiTroService.layTheoMa(3L));

			taiKhoanService.luu(taiKhoan);
			return ketQuaLuuSinhVien;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	@PostMapping("/them-sinh-vien-excel")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public ResponseEntity<?> themSinhVienExcel(@RequestParam("file") MultipartFile file) throws Exception {
		if (DeTaiImporter.isValidExcelFile(file)) {
			try {
				
				List<SinhVien> sinhViens = sinhVienImporter.addDataFDromExcel(file.getInputStream());
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				sinhVienService.luuDanhSach(sinhViens).stream().forEach(sv-> {
					try {
						TaiKhoan taiKhoan = new TaiKhoan(sv.getMaSinhVien(), encoder.encode("1111"),
								vaiTroService.layTheoMa(3L));
						taiKhoanService.luu(taiKhoan);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				
				return ResponseEntity.ok(sinhViens);
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(500).body("Have Error");
			}
		}
		return null;
	}
	
	@GetMapping("/xem-thong-tin/{maSinhVien}")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public SinhVien hienThiThongTinCaNhan(@PathVariable("maSinhVien") String maSinhVien) {
		try {
			SinhVien sinhVien = sinhVienService.layTheoMa(maSinhVien);

			return sinhVien;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
