package com.iuh.backendkltn32.controller;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.iuh.backendkltn32.dto.KetQuaHocTapDto;
import com.iuh.backendkltn32.dto.LayDeTaiRquestDto;
import com.iuh.backendkltn32.dto.LoginRequest;
import com.iuh.backendkltn32.dto.LopHocPhanDto;
import com.iuh.backendkltn32.dto.SinhVienDto;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.LopDanhNghia;
import com.iuh.backendkltn32.entity.LopHocPhan;
import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.PhieuCham;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.entity.TaiKhoan;
import com.iuh.backendkltn32.importer.DeTaiImporter;
import com.iuh.backendkltn32.importer.SinhVienImporter;
import com.iuh.backendkltn32.service.HocKyService;
import com.iuh.backendkltn32.service.LopDanhNghiaService;
import com.iuh.backendkltn32.service.LopHocPhanService;
import com.iuh.backendkltn32.service.NhomService;
import com.iuh.backendkltn32.service.PhieuChamService;
import com.iuh.backendkltn32.service.QuanLyBoMonService;
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
	
	@Autowired
	private HocKyService hocKyService;
	
	@Autowired
	private PhieuChamService phieuChamService;
	
	@Autowired
	private QuanLyBoMonService quanLyBoMonService;
	
	@GetMapping("/thong-tin-ca-nhan/{maSinhVien}")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public SinhVien hienThiThongTinCaNhan(@PathVariable String maSinhVien, @RequestBody LoginRequest loginRequest) {
		try {
			if (!loginRequest.getTenTaiKhoan().equals(maSinhVien)) {
				throw new RuntimeException("Khong Dung Ma Sinh Vien");
			}

			return sinhVienService.layTheoMa(maSinhVien);
		} catch (RuntimeException e) {
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
		} catch (RuntimeException e) {
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
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	@PostMapping("/them-sinh-vien-excel")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public ResponseEntity<?> themSinhVienExcel(@RequestParam("file") MultipartFile file) throws RuntimeException {
		if (DeTaiImporter.isValidExcelFile(file)) {
			try {
				List<SinhVien> sinhViens = sinhVienImporter.addDataFDromExcel(file.getInputStream());
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				for (SinhVien sv : sinhVienService.luuDanhSach(sinhViens)) {
					try {
						TaiKhoan taiKhoan = new TaiKhoan(sv.getMaSinhVien(), encoder.encode("1111"),
						vaiTroService.layTheoMa(3L));
						taiKhoanService.luu(taiKhoan);
					} catch (RuntimeException e) {
						e.printStackTrace();
					}
				}

				return ResponseEntity.ok(sinhViens);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return null;
	}
	
	@GetMapping("/xem-thong-tin/{maSinhVien}")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public SinhVien hienThiThongTinCaNhan(@PathVariable("maSinhVien") String maSinhVien) {
		try {

			return sinhVienService.layTheoMa(maSinhVien);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping("/lay-sinh-vien-lop")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public List<SinhVien> sinhViens(@RequestBody LopHocPhanDto lopHocPhanDto) throws RuntimeException {
		if (lopHocPhanDto.getMaHocKy() == null) {
			if (lopHocPhanDto.getMaLopHocPhan() == null) {
				
				return sinhVienService.layTatCaSinhVien();
			}
			return sinhVienService.layTatCaSinhVienTheoLopHocPhan(lopHocPhanService.layTheoMa(lopHocPhanDto.getMaLopHocPhan()));
		} else {
			if (lopHocPhanDto.getMaLopHocPhan() == null) {
				HocKy hocKy = hocKyService.layTheoMa(lopHocPhanDto.getMaHocKy());
				return sinhVienService.layTatCaSinhVienTheoHocKy(hocKy.getMaHocKy(), hocKy.getSoHocKy());
			}
			return sinhVienService.layTatCaSinhVienTheoLopHocPhan(lopHocPhanService.layTheoMa(lopHocPhanDto.getMaLopHocPhan()));
		}
	}
	
	@GetMapping("/lay-ket-qua/{maSinhVien}")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN') or hasAuthority('ROLE_QUANLY') ")
	public ResponseEntity<?> layDiemCuoiCung(@PathVariable("maSinhVien") String maSinhVien) throws RuntimeException {
		
		SinhVien sv = sinhVienService.layTheoMa(maSinhVien);
		if (sv.getLopHocPhan().getHocPhanKhoaLuanTotNghiep().getHocKy().getChoXemDiem()) {
			if (phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(maSinhVien, "CT").size() <= 0) {
				return ResponseEntity.ok(Arrays.asList(new KetQuaHocTapDto(sv.getLopHocPhan().getMaLopHocPhan(), "Khóa luận tốt nghiệp", "5", null, null, null)));
			}
			PhieuCham phieuChamHD1SV1 = phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(maSinhVien, "HD").get(0);
			PhieuCham phieuChamHoiDong1SV1 = phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(maSinhVien, "CT").get(0);
			PhieuCham phieuChamHoiDong2SV1 = phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(maSinhVien, "TK").get(0);

			Double diemPB = (double) 0;
			diemPB += phieuChamHD1SV1.getDiemPhieuCham();

			for (PhieuCham pc : phieuChamService.layPhieuTheoMaSinhVienTenVaiTro(maSinhVien, "PB")) {
				diemPB += pc.getDiemPhieuCham();
			}
			diemPB = diemPB / 3;

			Double diemHD = (phieuChamHoiDong1SV1.getDiemPhieuCham() + phieuChamHoiDong2SV1.getDiemPhieuCham()) / 2;

			Double kq = (double) (Math.round(((diemHD + diemPB) / 2) * 10)) / 10;
			
			Double diemThang4 = (double) 0;

			String diemChu = "";

			if (kq >= 9 && kq <= 10) {
				diemThang4 =  4.0;
				diemChu = "A+";
			} else if (kq >= 8.5 && kq <= 8.9) {
				diemThang4 =  3.8;
				diemChu = "A";
			} else if (kq >= 8.0 && kq <= 8.4) {
				diemThang4 =  3.5;
				diemChu = "B+";
			} else if (kq >= 7.0 && kq <= 7.9) {
				diemThang4 =  3.0;
				diemChu = "B";
			} else if (kq >= 6.0 && kq <= 8.9) {
				diemThang4 =  2.5;
				diemChu = "C+";
			} else if (kq >= 5.5 && kq <= 5.9) {
				diemThang4 =  2.0;
				diemChu = "C";
			} else if (kq >= 5.0 && kq <= 5.4) {
				diemThang4 =  1.5;
				diemChu = "D+";
			} else if (kq >= 4.0 && kq <= 4.9) {
				diemThang4 =  1.0;
				diemChu = "D";
			}  else {
				diemThang4 =  0.0;
				diemChu = "F";
			}
			
			
			return ResponseEntity.ok(Arrays.asList(new KetQuaHocTapDto(sv.getLopHocPhan().getMaLopHocPhan(), "Khóa luận tốt nghiệp", "5", kq, diemThang4, diemChu)));
		}
		return ResponseEntity.ok(new ArrayList<>());
		
	}
	
	
}
