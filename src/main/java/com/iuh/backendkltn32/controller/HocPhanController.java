package com.iuh.backendkltn32.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.entity.HocPhanKhoaLuanTotNghiep;
import com.iuh.backendkltn32.entity.LopHocPhan;
import com.iuh.backendkltn32.service.HocPhanKhoaLuanTotNghiepService;
import com.iuh.backendkltn32.service.LopHocPhanService;

@RestController
@RequestMapping("/api/hoc-phan")
public class HocPhanController {

	@Autowired
	private HocPhanKhoaLuanTotNghiepService hpService;
	
	@Autowired
	private LopHocPhanService lhpService;

	@GetMapping("/lay-hoc-phan/{maHocPhan}")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN')")
	public HocPhanKhoaLuanTotNghiep layHocPhan(@PathVariable("maHocPhan") String maHocPhan) {

		
		try {
			HocPhanKhoaLuanTotNghiep hocPhanKhoaLuanTotNghiep = hpService.layTheoMa(maHocPhan);
			return hocPhanKhoaLuanTotNghiep;
		} catch (RuntimeException e) {
			
			e.printStackTrace();
			return null;
		}

	}
	
	@GetMapping("/lay-lop-hoc-phan/{maLopHocPhan}")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN')")
	public LopHocPhan layLopHocPhan(@PathVariable("maLopHocPhan") String maLopHocPhan) {

		
		try {
			LopHocPhan lopHocPhan = lhpService.layTheoMa(maLopHocPhan);
			return lopHocPhan;
		} catch (RuntimeException e) {
			e.printStackTrace();
			
			return null;
		}

	}

}
