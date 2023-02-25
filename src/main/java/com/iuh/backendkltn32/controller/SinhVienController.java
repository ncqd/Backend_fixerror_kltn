package com.iuh.backendkltn32.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.dto.LoginRequest;
import com.iuh.backendkltn32.dto.NhomSinhVienDto;
import com.iuh.backendkltn32.entity.DeTai;
import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.service.DeTaiService;
import com.iuh.backendkltn32.service.NhomService;
import com.iuh.backendkltn32.service.SinhVienService;

@RestController
@RequestMapping("/api/sinh-vien")
public class SinhVienController {

	@Autowired
	private SinhVienService sinhVienService;
	
	@Autowired
	private DeTaiService deTaiService;
	
	@Autowired
	private NhomService nhomService;

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
	
	@GetMapping("/xem-de-tai-da-duyet")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public List<DeTai> xemDsDeTaiDaDuocDuyet(){
		try {
			List<DeTai> dsDeTai =  deTaiService.layDsDeTaiTheoNamHocKyDaDuyet("HK2 (2022-2023)");
			
			return dsDeTai;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@GetMapping("/thong-tin-tong-sinh-vien/{namHocKy}")
	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> dsSinhVienTrongHocKy(@PathVariable("namHocKy") String namHocKy) {
		
		Integer hocKy = Integer.parseInt(namHocKy.substring(2, 3));
		String namHoc = namHocKy.substring(10, 14);
		
		System.out.println("SinhVienControllẻ - dsSinhVienTrongHocKy - " + namHoc);
		
		List<Nhom> nhoms = nhomService.layTatCaNhom(hocKy, namHoc);
		
		if (nhoms.isEmpty()) {
			return ResponseEntity.ok(new ArrayList<>());
		}
		
		List<NhomSinhVienDto> nhomSinhVien = new ArrayList<>();
		
		for (Nhom nhom : nhoms) {
			List<String> sinhViens = sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom());
			NhomSinhVienDto nhomSinhVienTemp = new NhomSinhVienDto(nhom.getMaNhom(),sinhViens, nhom.getTinhTrang());
			nhomSinhVien.add(nhomSinhVienTemp);
		}
		
		return ResponseEntity.ok(nhomSinhVien);
				
	}
	
//	@GetMapping("/thong-tin-tong-sinh-vien/{namHocKy}")
//	@PreAuthorize("hasAuthority('ROLE_SINHVIEN')")
//	public ResponseEntity<?> dsSinhVienTrongHocKy(@PathVariable("namHocKy") String namHocKy) {
//		
//		Integer hocKy = Integer.parseInt(namHocKy.substring(2, 3));
//		String namHoc = namHocKy.substring(10, 14);
//		
//		System.out.println("SinhVienControllẻ - dsSinhVienTrongHocKy - " + namHoc);
//		
//		List<Nhom> nhoms = nhomService.layTatCaNhom(hocKy, namHoc);
//		
//		if (nhoms.isEmpty()) {
//			return ResponseEntity.ok(null);
//		}
//		
//		List<NhomSinhVienDto> nhomSinhVien = new ArrayList<>();
//		
//		for (Nhom nhom : nhoms) {
//			List<String> sinhViens = sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom());
//			NhomSinhVienDto nhomSinhVienTemp = new NhomSinhVienDto(nhom.getMaNhom(),sinhViens, nhom.getTinhTrang());
//			nhomSinhVien.add(nhomSinhVienTemp);
//		}
//		
//		return ResponseEntity.ok(nhomSinhVien);
//				
//	}

}
