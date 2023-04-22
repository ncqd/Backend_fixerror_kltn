package com.iuh.backendkltn32.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.entity.TieuChiChamDiem;
import com.iuh.backendkltn32.service.TieuChiChamDiemService;

@RestController
@RequestMapping("/api/tieu-chi-cham-diem")
public class TieuChiChamDiemController {
	
	@Autowired
	private TieuChiChamDiemService tieuChiChamDiemService;
	
	@PostMapping("/them-tieu-chi-cham-diem")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public TieuChiChamDiem themTieuChiChamDiem(@RequestBody TieuChiChamDiem tieuChiChamDiem) {

		try {
				
			return tieuChiChamDiemService.luu(tieuChiChamDiem);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PutMapping("/cap-nhat-tieu-chi-cham-diem")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public TieuChiChamDiem capNhatTieuChiChamDiem(@RequestBody TieuChiChamDiem tieuChiChamDiem) {

		try {
				
			return tieuChiChamDiemService.capNhat(tieuChiChamDiem);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@DeleteMapping("/xoa-tieu-chi-cham-diem/{ma}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public String xoaTieuChiChamDiem(@PathVariable("ma") String ma) {

		try {
				
			return tieuChiChamDiemService.xoa(ma);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping("/lay-ds-tieu-chi-cham-diem/{maPhieuCham}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public String layTieuChiChamDiemTheoMaPhieu(@PathVariable("maPhieuCham") String ma) {
		try {
			return tieuChiChamDiemService.xoa(ma);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
