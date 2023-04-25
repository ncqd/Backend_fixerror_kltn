package com.iuh.backendkltn32.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.dto.DangKyNhomRequest;
import com.iuh.backendkltn32.dto.LayDeTaiRquestDto;
import com.iuh.backendkltn32.dto.NhomPBResponeDto;
import com.iuh.backendkltn32.dto.NhomRoleGVRespone;
import com.iuh.backendkltn32.dto.NhomSinhVienDto;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.KeHoach;
import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.PhanCong;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.jms.JmsListenerConsumer;
import com.iuh.backendkltn32.jms.JmsPublishProducer;
import com.iuh.backendkltn32.service.HocKyService;
import com.iuh.backendkltn32.service.KeHoachService;
import com.iuh.backendkltn32.service.NhomService;
import com.iuh.backendkltn32.service.PhanCongService;
import com.iuh.backendkltn32.service.SinhVienService;

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

	/*
	 * // case1: tao moi -> check: co nhom chua ???? // TH1: -> chua co //TH2: -> co
	 * roi -> XONG XONG XONG
	 */
	@PostMapping("/dang-ky-nhom")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> dangKyNhom(@RequestBody DangKyNhomRequest request) throws Exception {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		List<KeHoach> keHoachs = keHoachService.layTheoTenVaMaHocKyVaiTro(hocKy.getMaHocKy(), "Lịch đăng ký nhóm",
				request.getVaiTro());
		System.out.println("NhomController - dang ky nhom - " + request);
		if (keHoachs.size() > 0) {
			KeHoach keHoach = keHoachs.get(0);
			if (keHoach.getThoiGianBatDau().getTime() > System.currentTimeMillis()) {
				throw new Exception("Chưa đến thời gian để đăng ký nhóm");
			} else if (keHoach.getThoiGianKetThuc().getTime() < System.currentTimeMillis()) {
				throw new Exception("Thời gian đăng ký nhóm đã hết");
			}

			producer.sendMessageOnNhomChanel(request);
			return listenerConsumer.listenerNhomChannel();
		} else {
			throw new Exception("Chưa có kế hoạch đăng ký nhóm");
		}
	}

	@PostMapping("/lay-ds-nhom")
	@PreAuthorize("hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_SINHVIEN')")
	public Set<NhomRoleGVRespone> layNhomTheoMaGv(@RequestBody LayDeTaiRquestDto request) throws Exception {
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
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}).toList();
					NhomRoleGVRespone nhomRoleGVRespone = new NhomRoleGVRespone(nhom,
							null, sinhViens, sinhViens2);
					respones.add(nhomRoleGVRespone);
				});

			}
		}
		
		if (request.getTrangThai() != null && request.getMaGiangVien() == null) {
			nhoms = nhomService.layTatCaNhomTheoTinhTrang(request.getMaHocKy(), request.getSoHocKy(), request.getTrangThai());
			
			if (!nhoms.isEmpty() && nhoms != null) {
				nhoms.stream().forEach(nhom -> {
					List<String> sinhViens = sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom());
					List<SinhVien> sinhViens2 = sinhViens.stream().map(sv -> {
						try {
							return sinhVienService.layTheoMa(sv);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}).toList();
					NhomRoleGVRespone nhomRoleGVRespone = new NhomRoleGVRespone(nhom,
							null, sinhViens,sinhViens2);
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
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}).toList();
					NhomRoleGVRespone nhomRoleGVRespone = new NhomRoleGVRespone(nhom,
							nhom.getDeTai().getMaDeTai(), sinhViens, sinhViens2);
					respones.add(nhomRoleGVRespone);
				});

			}

		}
		return respones;
	}

	@GetMapping("/lay-ds-nhom/{tinhTrang}")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public Set<NhomRoleGVRespone> layNhomTheoTinhTrang(@PathVariable("tinhTrang") Integer tinhTrang) throws Exception {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		List<Nhom> nhoms = nhomService.layTatCaNhomTheoTinhTrang(hocKy.getMaHocKy(), hocKy.getSoHocKy(), tinhTrang);
		Set<NhomRoleGVRespone> respones = new HashSet<>();
		if (!nhoms.isEmpty() && nhoms != null) {
			nhoms.stream().forEach(nhom -> {
				List<String> sinhViens = sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom());
				List<SinhVien> sinhViens2 = sinhViens.stream().map(sv -> {
					try {
						return sinhVienService.layTheoMa(sv);
					} catch (Exception e) {
						// TODO Auto-generated catch block
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
	public Set<NhomPBResponeDto> layNhomPB(@RequestBody LayDeTaiRquestDto request) throws Exception {
		List<Nhom> nhoms = nhomService.layTatCaNhomTheoTinhTrang(request.getMaHocKy(), request.getSoHocKy(), 1);
		Set<NhomPBResponeDto> respones = new HashSet<>();
		if (!nhoms.isEmpty() && nhoms != null) {
			
			nhoms.stream().forEach(nhom -> {
				Map<String, String> sinhViens =  new HashMap<>();
				sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom()).stream().forEach(sv -> {
					try {
						sinhViens.put(sv, sinhVienService.layTheoMa(sv).getTenSinhVien());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				List<String> tenGiangVienPBs = phanCongService.layPhanCongTheoMaNhom(nhom).stream().map(pc-> pc.getGiangVien().getTenGiangVien()).toList();
				List<String> ma = phanCongService.layPhanCongTheoMaNhom(nhom).stream().map(pc-> pc.getGiangVien().getMaGiangVien()).toList();
				List<KeHoach> keHoachs = keHoachService.layKeHoachTheoMaHocKyVaMaLoai(request.getMaHocKy(), "3", ma.get(0));
				NhomPBResponeDto nhomRoleGVRespone = new NhomPBResponeDto(nhom.getMaNhom(), nhom.getTenNhom(), nhom.getDeTai().getMaDeTai(), 
						nhom.getDeTai().getTenDeTai(), sinhViens, nhom.getDeTai().getGiangVien().getTenGiangVien(), tenGiangVienPBs, 
						nhom.getDeTai().getGiangVien().getMaGiangVien(), 
						keHoachs.size() > 0 ? keHoachs.get(0).getThoiGianBatDau() : null, 
						keHoachs.size() > 0?  keHoachs.get(0).getThoiGianKetThuc(): null,
						keHoachs.size() > 0? (keHoachs.get(0).getThoiGianBatDau().getHours()-5) + "": null , 
						keHoachs.size() > 0? (keHoachs.get(0).getThoiGianKetThuc().getHours()-5) + "": null ,		
						keHoachs.size() > 0?keHoachs.get(0).getChuThich() : "");
				if (!respones.contains(nhomRoleGVRespone)) {

					respones.add(nhomRoleGVRespone);
				}
			});

		}
		return respones;
	}

	@GetMapping("/lay-nhom/{maNhom}")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_SINHVIEN')")
	public NhomSinhVienDto layNhom(@PathVariable("maNhom") String maNhom) throws Exception {
		Nhom nhom = nhomService.layTheoMa(maNhom);
		List<SinhVien> sinhViens = sinhVienService.layTatCaSinhVienTheoNhom(maNhom).stream().map(ma -> {
			try {
				return sinhVienService.layTheoMa(ma);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}).toList();

		NhomSinhVienDto respones = new NhomSinhVienDto(nhom, sinhViens, nhom.getTinhTrang(), nhom.getDeTai());

		return respones;
	}

	@PostMapping("/roi-nhom")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> roiKhoiNhomDaDK(@RequestBody DangKyNhomRequest request) throws Exception {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		List<KeHoach> keHoachs = keHoachService.layTheoTenVaMaHocKyVaiTro(hocKy.getMaHocKy(), "Lịch đăng ký nhóm",
				request.getVaiTro());
		System.out.println("NhomController - dang ky nhom - " + request);
		if (keHoachs.size() > 0) {
			KeHoach keHoach = keHoachs.get(0);
			if (keHoach.getThoiGianBatDau().getTime() > System.currentTimeMillis()) {
				throw new Exception("Chưa đến thời gian để đăng ký nhóm");
			} else if (keHoach.getThoiGianKetThuc().getTime() < System.currentTimeMillis()) {
				throw new Exception("Thời gian đăng ký nhóm đã hết");
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
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(500).body(e.getMessage());
			}
		} else {
			throw new Exception("Chưa có kế hoạch đăng ký nhóm");
		}

	}

}
