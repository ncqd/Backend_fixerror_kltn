package com.iuh.backendkltn32.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.dto.DangKyDeTaiRequest;
import com.iuh.backendkltn32.dto.DeTaiDto;
import com.iuh.backendkltn32.dto.LayDeTaiRquestDto;
import com.iuh.backendkltn32.entity.DeTai;
import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.jms.JmsListenerConsumer;
import com.iuh.backendkltn32.jms.JmsPublishProducer;
import com.iuh.backendkltn32.service.DeTaiService;
import com.iuh.backendkltn32.service.GiangVienService;
import com.iuh.backendkltn32.service.HocKyService;

@RestController
@RequestMapping("/api/de-tai")
public class DeTaiController {

	@Autowired
	private DeTaiService deTaiService;
	
	@Autowired
	private JmsListenerConsumer listenerConsumer;

	@Autowired
	private HocKyService hocKyService;

	@Autowired
	private GiangVienService giangVienService;
	
	@Autowired
	private JmsPublishProducer producer;

	@PostMapping("/them-de-tai/{maGiangVien}")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY')")
	public DeTai themDeTai(@RequestBody DeTai deTai, @PathVariable("maGiangVien") String maGiangVien) {

		try {
			GiangVien giangVien = giangVienService.layTheoMa(maGiangVien);
			HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
			DeTai deTaiCuoiTrongHK = deTaiService.getDeTaiCuoiCungTrongHocKy(hocKy.getMaHocKy(), hocKy.getSoHocKy());

			String maDT = "001";

			if (deTaiCuoiTrongHK == null) {
				maDT = "001";
			} else {
				Long soMaDT = Long.parseLong(deTaiCuoiTrongHK.getMaDeTai().substring(2)) + 1;
				System.out.println("chua ra so" + deTaiCuoiTrongHK.getMaDeTai().substring(2));
				if (soMaDT < 10) {
					maDT = "00" + soMaDT;
				} else if (soMaDT >= 10 && soMaDT < 100) {
					maDT = "0" + soMaDT;
				} else {
					maDT = "" + soMaDT;
				}
			}
			deTai.setMaDeTai("DT" + maDT);
			deTai.setGiangVien(giangVien);
			deTai.setHocKy(hocKy);
			deTai.setTrangThai(0);
			System.out.println("giang-vien-controller - them de tai - " + hocKy);

			DeTai ketQuaLuu = deTaiService.luu(deTai);

			return ketQuaLuu;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@DeleteMapping("/xoa-de-tai/{maDeTai}")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY')")
	public String xoaDeTai(@PathVariable String maDeTai) {

		try {
			String ketQuaXoaDeTai = deTaiService.xoa(maDeTai);
			return ketQuaXoaDeTai;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@PutMapping("/sua-de-tai")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY')")
	public DeTai suaDeTai(@RequestBody DeTai deTai) {

		try {
			HocKy hocKy = hocKyService.layTheoMa(deTai.getHocKy().getMaHocKy());
			deTai.setHocKy(hocKy);
			deTai.setTrangThai(0);
			DeTai ketQuaLuu = deTaiService.capNhat(deTai);

			return ketQuaLuu;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@PostMapping("/lay-ds-de-tai-theo-nam-hk")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY')")
	public List<DeTai> layDanhSachDeTaiTheoNamHocKyTrangThai(@RequestBody LayDeTaiRquestDto layDeTaiRquestDto) {
		try {
			List<DeTai> dsDeTai = new ArrayList<>();
			if (layDeTaiRquestDto.getTrangThai() == null) {
				dsDeTai = deTaiService.layDsDeTaiTheoNamHocKy(layDeTaiRquestDto.getMaHocKy(),
						layDeTaiRquestDto.getSoHocKy(), layDeTaiRquestDto.getMaGiangVien());
			} else {
				dsDeTai = deTaiService.layDsDeTaiTheoNamHocKyTheoTrangThai(layDeTaiRquestDto.getMaHocKy(),
						layDeTaiRquestDto.getSoHocKy(), layDeTaiRquestDto.getMaGiangVien(),
						layDeTaiRquestDto.getTrangThai());
			}

			return dsDeTai;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping("/xem-de-tai-da-duyet")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> xemDsDeTaiDaDuocDuyet(@RequestBody LayDeTaiRquestDto layDeTaiRquestDto) {
		try {
			List<DeTaiDto> dsDeTaiDtos = new ArrayList<>();
			if (layDeTaiRquestDto.getMaGiangVien() == null) {
				List<DeTai> dsDeTai = deTaiService.layDsDeTaiTheoNamHocKyDaDuyet(layDeTaiRquestDto.getMaHocKy(),
						layDeTaiRquestDto.getSoHocKy());

				for (DeTai deTai : dsDeTai) {
					Integer soNhomDaDKDeTai = deTaiService.laySoNhomDaDangKyDeTai(deTai.getMaDeTai());
					dsDeTaiDtos.add(new DeTaiDto(deTai,
							soNhomDaDKDeTai == deTai.getGioiHanSoNhomThucHien() ? true : false, deTai.getGiangVien()));
				}
			}else {
				List<DeTai> dsDeTai = deTaiService.layDsDeTaiTheoNamHocKyTheoTrangThai(layDeTaiRquestDto.getMaHocKy(),
						layDeTaiRquestDto.getSoHocKy(), layDeTaiRquestDto.getMaGiangVien(), layDeTaiRquestDto.getTrangThai());
				
				for (DeTai deTai : dsDeTai) {
					Integer soNhomDaDKDeTai = deTaiService.laySoNhomDaDangKyDeTai(deTai.getMaDeTai());
					dsDeTaiDtos.add(new DeTaiDto(deTai,
							soNhomDaDKDeTai == deTai.getGioiHanSoNhomThucHien() ? true : false, deTai.getGiangVien()));
				}
			}
			

			return ResponseEntity.ok(dsDeTaiDtos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.ok("Have Error");
		}
	}
	
	@PostMapping("/dang-ky-de-tai")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> dangKyDeTai(@RequestBody DangKyDeTaiRequest request) {

		try {
			producer.sendMessageOnDeTaiChanel(request);
			return listenerConsumer.listenerDeTaiChannel();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Have Error");
		}
	}
	
	@PostMapping("/import-excel")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> importExcel(@RequestBody DangKyDeTaiRequest request) {

		try {
			return ResponseEntity.ok("aaaa");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Have Error");
		}
	}
}
