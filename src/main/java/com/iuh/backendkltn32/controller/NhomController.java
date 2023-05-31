package com.iuh.backendkltn32.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.iuh.backendkltn32.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.KeHoach;
import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.entity.TinNhan;
import com.iuh.backendkltn32.jms.JmsListenerConsumer;
import com.iuh.backendkltn32.jms.JmsPublishProducer;
import com.iuh.backendkltn32.service.HocKyService;
import com.iuh.backendkltn32.service.KeHoachService;
import com.iuh.backendkltn32.service.NhomService;
import com.iuh.backendkltn32.service.PhanCongService;
import com.iuh.backendkltn32.service.SinhVienService;
import com.iuh.backendkltn32.service.TinNhanSerivce;

@RestController
@RequestMapping("/api/nhom")
public class NhomController {

	@Autowired
	private JmsPublishProducer producer;

	@Autowired
	private JmsListenerConsumer listenerConsumer;

	@Autowired
	private NhomService nhomService;

	@Autowired
	private SinhVienService sinhVienService;

	@Autowired
	private HocKyService hocKyService;

	@Autowired
	private KeHoachService keHoachService;

	@Autowired
	private PhanCongService phanCongService;

	@Autowired
	private TinNhanSerivce tinNhanSerivce;

	/*
	 * // case1: tao moi -> check: co nhom chua ???? // TH1: -> chua co //TH2: -> co
	 * roi -> XONG XONG XONG
	 */
	@PostMapping("/dang-ky-nhom")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> dangKyNhom(@RequestBody DangKyNhomRequest request) throws RuntimeException {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		List<KeHoach> keHoachs = keHoachService.layTheoTenVaMaHocKyVaiTro(hocKy.getMaHocKy(), "Lịch đăng ký nhóm",
				request.getVaiTro());
		if (keHoachs.size() > 0) {
			KeHoach keHoach = keHoachs.get(0);
			if (keHoach.getThoiGianBatDau().getTime() > System.currentTimeMillis()) {
				throw new RuntimeException("Chưa đến thời gian để đăng ký nhóm");
			} else if (keHoach.getThoiGianKetThuc().getTime() < System.currentTimeMillis()) {
				throw new RuntimeException("Thời gian đăng ký nhóm đã hết");
			}

			producer.sendMessageOnNhomChanel(request);
			try {
				return listenerConsumer.listenerNhomChannel();
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		throw new RuntimeException("Chưa có kế hoạch đăng ký nhóm");

	}

	@PostMapping("/gan-nhom-tudong")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public void ganNhomTuDong(@RequestBody List<String> dsMaSinhVien) throws RuntimeException {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		List<KeHoach> keHoachs = keHoachService.layTheoTenVaMaHocKyVaiTro(hocKy.getMaHocKy(), "Lịch đăng ký nhóm",
				"ROLE_SINHVIEN");
		if (keHoachs.size() > 0) {
			KeHoach keHoach = keHoachs.get(0);
			if (keHoach.getThoiGianBatDau().getTime() > System.currentTimeMillis()) {
				throw new RuntimeException("Chưa đến thời gian để đăng ký nhóm");
			} else if (keHoach.getThoiGianKetThuc().getTime() < System.currentTimeMillis()) {
				throw new RuntimeException("Thời gian đăng ký nhóm đã hết");
			}
			DangKyNhomRequest dangKyNhomRequest = new DangKyNhomRequest();
			dangKyNhomRequest.setVaiTro("ROLE_QUANLY");
			List<String> masvreq = new ArrayList<>();
			if (dsMaSinhVien.size() % 2 != 0) {
				masvreq.add(dsMaSinhVien.get(dsMaSinhVien.size() - 1));
				dangKyNhomRequest.setDsMaSinhVien(masvreq);
				producer.sendMessageOnNhomChanel(dangKyNhomRequest);
				try {
					listenerConsumer.listenerNhomChannel();
					masvreq.clear();
					dangKyNhomRequest = new DangKyNhomRequest();
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage());
				}
				dsMaSinhVien.remove(dsMaSinhVien.size() - 1);
			}

			for (String maSV : dsMaSinhVien) {
				masvreq.add(maSV);
				if (masvreq.size() % 2 == 0) {
					dangKyNhomRequest.setDsMaSinhVien(masvreq);
					producer.sendMessageOnNhomChanel(dangKyNhomRequest);
					try {
						listenerConsumer.listenerNhomChannel();
						masvreq.clear();
						dangKyNhomRequest.setDsMaSinhVien(new ArrayList<>());
					} catch (Exception e) {
						throw new RuntimeException(e.getMessage());
					}
				}

			}
		} else {
			
			throw new RuntimeException("Chưa có kế hoạch đăng ký nhóm");
		}

	}

	@PostMapping("/lay-ds-nhom")
	@PreAuthorize("hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_SINHVIEN')")
	public Set<NhomRoleGVRespone> layNhomTheoMaGv(@RequestBody LayDeTaiRquestDto request) throws RuntimeException {
		List<Nhom> nhoms = new ArrayList<>();
		Set<NhomRoleGVRespone> respones = new HashSet<>();
		if (request.getTrangThai() == null && request.getMaGiangVien() == null) {
			nhoms = nhomService.layTatCaNhom(request.getMaHocKy(), request.getSoHocKy());
			if (!nhoms.isEmpty() && nhoms != null) {
				nhoms.stream().forEach(nhom -> {
					List<String> sinhViens = sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom());
					List<SinhVien> sinhViens2 = sinhViens.stream().map(sv -> {
						try {
							return sinhVienService.layTheoMa(sv);
						} catch (RuntimeException e) {
							e.printStackTrace();
						}
						return null;
					}).toList();
					NhomRoleGVRespone nhomRoleGVRespone = new NhomRoleGVRespone(nhom,
							nhom.getDeTai() == null ? null : nhom.getDeTai().getMaDeTai(), sinhViens, sinhViens2);
					respones.add(nhomRoleGVRespone);
				});

			}
		}

		if (request.getTrangThai() != null && request.getMaGiangVien() == null) {
			nhoms = nhomService.layTatCaNhomTheoTinhTrang(request.getMaHocKy(), request.getSoHocKy(),
					request.getTrangThai());

			if (!nhoms.isEmpty() && nhoms != null) {
				nhoms.stream().forEach(nhom -> {
					List<String> sinhViens = sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom());
					List<SinhVien> sinhViens2 = sinhViens.stream().map(sv -> {
						try {
							return sinhVienService.layTheoMa(sv);
						} catch (RuntimeException e) {
							e.printStackTrace();
						}
						return null;
					}).toList();
					NhomRoleGVRespone nhomRoleGVRespone = new NhomRoleGVRespone(nhom, null, sinhViens, sinhViens2);
					respones.add(nhomRoleGVRespone);
				});

			}
		}

		if (request.getMaGiangVien() != null) {
			nhoms = nhomService.layDSNhomTheMaGiangVien(request.getMaHocKy(), request.getSoHocKy(),
					request.getMaGiangVien());
			if (!nhoms.isEmpty() && nhoms != null) {
				nhoms.stream().forEach(nhom -> {
					List<String> sinhViens = sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom());
					List<SinhVien> sinhViens2 = sinhViens.stream().map(sv -> {
						try {
							return sinhVienService.layTheoMa(sv);
						} catch (RuntimeException e) {
							e.printStackTrace();
						}
						return null;
					}).toList();
					NhomRoleGVRespone nhomRoleGVRespone = new NhomRoleGVRespone(nhom, nhom.getDeTai().getMaDeTai(),
							sinhViens, sinhViens2);
					respones.add(nhomRoleGVRespone);
				});

			}

		}
		return respones;
	}

	@GetMapping("/lay-ds-nhom/{tinhTrang}")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public Set<NhomRoleGVRespone> layNhomTheoTinhTrang(@PathVariable("tinhTrang") Integer tinhTrang)
			throws RuntimeException {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		List<Nhom> nhoms = nhomService.layTatCaNhomTheoTinhTrang(hocKy.getMaHocKy(), hocKy.getSoHocKy(), tinhTrang);
		Set<NhomRoleGVRespone> respones = new HashSet<>();
		if (!nhoms.isEmpty() && nhoms != null) {
			nhoms.stream().forEach(nhom -> {
				List<String> sinhViens = sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom());
				List<SinhVien> sinhViens2 = sinhViens.stream().map(sv -> {
					try {
						return sinhVienService.layTheoMa(sv);
					} catch (RuntimeException e) {
						e.printStackTrace();
					}
					return null;
				}).toList();
				NhomRoleGVRespone nhomRoleGVRespone = new NhomRoleGVRespone(nhom, null, sinhViens, sinhViens2);
				if (!respones.contains(nhomRoleGVRespone)) {
					respones.add(nhomRoleGVRespone);
				}
			});

		}
		return respones;
	}

	@PostMapping("/lay-ds-nhom-phan-bien")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public Set<NhomPBResponeDto> layNhomPB(@RequestBody LayDsNhomPBDto request) throws RuntimeException {
		List<Nhom> nhoms = nhomService.layTatCaNhomTheoTinhTrang(request.getMaHocKy(), request.getSoHocKy(), 1);
		String valid = request.getVaiTro().equals("PB") ? "HD" : "PB";
		Set<NhomPBResponeDto> respones = new HashSet<>();
		if (!nhoms.isEmpty() && nhoms != null) {
			nhoms.stream().forEach(nhom -> {
				if (sinhVienService.timMaSinhVienChuaCoPhieuChamDiemTheoNhuCauCoDiemTheoNhuCau(nhom.getMaNhom(),
						valid) != null
						&& phanCongService.layPhanCongTheoMaNhomVaTen(nhom.getMaNhom(),
								request.getVaiTro().equals("PB") ? "phan bien" : "chu tich").size() == 0) {
					Map<String, String> sinhViens = new HashMap<>();
					sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom()).stream().forEach(sv -> {
						try {
							sinhViens.put(sv, sinhVienService.layTheoMa(sv).getTenSinhVien());
						} catch (RuntimeException e) {
							e.printStackTrace();
						}
					});
					List<String> tenGiangVienPBs = phanCongService.layPhanCongTheoMaNhom(nhom).stream()
							.map(pc -> pc.getGiangVien().getTenGiangVien()).toList();
					List<String> ma = phanCongService.layPhanCongTheoMaNhom(nhom).stream()
							.map(pc -> pc.getGiangVien().getMaGiangVien()).toList();
					List<KeHoach> keHoachs = keHoachService.layKeHoachTheoMaHocKyVaMaLoai(request.getMaHocKy(), "3",
							ma.size() > 0 ? ma.get(0) : "");
					@SuppressWarnings("deprecation")
					NhomPBResponeDto nhomRoleGVRespone = new NhomPBResponeDto(nhom.getMaNhom(), nhom.getTenNhom(),
							nhom.getDeTai().getMaDeTai(), nhom.getDeTai().getTenDeTai(), sinhViens,
							nhom.getDeTai().getGiangVien().getTenGiangVien(), tenGiangVienPBs,
							nhom.getDeTai().getGiangVien().getMaGiangVien(),
							keHoachs.size() > 0 ? keHoachs.get(0).getThoiGianBatDau() : null,
							keHoachs.size() > 0 ? keHoachs.get(0).getThoiGianKetThuc() : null,
							keHoachs.size() > 0 ? (keHoachs.get(0).getThoiGianBatDau().getHours() - 5) + "" : null,
							keHoachs.size() > 0 ? (keHoachs.get(0).getThoiGianKetThuc().getHours() - 5) + "" : null,
							keHoachs.size() > 0 ? keHoachs.get(0).getChuThich() : "");
					if (!respones.contains(nhomRoleGVRespone)) {
						respones.add(nhomRoleGVRespone);
					}
				}
			});
		}
		return respones;
	}

	@GetMapping("/lay-nhom/{maNhom}")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_SINHVIEN')")
	public NhomSinhVienDto layNhom(@PathVariable("maNhom") String maNhom) throws RuntimeException {
		Nhom nhom = nhomService.layTheoMa(maNhom);
		List<SinhVien> sinhViens = sinhVienService.layTatCaSinhVienTheoNhom(maNhom).stream().map(ma -> {
			try {
				return sinhVienService.layTheoMa(ma);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			return null;
		}).toList();

		NhomSinhVienDto respones = new NhomSinhVienDto(nhom, sinhViens, nhom.getTinhTrang(), nhom.getDeTai());

		return respones;
	}

	@PostMapping("/dang-ky-co-san")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> dangKyNhomCoSan(@RequestBody DangKyNhomRequest request) throws RuntimeException {
		List<String> sinhVienTrongNhom = sinhVienService.layTatCaSinhVienTheoNhom(request.getMaNhom());
		if (sinhVienTrongNhom.size() >= 2) {
			throw new RuntimeException("Nhóm Đã Đủ Thành Viên");
		}
		SinhVien sinhVienXinGiaNhap = sinhVienService.layTheoMa(request.getDsMaSinhVien().get(0));
		TinNhan tinNhan = new TinNhan("Có Sinh Viên Muốn xin gia nhập nhóm của bạn",
				"Có Sinh Viên Muốn xin gia nhập nhóm của bạn | " + sinhVienXinGiaNhap.getMaSinhVien() + " | "
						+ sinhVienXinGiaNhap.getTenSinhVien() + " | " + request.getPassword(),
				request.getDsMaSinhVien().get(0), sinhVienTrongNhom.get(0), 0,
				new Timestamp(System.currentTimeMillis()));
		tinNhanSerivce.luu(tinNhan);
		return ResponseEntity.ok(tinNhan);
	}

	@PostMapping("/roi-nhom")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> roiKhoiNhomDaDK(@RequestBody DangKyNhomRequest request) throws RuntimeException {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		List<KeHoach> keHoachs = keHoachService.layTheoTenVaMaHocKyVaiTro(hocKy.getMaHocKy(), "Lịch đăng ký nhóm",
				request.getVaiTro());
		if (keHoachs.size() > 0) {
			KeHoach keHoach = keHoachs.get(0);
			if (keHoach.getThoiGianBatDau().getTime() > System.currentTimeMillis()) {
				throw new RuntimeException("Chưa đến thời gian để đăng ký nhóm");
			} else if (keHoach.getThoiGianKetThuc().getTime() < System.currentTimeMillis()) {
				throw new RuntimeException("Thời gian đăng ký nhóm đã hết");
			}

			try {
				SinhVien sinhVien = sinhVienService.layTheoMa(request.getDsMaSinhVien().get(0));
				if (sinhVien.getNhom() == null) {
					return ResponseEntity.status(500).body("Chua co nhom de roi");
				}
				if (request.getMaNhom() == null) {
					return ResponseEntity.status(500).body("Ma nhom bang null");
				}
				if (sinhVien.getNhom().getDeTai() != null || sinhVien.getNhom().getTinhTrang() == 1) {
					return ResponseEntity.status(500).body("Không thể rời nhóm");
				}
				sinhVien.setNhom(null);
				sinhVienService.capNhat(sinhVien);
				if (nhomService.laySoSinhVienTrongNhomTheoMa(request.getMaNhom()) == null) {
					nhomService.xoa(request.getMaNhom());
				}
				return ResponseEntity.ok(sinhVien);
			} catch (RuntimeException e) {
				e.printStackTrace();
				return ResponseEntity.status(500).body(e.getMessage());
			}
		} else {
			throw new RuntimeException("Chưa có kế hoạch đăng ký nhóm");
		}

	}

	@PostMapping("/lay-ds-nhom-theo-vai-tro")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY')")
	public Set<NhomVaiTro> layNhomvaitro(@RequestBody LayKeHoachRequest request) throws RuntimeException {
		HocKy hocKy = hocKyService.layTheoMa(request.getMaHocKy());
		Set<NhomVaiTro> respones = new HashSet<>();
		if (request.getVaiTro().equals("HD")) {
			List<Nhom> nhoms = nhomService.layDSNhomTheMaGiangVien(hocKy.getMaHocKy(), hocKy.getSoHocKy(),
					request.getMaNguoiDung());
			if (!nhoms.isEmpty() && nhoms != null) {
				nhoms.stream().forEach(nhom -> {
					String maGV = request.getMaNguoiDung();
					if (!sinhVienService
							.timMaSinhVienChuaCoPhieuChamDiemTheoNhuCau(nhom.getMaNhom(), request.getVaiTro(), maGV)
							.contains(nhom.getMaNhom())) {
						List<SinhVienNhomVaiTroDto> sinhViens = new ArrayList<>();
						sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom()).stream().forEach(sv -> {
							try {
								SinhVien sv1 = sinhVienService.layTheoMa(sv);
								sinhViens.add(new SinhVienNhomVaiTroDto(sv1.getMaSinhVien(), sv1.getTenSinhVien()));

							} catch (RuntimeException e) {
								e.printStackTrace();
							}
						});
						respones.add(new NhomVaiTro(nhom.getMaNhom(), nhom.getTenNhom(), nhom.getDeTai().getMaDeTai(),
								nhom.getDeTai().getTenDeTai(), sinhViens));

					}
				});
			}
		} else {
			System.out.println(request.getVaiTro());
			String viTriPhanCong = "";
			switch (request.getVaiTro()) {
			case "PB":
				viTriPhanCong = "phan bien";
				break;
			case "CT":
				viTriPhanCong = "chu tich";
				break;
			case "TK":
				viTriPhanCong = "thu ky";
				break;
			case "TV3":
				viTriPhanCong = "thanh vien 3";
				break;
			default:
				viTriPhanCong = "phan bien";
				break;
			}
			List<Nhom> nhoms = nhomService.layNhomTheoVaiTro(hocKy.getMaHocKy(), viTriPhanCong,
					request.getMaNguoiDung());
			if (!nhoms.isEmpty() && nhoms != null) {
				nhoms.stream().forEach(nhom -> {
					String maGV = request.getMaNguoiDung();
					if (!sinhVienService
							.timMaSinhVienChuaCoPhieuChamDiemTheoNhuCau(nhom.getMaNhom(), request.getVaiTro(), maGV)
							.contains(nhom.getMaNhom())
							|| sinhVienService.timMaSinhVienChuaCoPhieuChamDiemTheoNhuCau(nhom.getMaNhom(),
									request.getVaiTro(), maGV).size() == 0) {
						List<SinhVienNhomVaiTroDto> sinhViens = new ArrayList<>();
						sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom()).stream().forEach(sv -> {
							try {

								SinhVien sv1 = sinhVienService.layTheoMa(sv);
								sinhViens.add(new SinhVienNhomVaiTroDto(sv1.getMaSinhVien(), sv1.getTenSinhVien()));
							} catch (RuntimeException e) {
								e.printStackTrace();
							}
						});
						respones.add(new NhomVaiTro(nhom.getMaNhom(), nhom.getTenNhom(), nhom.getDeTai().getMaDeTai(),
								nhom.getDeTai().getTenDeTai(), sinhViens));
					}
				});

			}
		}
		return respones;
	}

	@PostMapping("/lay-ds-nhom-theo-vai-tro-cu-the")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY')")
	public Set<NhomVaiTro> layNhomvaitroCuThe(@RequestBody NhomVaiTroRequest request) throws RuntimeException {
		HocKy hocKy = null;
		if (request.getMaHocKy() == null) {
			hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		} else {
			hocKy = hocKyService.layTheoMa(request.getMaHocKy());
		}
		Set<NhomVaiTro> respones = new HashSet<>();
		String viTriCham = request.getDotCham() != null && !request.getDotCham().isEmpty()
				&& !request.getDotCham().isBlank() && !request.getDotCham().equals("") ? request.getDotCham() : "All";
		System.out.println(viTriCham);
		if (viTriCham.equals("All")) {
			System.out.println("All");
			List<Nhom> nhomAs = nhomService.layDSNhomTheMaGiangVien(hocKy.getMaHocKy(), hocKy.getSoHocKy(),
					request.getMaNguoiDung());
			if (!nhomAs.isEmpty() && nhomAs != null) {
				nhomAs.stream().forEach(nhom -> {
					String maGV = request.getMaNguoiDung();
					if (xuatSinhVienKhiCan(nhom, "HD", maGV).size() > 0) {
						respones.add(new NhomVaiTro(nhom.getMaNhom(), nhom.getTenNhom(), nhom.getDeTai().getMaDeTai(),
								nhom.getDeTai().getTenDeTai(), xuatSinhVienKhiCan(nhom, "HD", maGV), "HD"));
					}
				});
			}
			for (String viTriCham2 : Arrays.asList("PB", "CT", "TK", "TV3")) {
				String vaiTroCham = "";
				switch (viTriCham2) {
				case "CT":
					vaiTroCham = "chu tich";
					break;
				case "TK":
					vaiTroCham = "thu ky";
					break;
				case "TV3":
					vaiTroCham = "thanh vien 3";
					break;
				case "PB":
					vaiTroCham = "phan bien";
					break;
				}
				Set<Nhom> nhomBs = new HashSet<>(
						nhomService.layNhomTheoVaiTro(hocKy.getMaHocKy(), vaiTroCham, request.getMaNguoiDung()));
				if (!nhomBs.isEmpty() && nhomBs != null) {
					nhomBs.stream().forEach(nhom -> {
						String maGV = request.getMaNguoiDung();
						if (xuatSinhVienKhiCan(nhom, viTriCham2, maGV).size() > 0) {
							respones.add(new NhomVaiTro(nhom.getMaNhom(), nhom.getTenNhom(),
									nhom.getDeTai().getMaDeTai(), nhom.getDeTai().getTenDeTai(),
									xuatSinhVienKhiCan(nhom, viTriCham2, maGV), viTriCham2));
						}
					});
					System.out.println(respones);
				}
			}

			return respones;
		}

		if (viTriCham.equals("HD")) {
			System.out.println("HUOng Dan");
			List<Nhom> nhoms = nhomService.layDSNhomTheMaGiangVien(hocKy.getMaHocKy(), hocKy.getSoHocKy(),
					request.getMaNguoiDung());
			if (!nhoms.isEmpty() && nhoms != null) {
				nhoms.stream().forEach(nhom -> {
					String maGV = request.getMaNguoiDung();
					if (xuatSinhVienKhiCan(nhom, viTriCham, maGV).size() > 0) {
						respones.add(new NhomVaiTro(nhom.getMaNhom(), nhom.getTenNhom(), nhom.getDeTai().getMaDeTai(),
								nhom.getDeTai().getTenDeTai(), xuatSinhVienKhiCan(nhom, request.getDotCham(), maGV),
								"HD"));
					}
				});
			}
		} else if (viTriCham.equals("PB")) {
			System.out.println("Phan Bien");
			List<Nhom> nhoms = nhomService.layNhomTheoVaiTro(hocKy.getMaHocKy(), "phan bien", request.getMaNguoiDung());
			if (!nhoms.isEmpty() && nhoms != null) {
				nhoms.stream().forEach(nhom -> {
					String maGV = request.getMaNguoiDung();
					if (xuatSinhVienKhiCan(nhom, viTriCham, maGV).size() > 0) {
						respones.add(new NhomVaiTro(nhom.getMaNhom(), nhom.getTenNhom(), nhom.getDeTai().getMaDeTai(),
								nhom.getDeTai().getTenDeTai(), xuatSinhVienKhiCan(nhom, request.getDotCham(), maGV),
								"PB"));
					}
				});
			}
		} else if (viTriCham.equals("Hoi Dong")) {
			System.out.println("Hoi Dong");
			List<String> vaiTroTemp = request.getVaitro() != null ? request.getVaitro()
					: Arrays.asList("CT", "TK", "TV3");
			for (String vaiTro : vaiTroTemp) {
				String viTriPhanCong = "";
				switch (vaiTro) {
				case "CT":
					viTriPhanCong = "chu tich";
					break;
				case "TK":
					viTriPhanCong = "thu ky";
					break;
				case "TV3":
					viTriPhanCong = "thanh vien 3";
					break;
				}

				if (request.getPpcham().size() == 1) {
					if (request.getPpcham().get(0).equals("chamPoster")) {
						List<Nhom> nhoms = nhomService.layNhomTheoPPChamPoster(hocKy.getMaHocKy(),
								request.getMaNguoiDung(), viTriPhanCong);
						if (!nhoms.isEmpty() && nhoms != null) {
							nhoms.stream().forEach(nhom -> {
								String maGV = request.getMaNguoiDung();
								if (xuatSinhVienKhiCan(nhom, viTriCham, maGV).size() > 0) {
									respones.add(new NhomVaiTro(nhom.getMaNhom(), nhom.getTenNhom(),
											nhom.getDeTai().getMaDeTai(), nhom.getDeTai().getTenDeTai(),
											xuatSinhVienKhiCan(nhom, request.getDotCham(), maGV), vaiTro));
								}
							});
						}
					} else {
						List<Nhom> nhoms = nhomService.layNhomTheoPPChamHD(hocKy.getMaHocKy(), request.getMaNguoiDung(),
								viTriPhanCong);
						if (!nhoms.isEmpty() && nhoms != null) {
							nhoms.stream().forEach(nhom -> {
								respones.add(new NhomVaiTro(nhom.getMaNhom(), nhom.getTenNhom(),
										nhom.getDeTai().getMaDeTai(), nhom.getDeTai().getTenDeTai(),
										xuatSinhVienKhiCan(nhom, request.getDotCham(), request.getMaNguoiDung()),
										vaiTro));
							});
						}
					}
				} else {
					List<Nhom> nhomPoster = nhomService.layNhomTheoPPChamPoster(hocKy.getMaHocKy(),
							request.getMaNguoiDung(), vaiTro);
					if (!nhomPoster.isEmpty() && nhomPoster != null) {
						nhomPoster.stream().forEach(nhom -> {
							if (xuatSinhVienKhiCan(nhom, viTriCham, request.getMaNguoiDung()).size() > 0) {

								respones.add(new NhomVaiTro(nhom.getMaNhom(), nhom.getTenNhom(),
										nhom.getDeTai().getMaDeTai(), nhom.getDeTai().getTenDeTai(),
										xuatSinhVienKhiCan(nhom, request.getDotCham(), request.getMaNguoiDung()),
										vaiTro));
							}
						});
					}
					List<Nhom> nhomHD = nhomService.layNhomTheoPPChamHD(hocKy.getMaHocKy(), request.getMaNguoiDung(),
							vaiTro);
					if (!nhomHD.isEmpty() && nhomHD != null) {
						nhomHD.stream().forEach(nhom -> {
							if (xuatSinhVienKhiCan(nhom, viTriCham, request.getMaNguoiDung()).size() > 0) {
								respones.add(new NhomVaiTro(nhom.getMaNhom(), nhom.getTenNhom(),
										nhom.getDeTai().getMaDeTai(), nhom.getDeTai().getTenDeTai(),
										xuatSinhVienKhiCan(nhom, request.getDotCham(), request.getMaNguoiDung()),
										vaiTro));
							}
						});
					}
				}
			}
		}
		return respones;
	}

	private List<SinhVienNhomVaiTroDto> xuatSinhVienKhiCan(Nhom nhom, String dotCham, String maGV) {
		List<SinhVienNhomVaiTroDto> sinhViens = new ArrayList<>();

		if (!sinhVienService.timMaSinhVienChuaCoPhieuChamDiemTheoNhuCau(nhom.getMaNhom(), dotCham, maGV)
				.contains(nhom.getMaNhom())
				|| sinhVienService.timMaSinhVienChuaCoPhieuChamDiemTheoNhuCau(nhom.getMaNhom(), dotCham, maGV)
						.size() == 0) {

			sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom()).stream().forEach(sv -> {
				try {
					SinhVien sv1 = sinhVienService.layTheoMa(sv);
					sinhViens.add(new SinhVienNhomVaiTroDto(sv1.getMaSinhVien(), sv1.getTenSinhVien()));
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
			});
		}
		return sinhViens;
	}
	

	@GetMapping("/lay-ds-nhom-chua-dangky-detai")
	public List<Nhom> layDSDeTai() {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		List<Nhom> dsNhomChuaDKDT = new ArrayList<>();
		nhomService.layTatCaNhomTheoTinhTrang(hocKy.getMaHocKy(), hocKy.getSoHocKy(), 1).forEach(nhom -> {
			if (nhom.getDeTai() == null) {
				dsNhomChuaDKDT.add(nhom);
			}
		});
		return dsNhomChuaDKDT;
	}

}
