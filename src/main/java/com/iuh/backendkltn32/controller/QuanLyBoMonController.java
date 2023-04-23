package com.iuh.backendkltn32.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iuh.backendkltn32.dto.DuyetRequest;
import com.iuh.backendkltn32.dto.GiangVienDto;
import com.iuh.backendkltn32.dto.LapKeHoachDto;
import com.iuh.backendkltn32.dto.LayDeTaiRquestDto;
import com.iuh.backendkltn32.dto.LoginRequest;
import com.iuh.backendkltn32.dto.PhanCongDto;
import com.iuh.backendkltn32.dto.SinhVienDto;
import com.iuh.backendkltn32.entity.DeTai;
import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.KeHoach;
import com.iuh.backendkltn32.entity.Khoa;
import com.iuh.backendkltn32.entity.LopDanhNghia;
import com.iuh.backendkltn32.entity.LopHocPhan;
import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.PhanCong;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.entity.TaiKhoan;
import com.iuh.backendkltn32.excel.DeTaiImporter;
import com.iuh.backendkltn32.excel.GiangVienImporter;
import com.iuh.backendkltn32.excel.SinhVienImporter;
import com.iuh.backendkltn32.export.SinhVienExcelExporoter;
import com.iuh.backendkltn32.service.DeTaiService;
import com.iuh.backendkltn32.service.GiangVienService;
import com.iuh.backendkltn32.service.KeHoachService;
import com.iuh.backendkltn32.service.KhoaService;
import com.iuh.backendkltn32.service.LopDanhNghiaService;
import com.iuh.backendkltn32.service.LopHocPhanService;
import com.iuh.backendkltn32.service.NhomService;
import com.iuh.backendkltn32.service.PhanCongService;
import com.iuh.backendkltn32.service.SinhVienService;
import com.iuh.backendkltn32.service.TaiKhoanService;
import com.iuh.backendkltn32.service.VaiTroService;
import com.iuh.backendkltn32.service.impl.GiangVienServiceImpl;

@RestController
@RequestMapping("/api/quan-ly")
public class QuanLyBoMonController {

	@Autowired
	private SinhVienService sinhVienService;

	@Autowired
	private TaiKhoanService taiKhoanService;

	@Autowired
	private VaiTroService vaiTroService;

	@Autowired
	private GiangVienService giangVienService;

	@Autowired
	private KhoaService khoaService;

	@Autowired
	private DeTaiService deTaiService;

	@Autowired
	private NhomService nhomService;

	@Autowired
	private KeHoachService keHoachService;
	
	@Autowired
	private LopDanhNghiaService lopDanhNghiaService;
	
	@Autowired
	private LopHocPhanService lopHocPhanService;
	
	@Autowired
	private SinhVienImporter sinhVienImporter;
	
	@Autowired
	private GiangVienImporter giangVienImporter;
	
	@Autowired
	private PhanCongService phanCongService;
	
	@GetMapping("/thong-tin-ca-nhan/{maQuanLy}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public GiangVien hienThiThongTinCaNhan(@PathVariable String maQuanLy, @RequestBody LoginRequest loginRequest) {
		try {
			if (!loginRequest.getTenTaiKhoan().equals(maQuanLy)) {
				throw new Exception("Khong Dung Ma Giang Vien");
			}
			GiangVien giangVien = giangVienService.layTheoMa(maQuanLy);

			return giangVien;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
					vaiTroService.layTheoMa(4L));

			taiKhoanService.luu(taiKhoan);
			return ketQuaLuuSinhVien;
		} catch (Exception e) {
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
					giangVien.getSoDienThoai(), giangVien.getEmail(), giangVien.getCmnd(), giangVien.getHocVi(), giangVien.getNgaySinh(), 
					giangVien.getNamCongTac(), khoa, giangVien.getAnhDaiDien(), giangVien.getGioiTinh() );
			GiangVien ketQuaLuuGiangVien = giangVienService.luu(giangVien2);
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

			TaiKhoan taiKhoan = new TaiKhoan(giangVien.getMaGiangVien(), encoder.encode("1111"),
					vaiTroService.layTheoMa(2L));

			taiKhoanService.luu(taiKhoan);
			return ketQuaLuuGiangVien;
		} catch (Exception e) {
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
					giangVien.getSoDienThoai(), giangVien.getEmail(), giangVien.getCmnd(), giangVien.getHocVi(), giangVien.getNgaySinh(), 
					giangVien.getNamCongTac(), khoa, giangVien.getAnhDaiDien(), giangVien.getGioiTinh() );
			GiangVien ketQuaLuuGiangVien = giangVienService.capNhat(giangVien2);
			
			return ketQuaLuuGiangVien;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@GetMapping("/xuat-ds-sinhvien")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=danh-sach-sinh-vien_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		List<SinhVien> dsSinhVien = sinhVienService.layTatCaSinhVien();

		SinhVienExcelExporoter excelExporter = new SinhVienExcelExporoter(dsSinhVien);

		excelExporter.export(response);
	}

	@PostMapping("/duyet-de-tai-theo-nam-hk")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public DeTai pheDuyetDeTai(@RequestBody DuyetRequest duyetDeTaiRequest) {
		try {
			DeTai deTai = deTaiService.layTheoMa(duyetDeTaiRequest.getMa());
			deTai.setTrangThai(duyetDeTaiRequest.getTrangThai());
			deTaiService.capNhat(deTai);
			return deTai;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@PostMapping("/duyet-nhom-theo-nam-hk")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public Nhom pheDuyetNhom(@RequestBody DuyetRequest duyetNhomRequest) {
		try {
			Nhom nhom = nhomService.layTheoMa(duyetNhomRequest.getMa());

			nhom.setTinhTrang(duyetNhomRequest.getTrangThai());
			nhomService.capNhat(nhom);
			return nhom;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@PostMapping("/them-ke-hoach")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public LapKeHoachDto themKeHoach(@RequestBody LapKeHoachDto lapKeHoachDto) throws Exception {
		KeHoach keHoach = new KeHoach(lapKeHoachDto.getTenKeHoach(), lapKeHoachDto.getChuThich(),
				lapKeHoachDto.getDsNgayThucHienKhoaLuan() != null ? lapKeHoachDto.getDsNgayThucHienKhoaLuan().toString()
						.substring(1, lapKeHoachDto.getDsNgayThucHienKhoaLuan().toString().length() - 1) : null,
				lapKeHoachDto.getHocKy(), lapKeHoachDto.getThoiGianBatDau() , lapKeHoachDto.getThoiGianKetThuc(),
				lapKeHoachDto.getTinhTrang(), lapKeHoachDto.getVaiTro(), lapKeHoachDto.getMaNguoiDung());
		keHoachService.luu(keHoach);
		return lapKeHoachDto;
	}

	@PutMapping("/cap-nhat-ke-hoach")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public LapKeHoachDto capNhatKeHoach(@RequestBody LapKeHoachDto lapKeHoachDto) throws Exception {
		KeHoach keHoach = new KeHoach(lapKeHoachDto.getId(), lapKeHoachDto.getTenKeHoach(), lapKeHoachDto.getChuThich(),
				lapKeHoachDto.getDsNgayThucHienKhoaLuan() != null ? lapKeHoachDto.getDsNgayThucHienKhoaLuan().toString()
						.substring(1, lapKeHoachDto.getDsNgayThucHienKhoaLuan().toString().length() - 1) : null,
				lapKeHoachDto.getHocKy(), lapKeHoachDto.getThoiGianBatDau(), lapKeHoachDto.getThoiGianKetThuc(),
				lapKeHoachDto.getTinhTrang(), lapKeHoachDto.getVaiTro(), lapKeHoachDto.getMaNguoiDung());
		keHoachService.capNhat(keHoach);
		return lapKeHoachDto;
	}

	@DeleteMapping("/xoa-ke-hoach/{maKeHoach}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public String xoaKeHoach(@PathVariable String maKeHoach) throws Exception {
		return keHoachService.xoa(maKeHoach);
	}

	@GetMapping("/lay-ke-hoach/{maKeHoach}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public LapKeHoachDto layKeHoachTheoMa(@PathVariable String maKeHoach) throws Exception {
		KeHoach kh = keHoachService.layTheoMa(maKeHoach);
		String[] ngayThucHienKL = kh.getDsNgayThucHienKhoaLuan() != null ? kh.getDsNgayThucHienKhoaLuan().split(",\\s") : new String[0];
		LapKeHoachDto lapKeHoachDto = new LapKeHoachDto(kh.getId(), kh.getTenKeHoach(), kh.getChuThich(),
				Arrays.asList(ngayThucHienKL), kh.getHocKy(), new Timestamp(kh.getThoiGianBatDau().getTime()), new Timestamp(kh.getThoiGianKetThuc().getTime()),
				kh.getTinhTrang(), kh.getVaiTro(), kh.getMaNguoiDung());
		return lapKeHoachDto;
	}

	@GetMapping("/lay-ke-hoach-theo-hocky/{maHocKy}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public List<LapKeHoachDto> layKeHoachTheoHk(@PathVariable String maHocKy) throws Exception {
		List<LapKeHoachDto> ds = new ArrayList<>();
		keHoachService.layKeHoachTheoMaHocKy(maHocKy).stream().forEach(kh -> {
			
			String[] ngayThucHienKL = kh.getDsNgayThucHienKhoaLuan() != null ? kh.getDsNgayThucHienKhoaLuan().split(",\\s") : new String[0];
			LapKeHoachDto lapKeHoachDto = new LapKeHoachDto(kh.getId(), kh.getTenKeHoach(), kh.getChuThich(),
					Arrays.asList(ngayThucHienKL), kh.getHocKy(), new Timestamp(kh.getThoiGianBatDau().getTime()), new Timestamp(kh.getThoiGianKetThuc().getTime()),
					kh.getTinhTrang(), kh.getVaiTro(), kh.getMaNguoiDung());
				ds.add(lapKeHoachDto);
		});
		return ds;
	}
	
	@PostMapping("/them-sinh-vien-excel")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public ResponseEntity<?> themSinhVienExcel(@RequestParam("file") MultipartFile file) throws Exception {
		if (DeTaiImporter.isValidExcelFile(file)) {
			try {
				
				List<SinhVien> sinhViens = sinhVienImporter.addDataFDromExcel(file.getInputStream());
				sinhVienService.luuDanhSach(sinhViens);
				return ResponseEntity.ok(sinhViens);
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(500).body("Have Error");
			}
		}
		return null;
	}
	
	@PostMapping("/them-giang-vien-excel")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public ResponseEntity<?> themGiangVienExcel(@RequestParam("file") MultipartFile file) throws Exception {
		if (DeTaiImporter.isValidExcelFile(file)) {
			try {
				
				List<GiangVien> giangViens = giangVienImporter.addDataFDromExcel(file.getInputStream());
				giangVienService.luuDanhSach(giangViens);
				return ResponseEntity.ok(giangViens);
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(500).body("Have Error");
			}
		}
		return null;
	}
	
	@PostMapping("/them-phan-cong")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public ResponseEntity<?> themPhanCongGiangVien(@RequestBody PhanCongDto phanCongDto) throws Exception {
		Nhom nhom = nhomService.layTheoMa(phanCongDto.getMaNhom());
		GiangVien giangVien = giangVienService.layTheoMa(phanCongDto.getMaGiangVien());
		PhanCong phanCong = new PhanCong(phanCongDto.getViTriPhanCong(), phanCongDto.getChamCong(), nhom, giangVien);
		
		return ResponseEntity.ok(phanCongService.luu(phanCong));
	}
	
	@PutMapping("/cap-nhat-phan-cong")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public ResponseEntity<?> capNhatPhanCongGiangVien(@RequestBody PhanCongDto phanCongDto) throws Exception {
		Nhom nhom = nhomService.layTheoMa(phanCongDto.getMaNhom());
		GiangVien giangVien = giangVienService.layTheoMa(phanCongDto.getMaGiangVien());
		PhanCong phanCong = new PhanCong(phanCongDto.getViTriPhanCong(), phanCongDto.getChamCong(), nhom, giangVien);
		
		return ResponseEntity.ok(phanCongService.capNhat(phanCong));
	}
	
	@DeleteMapping("/xoa-phan-cong/{maPhanCong}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public ResponseEntity<?> xoaPhanCong(@PathVariable("maPhanCong") String maPhanCong) throws Exception {
		
		return ResponseEntity.ok(phanCongService.xoa(maPhanCong));
	}
	
	@GetMapping("/lay-ds-giang-vien")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public ResponseEntity<?> xoaPhanCong() throws Exception {
		
		return ResponseEntity.ok(giangVienService.layDanhSach());
	}
	
	@PostMapping("/lay-ds-sinh-vien")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public ResponseEntity<?> layDsSinhVien(@RequestBody LayDeTaiRquestDto request) {

		try {
			
			if (request.getMaHocKy() == null) {
				return ResponseEntity.ok(sinhVienService.layTatCaSinhVien());
			}
			
			return ResponseEntity.ok(sinhVienService.layTatCaSinhVienTheoHocKy(request.getMaHocKy(), request.getSoHocKy()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
