package com.iuh.backendkltn32.controller;

import java.util.List;

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

import com.iuh.backendkltn32.dto.PhieuChamDiemDto;
import com.iuh.backendkltn32.dto.PhieuChamMauDto;
import com.iuh.backendkltn32.entity.PhieuChamMau;
import com.iuh.backendkltn32.entity.TieuChiChamDiem;
import com.iuh.backendkltn32.service.PhieuChamMauService;
import com.iuh.backendkltn32.service.TieuChiChamDiemService;

@RestController
@RequestMapping("/api/phieu-mau")
public class PhieuChamMauController {
	
	@Autowired
	private PhieuChamMauService phieuChamMauService;
	
	@Autowired
	private TieuChiChamDiemService tieuChiChamDiemService;
	
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
	public PhieuChamMau themPhieuChamMau(@RequestBody PhieuChamMauDto phieuChamMau) {

		try {
			List<TieuChiChamDiem> tieuChiChamDiems = phieuChamMau.getTieuChiChamDiems().stream().map(tc -> {
				try {
					return tieuChiChamDiemService.layTheoMa(tc);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}).toList();
			return phieuChamMauService.luu(new PhieuChamMau(phieuChamMau.getTenPhieuCham(), tieuChiChamDiems, phieuChamMau.getVaiTroDung()));
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
	
	@GetMapping("/lay-het/{vaiTroNguoiDung}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public List<PhieuChamMau> layHet(@PathVariable("vaiTroNguoiDung") String vaiTroNguoiDung) {
		try {
			return phieuChamMauService.layHet(vaiTroNguoiDung);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
