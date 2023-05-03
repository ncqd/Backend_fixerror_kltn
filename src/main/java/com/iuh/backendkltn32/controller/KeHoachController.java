package com.iuh.backendkltn32.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.dto.KeHoachGvDto;
import com.iuh.backendkltn32.dto.LapKeHoachDto;
import com.iuh.backendkltn32.dto.LapKeHoachValidateDto;
import com.iuh.backendkltn32.dto.LayKeHoachRequest;
import com.iuh.backendkltn32.dto.NgayDto;
import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.KeHoach;
import com.iuh.backendkltn32.entity.LoaiKeHoach;
import com.iuh.backendkltn32.service.GiangVienService;
import com.iuh.backendkltn32.service.HocKyService;
import com.iuh.backendkltn32.service.KeHoachService;
import com.iuh.backendkltn32.service.SinhVienService;

@RestController
@RequestMapping("/api/ke-hoach")
public class KeHoachController {

	@Autowired
	private KeHoachService keHoachService;

	@Autowired
	private HocKyService hocKyService;

	@Autowired
	private SinhVienService sinhVienService;

	@Autowired
	private GiangVienService giangVienService;

	@PostMapping("/lay-ke-hoach")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public List<LapKeHoachDto> layKeHoachTheoMaNDHoacVaiTro(@RequestBody LayKeHoachRequest request) {
		List<LapKeHoachDto> dsKeHoach = new ArrayList<>();
		keHoachService.layKeHoachTheoVaiTro(request.getMaHocKy(), request.getVaiTro()).stream().forEach(kh -> {
			String[] ngayThucHienKL = kh.getDsNgayThucHienKhoaLuan() != null
					? kh.getDsNgayThucHienKhoaLuan().split(",\\s")
					: new String[0];
			LapKeHoachDto lapKeHoachDto = new LapKeHoachDto(kh.getId(), kh.getTenKeHoach(), kh.getChuThich(),
					Arrays.asList(ngayThucHienKL), kh.getHocKy(), new Timestamp(kh.getThoiGianBatDau().getTime()),
					new Timestamp(kh.getThoiGianKetThuc().getTime()), kh.getTinhTrang(), kh.getVaiTro(),
					kh.getMaNguoiDung(), kh.getLoaiKeHoach().getId());
			dsKeHoach.add(lapKeHoachDto);
		});
		if (request.getMaNguoiDung() != null) {
			keHoachService.layKeHoachTheoMaNguoiDung(request.getMaHocKy(), request.getMaNguoiDung()).stream()
					.forEach(kh -> {
						String[] ngayThucHienKL = kh.getDsNgayThucHienKhoaLuan() != null
								? kh.getDsNgayThucHienKhoaLuan().split(",\\s")
								: new String[0];
						LapKeHoachDto lapKeHoachDto = new LapKeHoachDto(kh.getId(), kh.getTenKeHoach(),
								kh.getChuThich(), Arrays.asList(ngayThucHienKL), kh.getHocKy(),
								new Timestamp(kh.getThoiGianBatDau().getTime()),
								new Timestamp(kh.getThoiGianKetThuc().getTime()), kh.getTinhTrang(), kh.getVaiTro(),
								kh.getMaNguoiDung(), kh.getLoaiKeHoach().getId());
						dsKeHoach.add(lapKeHoachDto);
					});
		}

		return dsKeHoach;
	}

	@PostMapping("/lay-ke-hoach-theo-ten")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public List<LapKeHoachValidateDto> layKeHoachTheoTen(@RequestBody LayKeHoachRequest request) {
		List<LapKeHoachValidateDto> dsKeHoach = new ArrayList<>();
		String maHocKy = request.getMaHocKy();
		if (maHocKy == null) {

			maHocKy = hocKyService.layHocKyCuoiCungTrongDS().getMaHocKy();
		}
		keHoachService.layKeHoachTheoVaiTro(maHocKy, request.getVaiTro()).stream().forEach(kh -> {
			boolean isValidate = false;
			if (kh.getThoiGianBatDau().getTime() > System.currentTimeMillis()) {
				isValidate = true;
			}
			if (kh.getThoiGianKetThuc().getTime() < System.currentTimeMillis()) {
				isValidate = true;
			}
			LapKeHoachValidateDto lapKeHoachValidateDto = new LapKeHoachValidateDto(kh.getTenKeHoach(), isValidate);
			dsKeHoach.add(lapKeHoachValidateDto);
		});
//		if (request.getMaNguoiDung() != null) {
//			keHoachService.layKeHoachTheoMaNguoiDung(request.getMaHocKy(), request.getMaNguoiDung()).stream()
//					.forEach(kh -> {
//						String[] ngayThucHienKL = kh.getDsNgayThucHienKhoaLuan() != null
//								? kh.getDsNgayThucHienKhoaLuan().split(",\\s")
//								: new String[0];
//						LapKeHoachDto lapKeHoachDto = new LapKeHoachDto(kh.getId(), kh.getTenKeHoach(),
//								kh.getChuThich(), Arrays.asList(ngayThucHienKL), kh.getHocKy(),
//								new Timestamp(kh.getThoiGianBatDau().getTime()),
//								new Timestamp(kh.getThoiGianKetThuc().getTime()), kh.getTinhTrang(), kh.getVaiTro(),
//								kh.getMaNguoiDung());
//						dsKeHoach.add(lapKeHoachDto);
//					});
//		}

		return dsKeHoach;
	}

	@PostMapping("/them-ke-hoach-phan-bien/{maNhom}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public List<KeHoach> themKeHoach(@RequestBody LapKeHoachDto lapKeHoachDto, @PathVariable("maNhom") String maNhom)
			throws Exception {
		HocKy hocKy = hocKyService.layTheoMa(lapKeHoachDto.getMaHocKy());
		List<String> maSinhVien = sinhVienService.layTatCaSinhVienTheoNhom(maNhom);
		return maSinhVien.stream().map(sv -> {
			try {
				return keHoachService
						.luu(new KeHoach(lapKeHoachDto.getTenKeHoach(), lapKeHoachDto.getChuThich(),
								lapKeHoachDto.getDsNgayThucHienKhoaLuan() != null
										? lapKeHoachDto.getDsNgayThucHienKhoaLuan().toString().substring(1,
												lapKeHoachDto.getDsNgayThucHienKhoaLuan().toString().length() - 1)
										: null,
								hocKy, lapKeHoachDto.getThoiGianBatDau(), lapKeHoachDto.getThoiGianKetThuc(),
								lapKeHoachDto.getTinhTrang(), lapKeHoachDto.getVaiTro(), sv,
								new LoaiKeHoach(lapKeHoachDto.getMaLoaiLich())));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}).toList();
	}

	@PutMapping("/cap-nhat-ke-hoach")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public LapKeHoachDto capNhatKeHoach(@RequestBody LapKeHoachDto lapKeHoachDto) throws Exception {
		HocKy hocKy = hocKyService.layTheoMa(lapKeHoachDto.getMaHocKy());
		KeHoach keHoach = new KeHoach(lapKeHoachDto.getId(), lapKeHoachDto.getTenKeHoach(), lapKeHoachDto.getChuThich(),
				lapKeHoachDto.getDsNgayThucHienKhoaLuan() != null ? lapKeHoachDto.getDsNgayThucHienKhoaLuan().toString()
						.substring(1, lapKeHoachDto.getDsNgayThucHienKhoaLuan().toString().length() - 1) : null,
				hocKy, lapKeHoachDto.getThoiGianBatDau(), lapKeHoachDto.getThoiGianKetThuc(),
				lapKeHoachDto.getTinhTrang(), lapKeHoachDto.getVaiTro(), lapKeHoachDto.getMaNguoiDung(),
				new LoaiKeHoach(lapKeHoachDto.getMaLoaiLich()), "");
		keHoachService.capNhat(keHoach);
		return lapKeHoachDto;
	}

	@DeleteMapping("/xoa-ke-hoach/{maKeHoach}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public String xoaKeHoach(@PathVariable String maKeHoach) throws Exception {
		return keHoachService.xoa(maKeHoach);
	}

	@PostMapping("/lay-ke-hoach-theo-maloai/{maNhom}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public List<LapKeHoachDto> layKeHoachTheoMaLoaiVaMaNhom(@RequestBody LapKeHoachDto lapKeHoachDto,
			@PathVariable("maNhom") String maNhom) throws Exception {
		HocKy hocKy = hocKyService.layTheoMa(lapKeHoachDto.getMaHocKy());
		List<String> maSinhVien = sinhVienService.layTatCaSinhVienTheoNhom(maNhom);
		List<LapKeHoachDto> ds = new ArrayList<>();
		for (String masv : maSinhVien) {
			keHoachService
					.layKeHoachTheoMaHocKyVaMaLoai(hocKy.getMaHocKy(), lapKeHoachDto.getMaLoaiLich().toString(), masv)
					.stream().forEach(kh -> {
						String[] ngayThucHienKL = kh.getDsNgayThucHienKhoaLuan() != null
								? kh.getDsNgayThucHienKhoaLuan().split(",\\s")
								: new String[0];
						LapKeHoachDto lapKeHoachDtoa = new LapKeHoachDto(kh.getId(), kh.getTenKeHoach(),
								kh.getChuThich(), Arrays.asList(ngayThucHienKL), kh.getHocKy(),
								new Timestamp(kh.getThoiGianBatDau().getTime()),
								new Timestamp(kh.getThoiGianKetThuc().getTime()), kh.getTinhTrang(), kh.getVaiTro(),
								kh.getMaNguoiDung(), kh.getLoaiKeHoach().getId());
						ds.add(lapKeHoachDtoa);
					});
		}

		return ds;
	}

	@GetMapping("/tach-ke-hoach")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public List<NgayDto> tachKeHoach() {
		List<NgayDto> result = new ArrayList<>();
		String maHocKy = hocKyService.layHocKyCuoiCungTrongDS().getMaHocKy();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		int date = 0;
		for (KeHoach kh : keHoachService.layTheoTenVaMaHocKyVaiTro(maHocKy, "Lịch chấm phản biện", "ROLE_GIANGVIEN")) {
			while (kh.getThoiGianKetThuc().getDate() - date > 0) {
				Date ngay = new Date(kh.getThoiGianBatDau().getYear() + 1900, kh.getThoiGianBatDau().getMonth() + 1,
						kh.getThoiGianBatDau().getDate() + date >= 30 ? date + 1
								: kh.getThoiGianBatDau().getDate() + date);
				result.add(new NgayDto(ngay, format.format(date)));
				date++;
			}
		}
		return result;
	}

	@GetMapping("/lay-tiet")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public List<String> layTiet() {
		List<String> result = new ArrayList<>();
		result.add("1-2");
		result.add("3-4");
		result.add("5-6");
		result.add("7-8");
		result.add("9-10");
		result.add("11-12");
		return result;
	}

	@GetMapping("/lay-phong")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public List<String> layPhong() {
		List<String> result = new ArrayList<>();
		result.add("1");
		result.add("2");
		result.add("3");
		result.add("4");
		result.add("5");
		return result;
	}

	@PostMapping("/lay-giangvien")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public List<GiangVien> layDsGiangVien(@RequestBody KeHoachGvDto keHoachGvDto) {
		List<GiangVien> result = new ArrayList<>();
		Timestamp temp = new Timestamp(keHoachGvDto.getNgay().getTime());
		switch (keHoachGvDto.getTiet()) {
		case "1-2":
			temp.setHours(6);
			temp.setMinutes(30);
			break;
		case "3-4":
			temp.setHours(8);
			temp.setMinutes(10);
			break;
		case "5-6":
			temp.setHours(10);
			break;
		case "7-8":
			temp.setHours(12);
			temp.setMinutes(30);
			break;
		case "9-10":
			temp.setHours(14);
			temp.setMinutes(10);
			break;
		case "11-12":
			temp.setHours(4);
			break;
		}
		List<String> maNguoiDungs = keHoachService.layMaNGuoiDung(temp, keHoachGvDto.getPhong());
		for (GiangVien gv : giangVienService.layDanhSach()) {
			if (!maNguoiDungs.contains(gv.getMaGiangVien())) {
				result.add(gv);
			}
		}
		return result;
	}

	@PostMapping("/tao-kehoach-giang-pb")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public List<KeHoach> taoKeHoachGiangVienPb(@RequestBody KeHoachGvDto keHoachGvDto) throws Exception {
		List<KeHoach> result = new ArrayList<>();
		for (String ma : keHoachGvDto.getDsMaGiangVienPB()) {
			Timestamp tgbd = new Timestamp(keHoachGvDto.getNgay().getTime());
			Timestamp tgkt = new Timestamp(keHoachGvDto.getNgay().getTime());
			switch (keHoachGvDto.getTiet()) {
			case "1-2":
				tgbd.setHours(6);
				tgbd.setMinutes(30);
				tgkt.setHours(8);
				tgbd.setMinutes(10);
				break;
			case "3-4":
				tgbd.setHours(8);
				tgbd.setMinutes(10);
				tgkt.setHours(10);
				break;
			case "5-6":
				tgbd.setHours(10);
				tgkt.setHours(11);
				tgbd.setMinutes(40);
				break;
			case "7-8":
				tgbd.setHours(12);
				tgbd.setMinutes(30);
				tgkt.setHours(2);
				tgbd.setMinutes(10);
				break;
			case "9-10":
				tgbd.setHours(14);
				tgbd.setMinutes(10);
				tgkt.setHours(3);
				tgbd.setMinutes(50);
				break;
			case "11-12":
				tgbd.setHours(4);
				tgkt.setHours(5);
				tgbd.setMinutes(40);
				break;
			}
			KeHoach keHoach = new KeHoach("Lịch phản biện sinh viên", keHoachGvDto.getPhong(), null,
					hocKyService.layTheoMa(keHoachGvDto.getMaHocKy()), tgbd, tgkt, 1, "ROLE_GIANGVIEN", ma,
					new LoaiKeHoach(3), keHoachGvDto.getPhong());
			result.add(keHoachService.luu(keHoach));
		}

		return result;
	}

}
