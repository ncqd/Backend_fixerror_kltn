package com.iuh.backendkltn32.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.dto.PhieuChamDiemDto;
import com.iuh.backendkltn32.dto.TieuChiChamDiemDto;
import com.iuh.backendkltn32.entity.DiemThanhPhan;
import com.iuh.backendkltn32.entity.PhieuCham;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.entity.TaiKhoan;
import com.iuh.backendkltn32.entity.TieuChiChamDiem;
import com.iuh.backendkltn32.service.DeTaiService;
import com.iuh.backendkltn32.service.DiemThanhPhanService;
import com.iuh.backendkltn32.service.GiangVienService;
import com.iuh.backendkltn32.service.PhieuChamService;
import com.iuh.backendkltn32.service.TieuChiChamDiemService;

@RestController
@RequestMapping("/api/phieu-cham-diem")
public class PhieuChamDiemController {

	@Autowired
	private PhieuChamService phieuChamService;

	@Autowired
	private TieuChiChamDiemService tieuChiChamDiemService;

	@Autowired
	private DeTaiService deTaiService;

	@Autowired
	private GiangVienService giangVienService;
	
//	@Autowired
	private DiemThanhPhanService diemThanhPhanService;

	@PostMapping("/them-phieu-cham")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public PhieuCham themPhieuCham(@RequestBody PhieuChamDiemDto phieuCham) throws Exception {

		try {
			PhieuCham phieuChamAdd = phieuChamService.luu(new PhieuCham(phieuCham.getTenPhieu(), null, null, null,
					deTaiService.layTheoMa(phieuCham.getMaDeTai()),
					giangVienService.layTheoMa(phieuCham.getMaGiangVien())));

			List<DiemThanhPhan> dsDiemThanhPhans = new ArrayList<>();
			for (TieuChiChamDiemDto tc : phieuCham.getDsTieuChiChamDiem()) {
				TieuChiChamDiem tieuChiChamDiem = null;
				if (tc.getMaChuanDauRa() == null) {
					tieuChiChamDiem = tieuChiChamDiemService
							.luu(new TieuChiChamDiem(tc.getTenChuanDauRa(), tc.getDiemToiDa(), null));
				}
				DiemThanhPhan diemThanhPhan = new DiemThanhPhan(phieuChamAdd, tieuChiChamDiem, null);
				diemThanhPhanService.luu(diemThanhPhan);
				dsDiemThanhPhans.add(diemThanhPhan);
			}
			phieuChamAdd.setDsDiemThanhPhan(dsDiemThanhPhans);
			phieuChamAdd = phieuChamService.capNhat(phieuChamAdd);
			return phieuChamAdd;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@PutMapping("/sua-phieu-cham")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public PhieuCham suaPhieuCham(@RequestBody PhieuChamDiemDto phieuCham) throws Exception {

		try {
			PhieuCham phieuChamUpdate = phieuChamService.capNhat(
					new PhieuCham(phieuCham.getMaPhieuCham(), phieuCham.getTenPhieu(), phieuCham.getKetQuaTong(), null,
							null, deTaiService.layTheoMa(phieuCham.getMaDeTai()),
							giangVienService.layTheoMa(phieuCham.getMaGiangVien())));

			List<DiemThanhPhan> dsDiemThanhPhans = new ArrayList<>();
			for (TieuChiChamDiemDto tc : phieuCham.getDsTieuChiChamDiem()) {
				TieuChiChamDiem tieuChiChamDiem = null;
				if (tc.getMaChuanDauRa() == null) {
					tieuChiChamDiem = tieuChiChamDiemService
							.luu(new TieuChiChamDiem(tc.getTenChuanDauRa(), tc.getDiemToiDa(), null));
				}
				dsDiemThanhPhans.add(new DiemThanhPhan(phieuChamUpdate, tieuChiChamDiem, null));
			}
			phieuChamUpdate.setDsDiemThanhPhan(dsDiemThanhPhans);
			phieuChamUpdate = phieuChamService.capNhat(phieuChamUpdate);
			return phieuChamUpdate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@GetMapping("/lay-phieu-cham/{maPhieu}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public PhieuCham layPhieuCham(@PathVariable("maPhieu") String maPhieu) throws Exception {
		try {
			PhieuCham phieuChamUpdate = phieuChamService.layTheoMa(maPhieu);
			return phieuChamUpdate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
