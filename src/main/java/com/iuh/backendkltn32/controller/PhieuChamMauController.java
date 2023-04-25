package com.iuh.backendkltn32.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.entity.PhieuChamMau;
import com.iuh.backendkltn32.service.PhieuChamMauService;

@RestController
@RequestMapping("/api/phieu-mau")
public class PhieuChamMauController {
	
	private PhieuChamMauService phieuChamMauService;
	
	@GetMapping("/lay/{maPhieu}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public PhieuChamMau layPhieuMau(@PathVariable("maPhieu") String ma) {
		try {
			return phieuChamMauService.layTheoMa(ma);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping("/them")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public PhieuChamMau themPhieuChamMau(@RequestBody PhieuChamMau phieuChamMau) {

		try {
			return phieuChamMauService.luu(phieuChamMau);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PutMapping("/cap-nhat")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public PhieuChamMau capNhatPhieuChamMau(@RequestBody PhieuChamMau phieuChamMau) {

		try {
				
			return phieuChamMauService.capNhat(phieuChamMau);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@DeleteMapping("/xoa/{ma}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public String xoaPhieuChamMau(@PathVariable("ma") String ma) {

		try {
				
			return phieuChamMauService.xoa(ma);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
