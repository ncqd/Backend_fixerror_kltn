package com.iuh.backendkltn32.controller;

import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.HocPhanKhoaLuanTotNghiep;
import com.iuh.backendkltn32.service.HocKyService;
import com.iuh.backendkltn32.service.HocPhanKhoaLuanTotNghiepService;

@RestController
@RequestMapping("/api/hoc-ky")
@Transactional
public class HocKyController {
	
	@Autowired
	private HocKyService hocKyService;
	
	@Autowired
	private HocPhanKhoaLuanTotNghiepService hocPhanKhoaLuanTotNghiepService;

	@GetMapping("/lay-nam-hoc-ky")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public List<HocKy> layHocKy(){
		return hocKyService.layTatCaHocKy();
	}
	
	@GetMapping("/lay-hoc-ky-moi-nhat")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public HocKy layHocKyCuoiCung(){
		return hocKyService.layHocKyCuoiCungTrongDS();
	}
	
	@PostMapping("/them")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public HocKy taoHocKy(@RequestBody HocKy hocKy) throws Exception {
		HocKy hocKyCuoiCung = hocKyService.layHocKyCuoiCungTrongDS();
		String maHK = "";
		if (hocKyCuoiCung == null) {
			maHK = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2);
		}
		
		maHK = (Long.parseLong(hocKyCuoiCung.getMaHocKy().substring(0, 2)) + 1) + "" ;
		String soHocKy = hocKy.getThoiGianBatDau().getMonth()+1 >= 8 && hocKy.getThoiGianBatDau().getMonth()+1 < 12 ? "1" : "2";
		hocKy.setMaHocKy(maHK + soHocKy);
		hocKy.setSoHocKy(soHocKy);
		

		hocKy = hocKyService.luu(hocKy);

		return hocKy;
	}
}
