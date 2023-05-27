package com.iuh.backendkltn32.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.entity.HocPhanTienQuyet;
import com.iuh.backendkltn32.service.HocPhanTienQuyetService;

@RestController
@RequestMapping("/api/hoc-phan-tien-quyet")
public class HocPhanTienQuyetController {

	@Autowired
	private HocPhanTienQuyetService hocPhanTienQuyetService;
	
	@GetMapping("/lay-het")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY')")
	public List<HocPhanTienQuyet> getAll() {
		return hocPhanTienQuyetService.getAll();
	}
	
	
}
