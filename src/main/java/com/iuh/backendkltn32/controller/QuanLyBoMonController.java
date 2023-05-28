package com.iuh.backendkltn32.controller;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.iuh.backendkltn32.entity.*;
import com.iuh.backendkltn32.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.dto.ChoXemDiemDto;
import com.iuh.backendkltn32.dto.DataThongKeDto;
import com.iuh.backendkltn32.dto.DuyetRequest;
import com.iuh.backendkltn32.dto.GiangVienDtoChuaCoSoDTTT;
import com.iuh.backendkltn32.dto.LapKeHoachDto;
import com.iuh.backendkltn32.dto.LayDeTaiRquestDto;
import com.iuh.backendkltn32.dto.LoginRequest;
import com.iuh.backendkltn32.dto.LopHocPhanDto;
import com.iuh.backendkltn32.dto.PhanCongDto;
import com.iuh.backendkltn32.dto.PhanCongDto2;
import com.iuh.backendkltn32.dto.SinhVienNhomVaiTroDto;
import com.iuh.backendkltn32.dto.ThongKeDto;
import com.iuh.backendkltn32.export.DanhSachDeTaiExporter;
import com.iuh.backendkltn32.export.DanhSachNhomExporter;
import com.iuh.backendkltn32.export.DanhSachNhomKLTNChamDiemExporter;
import com.iuh.backendkltn32.export.FileMauGiangVienExporter;
import com.iuh.backendkltn32.export.FileMauSinhVienExporter;
import com.iuh.backendkltn32.export.KetQuaKLTNExporter;
import com.iuh.backendkltn32.export.MailMergeExporter;
import com.iuh.backendkltn32.export.PhieuChamExporter;
import com.iuh.backendkltn32.export.SinhVienExcelExporoter;

@RestController
@RequestMapping("/api/quan-ly")
public class QuanLyBoMonController {

	@Autowired
	private SinhVienService sinhVienService;

	@Autowired
	private GiangVienService giangVienService;

	@Autowired
	private DeTaiService deTaiService;

	@Autowired
	private NhomService nhomService;

	@Autowired
	private KeHoachService keHoachService;

	@Autowired
	private HocKyService hocKyService;

	@Autowired
	private PhanCongService phanCongService;

	@Autowired
	private TinNhanSerivce tinNhanSerivce;

	@Autowired
	private PhongService phongService;

	@Autowired
	private QuanLyBoMonService quanLyBoMonService;

	@Autowired
	private PhieuChamMauService phieuChamMauService;

	@Autowired
	private PhieuChamService phieuChamService;

	@Autowired
	private TieuChiChamDiemService tieuChiChamDiemService;

	@Autowired
	private LopHocPhanService lopHocPhanService;

	@GetMapping("/thong-tin-ca-nhan/{maQuanLy}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public GiangVien hienThiThongTinCaNhan(@PathVariable String maQuanLy, @RequestBody LoginRequest loginRequest) {
		try {
			if (!loginRequest.getTenTaiKhoan().equals(maQuanLy)) {
				throw new RuntimeException("Khong Dung Ma Giang Vien");
			}

			return giangVienService.layTheoMa(maQuanLy);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping("/xuat-ds-sinhvien")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public void exportToExcel(@RequestBody LopHocPhanDto lopHocPhanDto, HttpServletResponse response)
			throws RuntimeException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=danh-sach-sinh-vien_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		LopHocPhan lopHocPhan = lopHocPhanService.layTheoMa(lopHocPhanDto.getMaLopHocPhan());
		SinhVienExcelExporoter excelExporter = new SinhVienExcelExporoter(sinhVienService, phieuChamService,
				lopHocPhan);

		try {
			excelExporter.export(response);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@PostMapping("/duyet-de-tai")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public DeTai pheDuyetDeTai(@RequestBody DuyetRequest duyetDeTaiRequest) {
		try {
			DeTai deTai = deTaiService.layTheoMa(duyetDeTaiRequest.getMa());
			deTai.setTrangThai(duyetDeTaiRequest.getTrangThai());
			deTaiService.capNhat(deTai);
			TinNhan tinNhan;
			if (duyetDeTaiRequest.getTrangThai() == 1) {
				tinNhan = new TinNhan("Bạn có đề tài chưa được duyệt",
						"Đề Tài: " + deTai.getTenDeTai() + " | Chưa đạt | " + duyetDeTaiRequest.getLoiNhan() + " | "
								+ deTai.getMaDeTai(),
						"12392401", deTai.getGiangVien().getMaGiangVien(), 0,
						new Timestamp(System.currentTimeMillis()));
			} else {
				tinNhan = new TinNhan("Bạn có đề tài đã duyệt",
						"Đề Tài " + deTai.getTenDeTai() + "| Đã Được Duyệt  | " + " | " + deTai.getMaDeTai(),
						"12392401", deTai.getGiangVien().getMaGiangVien(), 0,
						new Timestamp(System.currentTimeMillis()));
			}

			tinNhanSerivce.luu(tinNhan);
			return deTai;
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;

	}

	@PostMapping("/duyet-nhom")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public Nhom pheDuyetNhom(@RequestBody DuyetRequest duyetNhomRequest) {
		try {
			Nhom nhom = nhomService.layTheoMa(duyetNhomRequest.getMa());

			nhom.setTinhTrang(duyetNhomRequest.getTrangThai());
			nhomService.capNhat(nhom);
			return nhom;
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;

	}

	@PostMapping("/them-ke-hoach")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public LapKeHoachDto themKeHoach(@RequestBody LapKeHoachDto lapKeHoachDto) throws RuntimeException {
		HocKy hocKy = hocKyService.layTheoMa(lapKeHoachDto.getMaHocKy());
		KeHoach keHoach = new KeHoach(lapKeHoachDto.getTenKeHoach(), lapKeHoachDto.getChuThich(),
				lapKeHoachDto.getDsNgayThucHienKhoaLuan() != null ? lapKeHoachDto.getDsNgayThucHienKhoaLuan().toString()
						.substring(1, lapKeHoachDto.getDsNgayThucHienKhoaLuan().toString().length() - 1) : null,
				hocKy, lapKeHoachDto.getThoiGianBatDau(), lapKeHoachDto.getThoiGianKetThuc(),
				lapKeHoachDto.getTinhTrang(), lapKeHoachDto.getVaiTro(), lapKeHoachDto.getMaNguoiDung(),
				new LoaiKeHoach(lapKeHoachDto.getMaLoaiLich()));
		keHoachService.luu(keHoach);
		return lapKeHoachDto;
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

	@GetMapping("/lay-ke-hoach/{maKeHoach}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public LapKeHoachDto layKeHoachTheoMa(@PathVariable String maKeHoach) throws RuntimeException {
		KeHoach kh = keHoachService.layTheoMa(maKeHoach);

		String[] ngayThucHienKL = kh.getDsNgayThucHienKhoaLuan() != null ? kh.getDsNgayThucHienKhoaLuan().split(",\\s")
				: new String[0];
		return new LapKeHoachDto(kh.getId(), kh.getTenKeHoach(), kh.getChuThich(), Arrays.asList(ngayThucHienKL),
				kh.getHocKy(), new Timestamp(kh.getThoiGianBatDau().getTime()),
				new Timestamp(kh.getThoiGianKetThuc().getTime()), kh.getTinhTrang(), kh.getVaiTro(),
				kh.getMaNguoiDung(), kh.getLoaiKeHoach().getId(),
				kh.getPhong() != null ? phongService.layTheoMa(kh.getPhong()).getTenPhong() : "Khong Co Phong");
	}

	@GetMapping("/lay-ke-hoach-theo-hocky/{maHocKy}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public List<LapKeHoachDto> layKeHoachTheoHk(@PathVariable String maHocKy) throws RuntimeException {
		List<LapKeHoachDto> ds = new ArrayList<>();
		keHoachService.layKeHoachTheoMaHocKy(maHocKy).forEach(kh -> {

			String[] ngayThucHienKL = kh.getDsNgayThucHienKhoaLuan() != null
					? kh.getDsNgayThucHienKhoaLuan().split(",\\s")
					: new String[0];
			LapKeHoachDto lapKeHoachDto = new LapKeHoachDto(kh.getId(), kh.getTenKeHoach(), kh.getChuThich(),
					Arrays.asList(ngayThucHienKL), kh.getHocKy(), new Timestamp(kh.getThoiGianBatDau().getTime()),
					new Timestamp(kh.getThoiGianKetThuc().getTime()), kh.getTinhTrang(), kh.getVaiTro(),
					kh.getMaNguoiDung(), kh.getLoaiKeHoach().getId(),
					kh.getPhong() != null ? phongService.layTheoMa(kh.getPhong()).getTenPhong() : "Khong Co Phong");
			ds.add(lapKeHoachDto);
		});
		return ds;
	}

	@PostMapping("/them-phan-cong")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public ResponseEntity<?> themPhanCongGiangVien(@RequestBody PhanCongDto phanCongDto) throws RuntimeException {
		Nhom nhom = nhomService.layTheoMa(phanCongDto.getMaNhom() == null ? "123" : phanCongDto.getMaNhom());
		List<PhanCong> phanCongs = phanCongDto.getDsMaGiangVienPB().stream().map(ma -> {
			GiangVien giangVien;
			try {
				if (nhom != null) {
					if (nhom.getDeTai().getGiangVien().getMaGiangVien().equals(ma)) {
						throw new RuntimeException("Không cho phép giảng viên hướng dẫn phản biện đề tài này");
					}
				}

				giangVien = giangVienService.layTheoMa(ma);

				PhanCong phanCong = new PhanCong(phanCongDto.getViTriPhanCong(), phanCongDto.getChamCong(), nhom,
						giangVien);
				phanCong.setMaPhanCong(phanCongDto.getMaPhanCong());
				return phanCongService.luu(phanCong);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			return null;
		}).toList();
		Integer gioBatDau = Integer.parseInt(phanCongDto.getTietBatDau()) + 5;
		Integer gioKetThuc = Integer.parseInt(phanCongDto.getTietKetThuc()) + 5;
		Timestamp tgbd = new Timestamp(phanCongDto.getNgay().getTime());
		tgbd.setHours(gioBatDau);
		Timestamp tgkt = new Timestamp(phanCongDto.getNgay().getTime());
		tgkt.setHours(gioKetThuc);
		sinhVienService
				.layTatCaSinhVienTheoNhom(
						phanCongDto.getMaNhom() == null ? "123" : Objects.requireNonNull(nhom).getMaNhom())
				.forEach(sv -> {
					try {

						KeHoach keHoach = new KeHoach("Lịch phản biện sinh viên", phanCongDto.getPhong(), null,
								hocKyService.layTheoMa(phanCongDto.getMaHocKy()), tgbd, tgkt, 1, "ROLE_SINHVIEN", sv,
								new LoaiKeHoach(3));
						keHoachService.luu(keHoach);
					} catch (RuntimeException e) {
						e.printStackTrace();
					}
				});
		return ResponseEntity.ok(phanCongs);
	}

	@PostMapping("/them-ds-phan-cong")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	@Transactional
	public ResponseEntity<?> themPhanCongGiangVien(@RequestBody List<PhanCongDto2> phanCongDtos)
			throws RuntimeException {
		try {
			List<PhanCong> phanCongs = new ArrayList<>();
			for (PhanCongDto2 phanCongDto : phanCongDtos) {
				Nhom nhom = nhomService.layTheoMa(phanCongDto.getMaNhom() == null ? "123" : phanCongDto.getMaNhom());
				if (phanCongDto.getViTriPhanCong().equals("phan bien")) {
					for (String ma : phanCongDto.getDsMaGiangVienPB()) {
						GiangVien giangVien;
						try {
							if (nhom != null) {
								if (nhom.getDeTai().getGiangVien().getMaGiangVien().equals(ma)) {
									return ResponseEntity.status(500)
											.body("Không cho phép giảng viên hướng dẫn phản biện đề tài này");
								}
							}
							System.out.println(ma);
							giangVien = giangVienService.layTheoMa(ma);

							PhanCong phanCong = new PhanCong(phanCongDto.getViTriPhanCong(), phanCongDto.getChamCong(),
									nhom, giangVien);
							phanCong.setMaPhanCong(phanCongDto.getMaPhanCong());
							phanCongs.add(phanCongService.luu(phanCong));
						} catch (RuntimeException e) {
							e.printStackTrace();
						}
					}
				} else if (phanCongDto.getViTriPhanCong().equals("hoi dong")) {
					try {
						String maGVCT = phanCongDto.getDsMaGiangVienPB().get(0);
						String maGVTK = phanCongDto.getDsMaGiangVienPB().get(1);

						GiangVien giangVienCT = giangVienService.layTheoMa(maGVCT);
						GiangVien giangVienTK = giangVienService.layTheoMa(maGVTK);

						if (nhom != null) {
							if (nhom.getDeTai().getGiangVien().getMaGiangVien().equals(maGVCT)) {
								return ResponseEntity.status(500)
										.body("Không cho phép giảng viên hướng dẫn phản biện đề tài này");
							}
						}
						if (nhom != null) {
							if (nhom.getDeTai().getGiangVien().getMaGiangVien().equals(maGVTK)) {
								return ResponseEntity.status(500)
										.body("Không cho phép giảng viên hướng dẫn phản biện đề tài này");
							}
						}

						PhanCong phanCongCT = new PhanCong("chu tich", phanCongDto.getChamCong(), nhom, giangVienCT);
						phanCongCT.setMaPhanCong(phanCongDto.getMaPhanCong());
						phanCongs.add(phanCongService.luu(phanCongCT));

						PhanCong phanCongTK = new PhanCong("thu ky", phanCongDto.getChamCong(), nhom, giangVienTK);
						phanCongTK.setMaPhanCong(phanCongDto.getMaPhanCong());
						phanCongs.add(phanCongService.luu(phanCongTK));

						if (phanCongDto.getDsMaGiangVienPB().size() > 2) {

							String maGVTV3 = phanCongDto.getDsMaGiangVienPB().get(2);
							GiangVien giangVientv3 = giangVienService.layTheoMa(maGVTV3);
							PhanCong phanCongTV3 = new PhanCong("thanh vien 3", phanCongDto.getChamCong(), nhom,
									giangVientv3);
							if (nhom != null) {
								if (nhom.getDeTai().getGiangVien().getMaGiangVien().equals(maGVTV3)) {
									return ResponseEntity.status(500)
											.body("Không cho phép giảng viên hướng dẫn phản biện đề tài này");
								}
							}
							phanCongTV3.setMaPhanCong(phanCongDto.getMaPhanCong());
							phanCongs.add(phanCongService.luu(phanCongTV3));
						}

					} catch (RuntimeException e) {
						e.printStackTrace();
					}
				}

				Timestamp tgbd = new Timestamp(phanCongDto.getNgay().getTime());
				Timestamp tgkt = new Timestamp(phanCongDto.getNgay().getTime());
				switch (phanCongDto.getTiet()) {
				case "1-2": {
					tgbd.setHours(6);
					tgbd.setMinutes(30);
					tgkt.setHours(8);
					tgkt.setMinutes(10);
					break;
				}
				case "3-4": {
					tgbd.setHours(8);
					tgbd.setMinutes(10);
					tgkt.setHours(10);
					break;
				}
				case "5-6": {
					tgbd.setHours(10);
					tgkt.setHours(11);
					tgkt.setMinutes(40);
					break;
				}
				case "7-8": {
					tgbd.setHours(12);
					tgbd.setMinutes(10);
					tgkt.setHours(2);
					tgkt.setMinutes(10);
					break;
				}
				case "9-10": {
					tgbd.setHours(14);
					tgbd.setMinutes(10);
					tgkt.setHours(3);
					tgkt.setMinutes(50);
					break;
				}
				case "11-12": {
					tgbd.setHours(16);
					tgkt.setHours(17);
					tgkt.setMinutes(40);
					break;
				}
				}
				sinhVienService
						.layTatCaSinhVienTheoNhom(
								phanCongDto.getMaNhom() == null ? "123" : Objects.requireNonNull(nhom).getMaNhom())
						.forEach(sv -> {
							try {
								Phong phong = phongService.layPhongTheoTenPhong(phanCongDto.getPhong());
								if (phanCongDto.getViTriPhanCong().equals("PB")) {
									KeHoach keHoach = new KeHoach("Lịch phản biện sinh viên", phong.getTenPhong(), null,
											hocKyService.layTheoMa(phanCongDto.getMaHocKy()), tgbd, tgkt, 1,
											"ROLE_SINHVIEN", sv, new LoaiKeHoach(3), phong.getId() + "");
									keHoachService.luu(keHoach);
								} else {
									KeHoach keHoach = new KeHoach("Lịch chấm hội đồng sinh viên", phong.getTenPhong(),
											null, hocKyService.layTheoMa(phanCongDto.getMaHocKy()), tgbd, tgkt, 1,
											"ROLE_SINHVIEN", sv, new LoaiKeHoach(3), phong.getId() + "");
									keHoachService.luu(keHoach);
								}

							} catch (RuntimeException e) {
								e.printStackTrace();
							}
						});
			}
			return ResponseEntity.ok(phanCongs);
		} catch (Exception e) {
			throw new RuntimeException("Đã xảy ra lỗi \n" + e.getMessage());
		}

	}

	@PutMapping("/cap-nhat-phan-cong")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public ResponseEntity<?> capNhatPhanCongGiangVien(@RequestBody PhanCongDto phanCongDto) throws RuntimeException {
		Nhom nhom = nhomService.layTheoMa(phanCongDto.getMaNhom());
		List<PhanCong> phanCongs = phanCongDto.getDsMaGiangVienPB().stream().map(ma -> {
			GiangVien giangVien;
			try {
				if (nhom.getDeTai().getGiangVien().getMaGiangVien().equals(ma)) {
					throw new RuntimeException("Không cho phép giảng viên hướng dẫn phản biện đề tài này");
				}
				giangVien = giangVienService.layTheoMa(ma);

				PhanCong phanCong = new PhanCong(phanCongDto.getViTriPhanCong(), phanCongDto.getChamCong(), nhom,
						giangVien);
				phanCong.setMaPhanCong(phanCongDto.getMaPhanCong());
				return phanCongService.capNhat(phanCong);
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}).toList();

		return ResponseEntity.ok(phanCongs);
	}

	@DeleteMapping("/xoa-phan-cong/{maPhanCong}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public ResponseEntity<?> xoaPhanCong(@PathVariable("maPhanCong") String maPhanCong) throws RuntimeException {

		return ResponseEntity.ok(phanCongService.xoa(maPhanCong));
	}

	@GetMapping("/lay-ds-giang-vien")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public ResponseEntity<?> xoaPhanCong() throws RuntimeException {

		return ResponseEntity.ok(giangVienService.layDanhSach());
	}

	@PostMapping("/lay-ds-sinh-vien")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public ResponseEntity<?> layDsSinhVien(@RequestBody LayDeTaiRquestDto request) {

		try {

			if (request.getMaHocKy() == null) {
				return ResponseEntity.ok(sinhVienService.layTatCaSinhVien());
			}

			return ResponseEntity
					.ok(sinhVienService.layTatCaSinhVienTheoHocKy(request.getMaHocKy(), request.getSoHocKy()));
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping("/xuat-ds-nhom")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public void xuatDSNhom(@RequestBody DuyetRequest request, HttpServletResponse response) throws RuntimeException {
		if (request.getMaHocKy() == null) {
			throw new RuntimeException("Please give me ma Hoc Ky");
		}
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		HocKy hocKy = hocKyService.layTheoMa(request.getMaHocKy());

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=danh-nhom-sinh-vien_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);
		DanhSachNhomExporter danhSachNhomExporter = new DanhSachNhomExporter(hocKy, nhomService, sinhVienService);
		try {
			danhSachNhomExporter.export(response);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@PostMapping("/xuat-ds-de-tai")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public void xuatDSDeTai(@RequestBody DuyetRequest request, HttpServletResponse response) throws RuntimeException {
		if (request.getMaHocKy() == null) {
			throw new RuntimeException("Please give me ma Hoc Ky");
		}
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=danh-de-tai_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		HocKy hocKy = hocKyService.layTheoMa(request.getMaHocKy());
		DanhSachDeTaiExporter danhSachDeTaiExporter = new DanhSachDeTaiExporter(deTaiService, hocKy.getMaHocKy());
		try {
			danhSachDeTaiExporter.export(response);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@PostMapping("/xuat-mailmerge")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public void xuatMailMerge(@RequestBody DuyetRequest request, HttpServletResponse response) throws RuntimeException {
		if (request.getMaHocKy() == null) {
			throw new RuntimeException("Please give me ma Hoc Ky");
		}
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=danh-mail-merge_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		HocKy hocKy = hocKyService.layTheoMa(request.getMaHocKy());
		MailMergeExporter danhSachDeTaiExporter = new MailMergeExporter(nhomService, sinhVienService, phanCongService,
				phieuChamService, hocKy);
		try {
			danhSachDeTaiExporter.export(response);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@PostMapping("/xuat-ketquanhom-kltn")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public void xuatKetQuaNhomKLTN(@RequestBody DuyetRequest request, HttpServletResponse response)
			throws RuntimeException {
		if (request.getMaHocKy() == null) {
			throw new RuntimeException("Please give me ma Hoc Ky");
		}
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=ketqua_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		HocKy hocKy = hocKyService.layTheoMa(request.getMaHocKy());
		KetQuaKLTNExporter ketQuaKLTNExporter = new KetQuaKLTNExporter(phieuChamMauService, tieuChiChamDiemService,
				hocKy, phieuChamService, nhomService, sinhVienService);
		try {
			ketQuaKLTNExporter.export(response);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@PostMapping("/xuat-nhom-ra-pb")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public void xuatDSNhomRaPB(@RequestBody DuyetRequest request, HttpServletResponse response)
			throws RuntimeException {
		if (request.getMaHocKy() == null) {
			throw new RuntimeException("Please give me ma Hoc Ky");
		}
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=ketqua_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		HocKy hocKy = hocKyService.layTheoMa(request.getMaHocKy());
		System.out.println(hocKy.getMaHocKy());
		DanhSachNhomKLTNChamDiemExporter ketQuaKLTNExporter = new DanhSachNhomKLTNChamDiemExporter(nhomService,
				sinhVienService, phanCongService, hocKy);
		try {
			ketQuaKLTNExporter.export(response);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@GetMapping("/xuat-file-mau-sv")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public void xuaFileMaiSinhVien(HttpServletResponse response) throws RuntimeException {
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=ketqua_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		FileMauSinhVienExporter sinhVienExporter = new FileMauSinhVienExporter();
		try {
			sinhVienExporter.export(response);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@GetMapping("/xuat-file-mau-gv")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public void xuaFileMauGaingVien(HttpServletResponse response) throws RuntimeException {
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=ketqua_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		FileMauGiangVienExporter giangVienExporter = new FileMauGiangVienExporter();
		try {
			giangVienExporter.export(response);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@GetMapping("/xuat-phieu-cham-ql")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public void xuatPhieuChamGVPB(HttpServletResponse response) throws RuntimeException {
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=ketqua_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

//		PhieuChamExporter phieuChamExporter = new PhieuChamExporter();
//		try {
//			phieuChamExporter.export(response);
//		} catch (Exception e) {
//			throw new RuntimeException(e.getMessage());
//		}
	}

	@GetMapping("/thong-ke-sinhvien-detai")
	public ThongKeDto thongKeSvDT() {
		List<HocKy> listHocKys = hocKyService.layTatCaHocKy();
		List<DataThongKeDto> thongKeDtos = new ArrayList<>();
		List<String> tenHocKys = new ArrayList<>();
		List<Long> soSV = new ArrayList<>();
		List<Long> soDT = new ArrayList<>();
		Collections.reverse(listHocKys);
		for (HocKy hocKy : listHocKys) {
			System.out.println("HK" + hocKy.getSoHocKy() + "-20" + hocKy.getMaHocKy().substring(0, 2));
			tenHocKys.add(("HK" + hocKy.getSoHocKy() + "-20" + hocKy.getMaHocKy().substring(0, 2)));
			List<SinhVien> sinhViens = sinhVienService.layTatCaSinhVienTheoHocKy(hocKy.getMaHocKy(),
					hocKy.getSoHocKy());
			List<DeTai> deTais = deTaiService.layDsDeTaiTheoNamHocKy(hocKy.getMaHocKy(), hocKy.getSoHocKy());
			soDT.add((long) deTais.size());
			soSV.add((long) sinhViens.size());
		}
		DataThongKeDto dataDeTai = new DataThongKeDto("Đề tài", soDT);
		DataThongKeDto dataSinhVien = new DataThongKeDto("Sinh viên", soSV);
		thongKeDtos.add(dataSinhVien);
		thongKeDtos.add(dataDeTai);

		return new ThongKeDto(tenHocKys, thongKeDtos);
	}

	@GetMapping("/thong-ke-sv-co-nhom")
	public ThongKeDto thongKeSvDangKyDT() {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		List<DataThongKeDto> thongKeDtos = new ArrayList<>();
		List<Long> soSV = new ArrayList<>();
		Long soSvDKDeTai = (long) 0;
		Long soSvChuaDKDeTai = (long) 0;
		for (SinhVien sv : sinhVienService.layTatCaSinhVienTheoHocKy(hocKy.getMaHocKy(), hocKy.getSoHocKy())) {
			if (sv.getNhom() != null) {
				soSvDKDeTai += 1;
			} else {
				soSvChuaDKDeTai += 1;
			}
		}
		soSV.add(soSvDKDeTai);
		soSV.add(soSvChuaDKDeTai);
		DataThongKeDto dataDeTai = new DataThongKeDto("Số lượng:", soSV);
		thongKeDtos.add(dataDeTai);

		return new ThongKeDto(Arrays.asList("Đã đăng ký", "Chưa được đăng ký"), thongKeDtos);
	}

	@GetMapping("/thong-ke-nhom-detai")
	public ThongKeDto thongKeDTDangKy() {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		List<DataThongKeDto> thongKeDtos = new ArrayList<>();
		List<Long> soSV = new ArrayList<>();
		Long soDTDK = (long) 0;
		Long soDTChuaDK = (long) 0;
		for (Nhom nhom : nhomService.layTatCaNhomTheoTinhTrang(hocKy.getMaHocKy(), hocKy.getSoHocKy(), 1)) {
			if (nhom.getDeTai() != null) {
				soDTDK += +1;
			} else {
				soDTChuaDK += 1;
			}
		}
		soSV.add(soDTDK);
		soSV.add(soDTChuaDK);
		DataThongKeDto dataDeTai = new DataThongKeDto("Số lượng:", soSV);
		thongKeDtos.add(dataDeTai);

		return new ThongKeDto(Arrays.asList("Đã đăng ký", "Chưa được đăng ký"), thongKeDtos);
	}

	@GetMapping("/thong-ke-detai-giangvien")
	public ThongKeDto thongKeDTGiangVien() {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		List<GiangVien> dsGiangVien = giangVienService.layDanhSach();
		List<String> tenGV = new ArrayList<>();
		List<DataThongKeDto> thongKeDtos = new ArrayList<>();
		List<Long> listsoDTDK = new ArrayList<>();
		List<Long> listsoChuaDTDK = new ArrayList<>();
		dsGiangVien.remove(0);
		for (GiangVien gv : dsGiangVien) {
			tenGV.add(gv.getTenGiangVien());
			Long soDTDK = (long) 0;
			Long soDTChuaDK = (long) 0;
			for (DeTai deTai : deTaiService.layDsDeTaiTheoNamHocKy(hocKy.getMaHocKy(), hocKy.getSoHocKy(),
					gv.getMaGiangVien())) {
				int soNhom = deTaiService.laySoNhomDaDangKyDeTai(deTai.getMaDeTai()) == null ? 0
						: deTaiService.laySoNhomDaDangKyDeTai(deTai.getMaDeTai());
				if (soNhom > 0) {
					soDTDK += 1;
				} else {
					soDTChuaDK += 1;
				}
			}
			listsoChuaDTDK.add(soDTChuaDK);
			listsoDTDK.add(soDTDK);

		}
		DataThongKeDto dataDeTai = new DataThongKeDto("đã được đăng ký", listsoDTDK);
		DataThongKeDto dataDeTaiChuaDK = new DataThongKeDto("chưa được đăng ký", listsoChuaDTDK);
		thongKeDtos.add(dataDeTai);
		thongKeDtos.add(dataDeTaiChuaDK);

		return new ThongKeDto(tenGV, thongKeDtos);
	}

	@GetMapping("/thong-ke-giangvien-cothenhan")
	public ThongKeDto thongKeSoNhomGiangVien() {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		List<GiangVien> dsGiangVien = giangVienService.layDanhSach();
		List<String> tenGV = new ArrayList<>();
		List<DataThongKeDto> thongKeDtos = new ArrayList<>();
		List<Long> listsoDTDK = new ArrayList<>();
		dsGiangVien.remove(0);
		for (GiangVien gv : dsGiangVien) {
			tenGV.add(gv.getTenGiangVien());
			Long soNhomCon = (long) 0;
			for (DeTai deTai : deTaiService.layDsDeTaiTheoNamHocKyTheoTrangThai(hocKy.getMaHocKy(), hocKy.getSoHocKy(),
					gv.getMaGiangVien(), 2)) {
				int soNhom = deTaiService.laySoNhomDaDangKyDeTai(deTai.getMaDeTai()) == null ? 0
						: deTaiService.laySoNhomDaDangKyDeTai(deTai.getMaDeTai());
				if (soNhom > 0 && deTai.getGioiHanSoNhomThucHien()
						- deTaiService.laySoNhomDaDangKyDeTai(deTai.getMaDeTai()) > 0) {
					soNhomCon += deTai.getGioiHanSoNhomThucHien()
							- deTaiService.laySoNhomDaDangKyDeTai(deTai.getMaDeTai());
				} else {
					soNhomCon += deTai.getGioiHanSoNhomThucHien();
				}
			}
			listsoDTDK.add(soNhomCon);
		}
		DataThongKeDto dataDeTai = new DataThongKeDto("Số nhóm còn có thể hướng dẫn", listsoDTDK);
		thongKeDtos.add(dataDeTai);

		return new ThongKeDto(tenGV, thongKeDtos);
	}

	@GetMapping("/ds-giangvien-chuadu-so-detai-toithieu/{soDeTaiToiThieu}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public List<GiangVienDtoChuaCoSoDTTT> getGiangVienChuaDuSoDTToiThieu(
			@PathVariable("soDeTaiToiThieu") Integer soDeTaiToiThieu) {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		List<GiangVien> ListGV = giangVienService.layDanhSach();
		ListGV.remove(giangVienService.layTheoMa("12392401"));
		List<GiangVienDtoChuaCoSoDTTT> result = new ArrayList<>();
		for (GiangVien giangVien : ListGV) {
			int soLuongDeTaiDaCo = giangVienService.layDanhSachTT(giangVien.getMaGiangVien(), hocKy.getMaHocKy());
			if (soLuongDeTaiDaCo < soDeTaiToiThieu) {
				result.add(new GiangVienDtoChuaCoSoDTTT(giangVien.getMaGiangVien(), giangVien.getTenGiangVien(),
						soLuongDeTaiDaCo));
			}

		}
		return result;
	}

	@GetMapping("/ds-sinhvien-chua-dangky-nhom")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public List<SinhVienNhomVaiTroDto> getSinhVienChuaDKNHom() {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		List<SinhVien> ListGV = sinhVienService.layTatCaSinhVienTheoHocKy(hocKy.getMaHocKy(), hocKy.getSoHocKy());
		List<SinhVienNhomVaiTroDto> result = new ArrayList<>();
		for (SinhVien giangVien : ListGV) {
			if (giangVien.getNhom() == null) {
				result.add(new SinhVienNhomVaiTroDto(giangVien.getMaSinhVien(), giangVien.getTenSinhVien()));

			}

		}
		return result;
	}

	@GetMapping("/ds-sinhvien-chua-dangky-detai")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public List<SinhVienNhomVaiTroDto> getSinhVienChuaDKDT() {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		List<Nhom> nhoms = nhomService.layTatCaNhomTheoTinhTrang(hocKy.getMaHocKy(), hocKy.getSoHocKy(), 1);
		List<SinhVienNhomVaiTroDto> result = new ArrayList<>();
		for (Nhom nhom : nhoms) {
			if (nhom.getDeTai() == null) {
				sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom()).forEach(ma -> {
					SinhVien sv = sinhVienService.layTheoMa(ma);
					result.add(new SinhVienNhomVaiTroDto(sv.getMaSinhVien(), sv.getTenSinhVien()));
				});
			}

		}
		return result;
	}
	
	@GetMapping("/thong-ke-mucdokho-detai")
	public ThongKeDto thongKeMucDoKhoCuaDeTai() {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		List<GiangVien> dsGiangVien = giangVienService.layDanhSach();
		List<String> tenGV = new ArrayList<>();
		List<DataThongKeDto> thongKeDtos = new ArrayList<>();
		List<Long> listsoDTDK = new ArrayList<>();
		dsGiangVien.remove(0);
		for (GiangVien gv : dsGiangVien) {
			tenGV.add(gv.getTenGiangVien());
			Long soNhomCon = (long) 0;
			for (DeTai deTai : deTaiService.layDsDeTaiTheoNamHocKyTheoTrangThai(hocKy.getMaHocKy(), hocKy.getSoHocKy(),
					gv.getMaGiangVien(), 2)) {
				int soNhom = deTaiService.laySoNhomDaDangKyDeTai(deTai.getMaDeTai()) == null ? 0
						: deTaiService.laySoNhomDaDangKyDeTai(deTai.getMaDeTai());
				if (soNhom > 0 && deTai.getGioiHanSoNhomThucHien()
						- deTaiService.laySoNhomDaDangKyDeTai(deTai.getMaDeTai()) > 0) {
					soNhomCon += deTai.getGioiHanSoNhomThucHien()
							- deTaiService.laySoNhomDaDangKyDeTai(deTai.getMaDeTai());
				} else {
					soNhomCon += deTai.getGioiHanSoNhomThucHien();
				}
			}
			listsoDTDK.add(soNhomCon);
		}
		DataThongKeDto dataDeTai = new DataThongKeDto("Số nhóm còn có thể hướng dẫn", listsoDTDK);
		thongKeDtos.add(dataDeTai);

		return new ThongKeDto(tenGV, thongKeDtos);
	}
	
	@GetMapping("/thong-ke-mucdokho-detai-giangvien")
	public ThongKeDto thongKeMucDoKhoCuaDeTaiGiangVien() {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		List<GiangVien> dsGiangVien = giangVienService.layDanhSach();
		List<String> tenGV = new ArrayList<>();
		List<DataThongKeDto> thongKeDtos = new ArrayList<>();
		List<Long> listsoDTDK = new ArrayList<>();
		dsGiangVien.remove(0);
		for (GiangVien gv : dsGiangVien) {
			tenGV.add(gv.getTenGiangVien());
			Long soNhomCon = (long) 0;
			for (DeTai deTai : deTaiService.layDsDeTaiTheoNamHocKyTheoTrangThai(hocKy.getMaHocKy(), hocKy.getSoHocKy(),
					gv.getMaGiangVien(), 2)) {
				int soNhom = deTaiService.laySoNhomDaDangKyDeTai(deTai.getMaDeTai()) == null ? 0
						: deTaiService.laySoNhomDaDangKyDeTai(deTai.getMaDeTai());
				if (soNhom > 0 && deTai.getGioiHanSoNhomThucHien()
						- deTaiService.laySoNhomDaDangKyDeTai(deTai.getMaDeTai()) > 0) {
					soNhomCon += deTai.getGioiHanSoNhomThucHien()
							- deTaiService.laySoNhomDaDangKyDeTai(deTai.getMaDeTai());
				} else {
					soNhomCon += deTai.getGioiHanSoNhomThucHien();
				}
			}
			listsoDTDK.add(soNhomCon);
		}
		DataThongKeDto dataDeTai = new DataThongKeDto("Số nhóm còn có thể hướng dẫn", listsoDTDK);
		thongKeDtos.add(dataDeTai);

		return new ThongKeDto(tenGV, thongKeDtos);
	}
}
