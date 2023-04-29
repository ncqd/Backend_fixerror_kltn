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

import com.iuh.backendkltn32.dto.DiemThanhPhanDto;
import com.iuh.backendkltn32.dto.PhieuChamDiemDto;
import com.iuh.backendkltn32.dto.PhieuChamMauDto2;
import com.iuh.backendkltn32.dto.TieuChiChamDiemDto;
import com.iuh.backendkltn32.entity.DiemThanhPhan;
import com.iuh.backendkltn32.entity.KetQua;
import com.iuh.backendkltn32.entity.PhieuCham;
import com.iuh.backendkltn32.entity.TieuChiChamDiem;
import com.iuh.backendkltn32.service.DeTaiService;
import com.iuh.backendkltn32.service.DiemThanhPhanService;
import com.iuh.backendkltn32.service.GiangVienService;
import com.iuh.backendkltn32.service.KetQuaService;
import com.iuh.backendkltn32.service.PhieuChamService;
import com.iuh.backendkltn32.service.SinhVienService;
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
	
	@Autowired
	private DiemThanhPhanService diemThanhPhanService;
	
	@Autowired
	private SinhVienService sinhVienService;
	
	@Autowired
	private KetQuaService ketQuaService;

	@PostMapping("/cham-diem")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public PhieuCham themPhieuCham(@RequestBody PhieuChamDiemDto phieuCham) throws Exception {

		try {
			PhieuCham phieuChamAdd = phieuChamService.luu(new PhieuCham(Math.random()+"" ,phieuCham.getTenPhieu(), null, null, null,
					deTaiService.layTheoMa(phieuCham.getMaDeTai()),
					giangVienService.layTheoMa(phieuCham.getMaGiangVien())));

			List<DiemThanhPhan> dsDiemThanhPhans = new ArrayList<>();
			for (TieuChiChamDiemDto tc : phieuCham.getDsTieuChiChamDiem()) {
				TieuChiChamDiem tieuChiChamDiem = tieuChiChamDiemService.layTheoMa(tc.getMaChuanDauRa()+"");
				
				DiemThanhPhan diemThanhPhan = new DiemThanhPhan(phieuChamAdd, tieuChiChamDiem, tc.getDiemThanhPhan()+"");
				diemThanhPhanService.luu(diemThanhPhan);
				dsDiemThanhPhans.add(diemThanhPhan);
			}
			phieuChamAdd.setDsDiemThanhPhan(dsDiemThanhPhans);
			phieuChamAdd = phieuChamService.capNhat(phieuChamAdd);
			List<KetQua> ketQuas = new ArrayList<>();
			String maPhieCHamAdd = phieuChamAdd.getMaPhieu();
			dsDiemThanhPhans.stream().forEach(dtp -> {
				KetQua ketQua = new KetQua();
				try {
					ketQua.setPhieuCham(phieuChamService.layTheoMa(maPhieCHamAdd));
					ketQua.setSinhVien(sinhVienService.layTheoMa(phieuCham.getMaSinhVien()));
					ketQuas.add(ketQuaService.luu(ketQua));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			phieuChamAdd.setDsKetQua(ketQuas);
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
							.luu(new TieuChiChamDiem(tc.getTenChuanDauRa(), tc.getDiemToiDa()));
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
	
	@PutMapping("/cham-diem-a")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public PhieuCham chamDiem(@RequestBody List<DiemThanhPhanDto> diemThanhPhanDtos) throws Exception {
		try {
			PhieuCham phieuChamUpdate = phieuChamService.layTheoMa(diemThanhPhanDtos.get(0).getMaPhieuCham());

			List<DiemThanhPhan> dsDiemThanhPhans = diemThanhPhanService.layDsDiemThanhPhan(phieuChamUpdate.getMaPhieu());
			if (diemThanhPhanDtos.size() > dsDiemThanhPhans.size()) {
				throw new Exception("Danh Sách chấm điểm không hợp lệ");
			}
			List<DiemThanhPhan> dsDiemThanhPhanUpdate = new ArrayList<>();
			dsDiemThanhPhans.stream().forEach(dtp -> {
				diemThanhPhanDtos.stream().forEach(dtpa -> {
					if (dtp.getTieuChiChamDiem().getMaChuanDauRa().equals(dtpa.getMaTieuChiCham())) {
						dtp.setDiemThanhPhan(dtpa.getDiem());
						dsDiemThanhPhanUpdate.add(dtp);
					}
				});
			});
			diemThanhPhanService.capNhatAll(dsDiemThanhPhanUpdate);
			phieuChamUpdate.setDsDiemThanhPhan(dsDiemThanhPhans);
			phieuChamUpdate = phieuChamService.capNhat(phieuChamUpdate);
			return phieuChamUpdate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PutMapping("/sua-diem")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public PhieuCham suaDiem(@RequestBody List<DiemThanhPhanDto> diemThanhPhanDtos) throws Exception {
		try {
			PhieuCham phieuChamUpdate = phieuChamService.layTheoMa(diemThanhPhanDtos.get(0).getMaPhieuCham());

			List<DiemThanhPhan> dsDiemThanhPhans = diemThanhPhanService.layDsDiemThanhPhan(phieuChamUpdate.getMaPhieu());
			if (diemThanhPhanDtos.size() > dsDiemThanhPhans.size()) {
				throw new Exception("Danh Sách chấm điểm không hợp lệ");
			}
			List<DiemThanhPhan> dsDiemThanhPhanUpdate = new ArrayList<>();
			dsDiemThanhPhans.stream().forEach(dtp -> {
				diemThanhPhanDtos.stream().forEach(dtpa -> {
					if (dtp.getTieuChiChamDiem().getMaChuanDauRa().equals(dtpa.getMaTieuChiCham())) {
						dtp.setDiemThanhPhan(dtpa.getDiem());
						dsDiemThanhPhanUpdate.add(dtp);
					}
				});
			});
			diemThanhPhanService.capNhatAll(dsDiemThanhPhanUpdate);
			phieuChamUpdate.setDsDiemThanhPhan(dsDiemThanhPhans);
			phieuChamUpdate = phieuChamService.capNhat(phieuChamUpdate);
			return phieuChamUpdate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
