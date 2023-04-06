package com.iuh.backendkltn32.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.service.HocKyService;

@RestController
@RequestMapping("/api/hoc-ky")
public class HocKyController {
	
	@Autowired
	private HocKyService hocKyService;

	@GetMapping("/lay-nam-hoc-ky")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public List<HocKy> layHocKy(){
		return hocKyService.layTatCaHocKy();
	}
	
	@GetMapping("/lay-hoc-ky-moi-nhat")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY')")
	public HocKy layHocKyCuoiCung(){
		return hocKyService.layHocKyCuoiCungTrongDS();
	}
}
