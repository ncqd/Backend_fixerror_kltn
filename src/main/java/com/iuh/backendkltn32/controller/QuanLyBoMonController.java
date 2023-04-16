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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.dto.DuyetRequest;
import com.iuh.backendkltn32.dto.LapKeHoachDto;
import com.iuh.backendkltn32.dto.LoginRequest;
import com.iuh.backendkltn32.entity.DeTai;
import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.KeHoach;
import com.iuh.backendkltn32.entity.Khoa;
import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.entity.TaiKhoan;
import com.iuh.backendkltn32.export.SinhVienExcelExporoter;
import com.iuh.backendkltn32.service.DeTaiService;
import com.iuh.backendkltn32.service.GiangVienService;
import com.iuh.backendkltn32.service.KeHoachService;
import com.iuh.backendkltn32.service.KhoaService;
import com.iuh.backendkltn32.service.NhomService;
import com.iuh.backendkltn32.service.SinhVienService;
import com.iuh.backendkltn32.service.TaiKhoanService;
import com.iuh.backendkltn32.service.VaiTroService;

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
	private PasswordEncoder encoder;

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
	public SinhVien themSinhVienVaoHeThong(@RequestBody SinhVien sinhVien) {

		try {
			SinhVien ketQuaLuuSinhVien = sinhVienService.luu(sinhVien);

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
	public GiangVien themSGiangVienVaoHeThong(@RequestBody GiangVien giangVien) {

		try {
			Khoa khoa = khoaService.layTheoMa(giangVien.getKhoa().getMaKhoa());
			giangVien.setKhoa(khoa);
			GiangVien ketQuaLuuGiangVien = giangVienService.luu(giangVien);

			TaiKhoan taiKhoan = new TaiKhoan(giangVien.getMaGiangVien(), encoder.encode("1111"),
					vaiTroService.layTheoMa(3L));

			taiKhoanService.luu(taiKhoan);
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
		KeHoach keHoach = new KeHoach(lapKeHoachDto.getId(), lapKeHoachDto.getChuThich(), lapKeHoachDto.getTenKeHoach(),
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

}
