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

import com.iuh.backendkltn32.dto.KeHoachDto;
import com.iuh.backendkltn32.dto.KeHoachGvDto;
import com.iuh.backendkltn32.dto.KeHoachPBDto;
import com.iuh.backendkltn32.dto.LapKeHoachDto;
import com.iuh.backendkltn32.dto.LapKeHoachValidateDto;
import com.iuh.backendkltn32.dto.LayKeHoachHocKyDto;
import com.iuh.backendkltn32.dto.LayKeHoachRequest;
import com.iuh.backendkltn32.dto.NgayDto;
import com.iuh.backendkltn32.dto.NhomVaiTro;
import com.iuh.backendkltn32.dto.SinhVienNhomVaiTroDto;
import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.KeHoach;
import com.iuh.backendkltn32.entity.LoaiKeHoach;
import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.Phong;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.export.GiangVienPBDto;
import com.iuh.backendkltn32.service.GiangVienService;
import com.iuh.backendkltn32.service.HocKyService;
import com.iuh.backendkltn32.service.KeHoachService;
import com.iuh.backendkltn32.service.PhongService;
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

	@Autowired
	private PhongService phongService;

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
					kh.getMaNguoiDung(), kh.getLoaiKeHoach().getId(),
					kh.getPhong() != null ? phongService.layTheoMa(kh.getPhong()).getTenPhong() : "Khong Co Phong");
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
								kh.getMaNguoiDung(), kh.getLoaiKeHoach().getId(),
								phongService.layTheoMa(kh.getPhong()).getTenPhong());
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
			throws RuntimeException {
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
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}).toList();
	}

	@PutMapping("/cap-nhat-ke-hoach")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public LapKeHoachDto capNhatKeHoach(@RequestBody LapKeHoachDto lapKeHoachDto) throws RuntimeException {
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
	public String xoaKeHoach(@PathVariable String maKeHoach) throws RuntimeException {
		return keHoachService.xoa(maKeHoach);
	}

	@PostMapping("/lay-ke-hoach-theo-maloai/{maNhom}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public List<LapKeHoachDto> layKeHoachTheoMaLoaiVaMaNhom(@RequestBody LapKeHoachDto lapKeHoachDto,
			@PathVariable("maNhom") String maNhom) throws RuntimeException {
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
								kh.getMaNguoiDung(), kh.getLoaiKeHoach().getId(),
								kh.getPhong() != null ? phongService.layTheoMa(kh.getPhong()).getTenPhong()
										: "Khong Co Phong");
						ds.add(lapKeHoachDtoa);
					});
		}

		return ds;
	}

	@GetMapping("/tach-ke-hoach")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	@SuppressWarnings("deprecation")
	public List<NgayDto> tachKeHoach() {
		List<NgayDto> result = new ArrayList<>();
		String maHocKy = hocKyService.layHocKyCuoiCungTrongDS().getMaHocKy();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		int date = 0;
		for (KeHoach kh : keHoachService.layTheoTenVaMaHocKyVaiTro(maHocKy, "Lịch chấm phản biện", "ROLE_GIANGVIEN")) {
			if (kh.getMaNguoiDung() == null) {
				while (kh.getThoiGianKetThuc().getDate() - (kh.getThoiGianBatDau().getDate() + date) >= 0) {
					Timestamp ngay = new Timestamp(kh.getThoiGianBatDau().getYear(), kh.getThoiGianBatDau().getMonth(),
							kh.getThoiGianBatDau().getDate() + date > 31 ? date + 1
									: kh.getThoiGianBatDau().getDate() + date,
							kh.getThoiGianBatDau().getHours(), kh.getThoiGianBatDau().getMinutes(),
							kh.getThoiGianBatDau().getSeconds(), kh.getThoiGianBatDau().getNanos());
					result.add(new NgayDto(ngay, format.format(ngay)));
					date++;
				}
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

	@PostMapping("/lay-phong")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public List<Phong> layPhong(@RequestBody KeHoachGvDto keHoachGvDto) {
		List<Phong> result = new ArrayList<>();
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
		List<String> phongs = keHoachService.layPhong(temp);
		for (Phong phong : phongService.layHetPhong()) {
			if (!phongs.contains(phong.getId() + "")) {
				result.add(phong);
			}
		}
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

	@PostMapping("/tao-kehoach-giangvien-pb")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public List<KeHoach> taoKeHoachGiangVienPb(@RequestBody KeHoachGvDto keHoachGvDto) throws RuntimeException {
		List<KeHoach> result = new ArrayList<>();
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		for (String ma : keHoachGvDto.getDsMaGiangVienPB()) {
			Timestamp tgbd = new Timestamp(keHoachGvDto.getNgay().getTime());
			Timestamp tgkt = new Timestamp(keHoachGvDto.getNgay().getTime());
			switch (keHoachGvDto.getTiet()) {
			case "1-2":
				tgbd.setHours(6);
				tgbd.setMinutes(30);
				tgkt.setHours(8);
				tgkt.setMinutes(10);
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
				tgbd.setHours(16);
				tgkt.setHours(17);
				tgbd.setMinutes(40);
				break;
			}
			KeHoach keHoach = new KeHoach("Lịch chấm phản biện", keHoachGvDto.getPhong(), null, hocKy, tgbd, tgkt, 1,
					"ROLE_GIANGVIEN", ma, new LoaiKeHoach(3), keHoachGvDto.getPhong());
			result.add(keHoachService.luu(keHoach));
		}

		return result;
	}

	@PostMapping("/lay-lich-hocky-pb")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public List<KeHoachDto> layLichTheoHKPB(@RequestBody LayKeHoachHocKyDto keHoachHocKyDto) throws RuntimeException {
		List<KeHoachDto> result = new ArrayList<>();
		String tenLich = "";
		switch (keHoachHocKyDto.getLich()) {
		case "PB":
			tenLich = "Lịch chấm phản biện";
			break;

		case "HD":
			tenLich = "Lịch chấm hội đồng";
			break;
		}
		Phong phong = new Phong(100, "aaa");
		Timestamp tgbd = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		keHoachService.layTheoTenKhongMaNguoiDung(keHoachHocKyDto.getMaHocKy(), tenLich, "ROLE_GIANGVIEN")
				.forEach(kh -> {
					String tiet = "";
					switch (kh.getThoiGianBatDau().getHours()) {
					case 6:
						tiet = "1-2";
						break;
					case 8:
						tiet = "3-4";
						break;
					case 10:
						tiet = "5-6";
						break;
					case 12:
						tiet = "7-8";
						break;
					case 14:
						tiet = "9-10";
						break;
					case 16:
						tiet = "11-12";
						break;
					}
					if (keHoachService.layTheoPhongTGChuaChiaNhom(kh.getPhong(), kh.getThoiGianBatDau()).size() == 0) {
						if (!phong.getId().toString().equals(kh.getPhong())
								|| tgbd.getHours() != kh.getThoiGianBatDau().getHours()
								|| tgbd.getDate() != kh.getThoiGianBatDau().getDate()) {
							try {
								phong.setId(Integer.parseInt(kh.getPhong()));
								tgbd.setHours(kh.getThoiGianBatDau().getHours());
								tgbd.setDate(kh.getThoiGianBatDau().getDate());
								List<GiangVien> gv = new ArrayList<>();
								for (String maGV : keHoachService.layTheoPhongTg(kh.getPhong(),
										kh.getThoiGianBatDau())) {
									gv.add(giangVienService.layTheoMa(maGV));
								}
								result.add(new KeHoachDto(kh.getId() + "", kh.getTenKeHoach(), tiet,
										format.format(kh.getThoiGianBatDau()),
										phongService.layTheoMa(kh.getPhong()).getTenPhong(), gv));
							} catch (RuntimeException e) {
								e.printStackTrace();
							}
						}

					}

				});

		return result;
	}

	@GetMapping("/tach-ke-hoach-hd")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public List<NgayDto> tachKeHoachHD() {
		List<NgayDto> result = new ArrayList<>();
		String maHocKy = hocKyService.layHocKyCuoiCungTrongDS().getMaHocKy();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		int date = 0;
		for (KeHoach kh : keHoachService.layTheoTenVaMaHocKyVaiTro(maHocKy, "Lịch chấm hội đồng", "ROLE_GIANGVIEN")) {
			if (kh.getMaNguoiDung() == null) {
				while (kh.getThoiGianKetThuc().getDate() - (kh.getThoiGianBatDau().getDate() + date) >= 0) {
					Timestamp ngay = new Timestamp(kh.getThoiGianBatDau().getYear(), kh.getThoiGianBatDau().getMonth(),
							kh.getThoiGianBatDau().getDate() + date >= 30 ? date + 1
									: kh.getThoiGianBatDau().getDate() + date,
							kh.getThoiGianBatDau().getHours(), kh.getThoiGianBatDau().getMinutes(),
							kh.getThoiGianBatDau().getSeconds(), kh.getThoiGianBatDau().getNanos());
					result.add(new NgayDto(ngay, format.format(ngay)));
					date++;
				}
			}

		}
		return result;
	}

	@PostMapping("/tao-kehoach-giangvien-hd")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public List<KeHoach> taoKeHoachGiangVienhhd(@RequestBody KeHoachGvDto keHoachGvDto) throws RuntimeException {
		List<KeHoach> result = new ArrayList<>();
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		for (String ma : keHoachGvDto.getDsMaGiangVienPB()) {
			Timestamp tgbd = new Timestamp(keHoachGvDto.getNgay().getTime());
			Timestamp tgkt = new Timestamp(keHoachGvDto.getNgay().getTime());
			switch (keHoachGvDto.getTiet()) {
			case "1-2":
				tgbd.setHours(6);
				tgbd.setMinutes(30);
				tgkt.setHours(8);
				tgkt.setMinutes(10);
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
				tgbd.setHours(16);
				tgkt.setHours(17);
				tgbd.setMinutes(40);
				break;
			}
			KeHoach keHoach = new KeHoach("Lịch chấm hội đồng", keHoachGvDto.getPhong(), null, hocKy, tgbd, tgkt, 1,
					"ROLE_GIANGVIEN", ma, new LoaiKeHoach(3), keHoachGvDto.getPhong());
			result.add(keHoachService.luu(keHoach));
		}

		return result;
	}

	@PostMapping("/lay-ke-hoach-pb")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public List<KeHoachPBDto> layKeHoachPB(@RequestBody LayKeHoachRequest request) {
		List<KeHoachPBDto> dsKeHoach = new ArrayList<>();
		if (request.getMaNguoiDung() == null) {
			throw new RuntimeException("Mã người dùng không hợp lệ");
		}
		keHoachService.layKeHoachTheoMaHocKyVaMaLoai(request.getMaHocKy(), "3", request.getMaNguoiDung()).stream()
				.forEach(kh -> {
					String[] ngayThucHienKL = kh.getDsNgayThucHienKhoaLuan() != null
							? kh.getDsNgayThucHienKhoaLuan().split(",\\s")
							: new String[0];
					List<SinhVienNhomVaiTroDto> nhomSinhVienPB = new ArrayList<>();
					NhomVaiTro nhomSvPB = new NhomVaiTro();
					for (KeHoach keHoachSV : keHoachService.layTheoPhongTGChuaChiaNhom(kh.getPhong(), kh.getThoiGianBatDau())) {
						SinhVien sinhVien = sinhVienService.layTheoMa(keHoachSV.getMaNguoiDung());
						nhomSvPB.setMaNhom(sinhVien.getNhom().getMaNhom());
						nhomSvPB.setMaDeTai(sinhVien.getNhom().getDeTai().getMaDeTai());
						nhomSvPB.setTenNhom(sinhVien.getNhom().getTenNhom());
						nhomSvPB.setTenDeTai(sinhVien.getNhom().getDeTai().getTenDeTai());
						nhomSinhVienPB.add(new SinhVienNhomVaiTroDto(sinhVien.getMaSinhVien(), sinhVien.getTenSinhVien()));
						
					}
					
					List<GiangVienPBDto> nhomGiangVienPB = new ArrayList<>();
					for (KeHoach keHoachGV : keHoachService.layKeHoachPB(kh.getPhong(), kh.getThoiGianBatDau())) {
						GiangVien giangVien = giangVienService.layTheoMa(keHoachGV.getMaNguoiDung());
						nhomGiangVienPB.add(new GiangVienPBDto(giangVien.getMaGiangVien(), giangVien.getTenGiangVien()));
					}
					KeHoachPBDto keHoachPBDto = new KeHoachPBDto(kh.getId(), kh.getTenKeHoach(), kh.getChuThich(), 
							Arrays.asList(ngayThucHienKL),kh.getHocKy(), kh.getHocKy().getMaHocKy(), kh.getThoiGianBatDau(), 
							kh.getThoiGianKetThuc(), kh.getTinhTrang(), kh.getVaiTro(), kh.getMaNguoiDung(),
							kh.getLoaiKeHoach().getId(), phongService.layTheoMa(kh.getPhong()).getTenPhong(), nhomSvPB,nhomSinhVienPB, nhomGiangVienPB);
					
					dsKeHoach.add(keHoachPBDto);
				});

		return dsKeHoach;
	}

}
