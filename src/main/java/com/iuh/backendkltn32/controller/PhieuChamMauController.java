package com.iuh.backendkltn32.controller;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.iuh.backendkltn32.dto.DangKyNhomRequest;
import com.iuh.backendkltn32.dto.LayDeTaiRquestDto;
import com.iuh.backendkltn32.dto.LayKeHoachRequest;
import com.iuh.backendkltn32.dto.PhieuChamDiemDto;
import com.iuh.backendkltn32.dto.PhieuChamMauDto;
import com.iuh.backendkltn32.dto.PhieuChamMauDto2;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.PhieuChamMau;
import com.iuh.backendkltn32.entity.TieuChiChamDiem;
import com.iuh.backendkltn32.service.HocKyService;
import com.iuh.backendkltn32.service.PhieuChamMauService;
import com.iuh.backendkltn32.service.TieuChiChamDiemService;

@RestController
@RequestMapping("/api/phieu-mau")
public class PhieuChamMauController {

	@Autowired
	private PhieuChamMauService phieuChamMauService;

	@Autowired
	private TieuChiChamDiemService tieuChiChamDiemService;
	
	@Autowired
	private HocKyService hocKyService;

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
	public PhieuChamMau themPhieuChamMau(@RequestBody PhieuChamMauDto phieuChamMau) throws Exception {
		Double diemTong = (double) 0;
		for (String matc : phieuChamMau.getTieuChiChamDiems()) {
			try {
				diemTong += tieuChiChamDiemService.layTheoMa(matc).getDiemToiDa();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (diemTong != 10) {
			throw new Exception("Khong Duoc Qua 10 Diem");
		}
		return phieuChamMauService
				.luu(new PhieuChamMau(phieuChamMau.getTenPhieuCham(), phieuChamMau.getTieuChiChamDiems().toString(),
						phieuChamMau.getVaiTroDung(), phieuChamMau.getMaHocKy()));
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

	@PostMapping("/lay-het/{vaiTroNguoiDung}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public List<PhieuChamMauDto2> layHet(@PathVariable("vaiTroNguoiDung") String vaiTroNguoiDung) {
		try {
			HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
			List<PhieuChamMauDto2> phieuChamMauDto2s = new ArrayList<>();
			phieuChamMauService.layHet(vaiTroNguoiDung, hocKy.getMaHocKy()).stream().forEach(pc -> {
				List<String> maTieuChiChamDiems = Arrays.asList(
						pc.getTieuChiChamDiems().substring(1, pc.getTieuChiChamDiems().length() - 1).split(",\\s"));
				List<TieuChiChamDiem> tieuChiChamDiems = maTieuChiChamDiems.stream().map(tc -> {
					try {
						return tieuChiChamDiemService.layTheoMa(tc);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}).toList();
				phieuChamMauDto2s.add(new PhieuChamMauDto2(pc.getTenPhieuCham(), tieuChiChamDiems, pc.getVaiTroDung()));
			});

			return phieuChamMauDto2s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping("/lay/{vaiTroNguoiDung}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_GIANGVIEN')")
	public PhieuChamMauDto2 layPhieuMauMoi(@PathVariable("vaiTroNguoiDung") String vaiTroNguoiDung) {
		try {
			HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
			List<PhieuChamMauDto2> phieuChamMauDto2s =  new ArrayList<>();
			phieuChamMauService.layHet(vaiTroNguoiDung, hocKy.getMaHocKy()).stream().forEach(pc -> {
				List<String> maTieuChiChamDiems = Arrays.asList(pc.getTieuChiChamDiems().substring(1, pc.getTieuChiChamDiems().length()-1).split(",\\s"));
				List<TieuChiChamDiem> tieuChiChamDiems = maTieuChiChamDiems.stream().map(tc -> {
					try {
						return tieuChiChamDiemService.layTheoMa(tc);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}).toList();
				phieuChamMauDto2s.add(new PhieuChamMauDto2(pc.getTenPhieuCham(), tieuChiChamDiems, pc.getVaiTroDung()));
			});

			return phieuChamMauDto2s.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
