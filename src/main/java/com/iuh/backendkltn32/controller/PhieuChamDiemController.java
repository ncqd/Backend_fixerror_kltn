package com.iuh.backendkltn32.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.iuh.backendkltn32.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.entity.DiemThanhPhan;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.KetQua;
import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.PhieuCham;
import com.iuh.backendkltn32.entity.PhieuChamMau;
import com.iuh.backendkltn32.entity.TieuChiChamDiem;
import com.iuh.backendkltn32.service.DeTaiService;
import com.iuh.backendkltn32.service.DiemThanhPhanService;
import com.iuh.backendkltn32.service.GiangVienService;
import com.iuh.backendkltn32.service.HocKyService;
import com.iuh.backendkltn32.service.KetQuaService;
import com.iuh.backendkltn32.service.PhieuChamMauService;
import com.iuh.backendkltn32.service.PhieuChamService;
import com.iuh.backendkltn32.service.SinhVienService;
import com.iuh.backendkltn32.service.TieuChiChamDiemService;

@RestController
@RequestMapping("/api/phieu-cham")
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

	@Autowired
	private PhieuChamMauService phieuChamMauService;

	@Autowired
	private HocKyService hocKyService;

	@PostMapping("/cham-diem")
	@PreAuthorize("hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_GIANGVIEN')")
	public List<PhieuCham> themPhieuCham(@RequestBody PhieuChamDiemDto phieuCham) throws Exception {
		List<PhieuCham> phieuChams = new ArrayList<>();
		try {
			PhieuCham phieuChamAddSV1;
			PhieuCham phieuChamAddSV2;
			phieuChamAddSV1 = phieuChamService.luu(new PhieuCham(Math.random() + "", phieuCham.getTenPhieu(), null,
					null, null, deTaiService.layTheoMa(phieuCham.getMaDeTai()),
					giangVienService.layTheoMa(phieuCham.getMaGiangVien())));

			HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
			List<TieuChiChamDiem> tieuChiChamDiems = null;
			for (PhieuChamMau pc : phieuChamMauService.layHet(phieuCham.getTenPhieu(), hocKy.getMaHocKy())) {
				List<String> maTieuChiChamDiems = Arrays.asList(
						pc.getTieuChiChamDiems().substring(1, pc.getTieuChiChamDiems().length() - 1).split(",\\s"));
				tieuChiChamDiems = maTieuChiChamDiems.stream().map(tc -> {
					try {
						return tieuChiChamDiemService.layTheoMa(tc);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}).toList();
			}

			List<DiemThanhPhan> dsDiemThanhPhans = new ArrayList<>();
			Double diemTong = (double) 0;

			for (int i = 0; i < tieuChiChamDiems.size(); i++) {
				TieuChiChamDiem tieuChiChamDiem = tieuChiChamDiemService
						.layTheoMa(tieuChiChamDiems.get(i).getMaChuanDauRa() + "");

				DiemThanhPhan diemThanhPhan = new DiemThanhPhan(phieuChamAddSV1, tieuChiChamDiem,
						phieuCham.getBangDiem().get(i).getDiemSV1() == null ? "0"
								: "" + phieuCham.getBangDiem().get(i).getDiemSV1(),
						phieuCham.getBangDiem().get(i).getYkien());
				diemThanhPhanService.luu(diemThanhPhan);
				dsDiemThanhPhans.add(diemThanhPhan);
				diemTong += phieuCham.getBangDiem().get(i).getDiemSV1();
			}
			if (diemTong > 10) {
				throw new Exception("Cham Diem Sai");
			}
			phieuChamAddSV1.setDsDiemThanhPhan(dsDiemThanhPhans);
			phieuChamAddSV1 = phieuChamService.capNhat(phieuChamAddSV1);

			List<KetQua> ketQuas = new ArrayList<>();

			String maPhieCHamAdd = phieuChamAddSV1.getMaPhieu();
			KetQua ketQua = new KetQua();
			try {
				ketQua.setPhieuCham(phieuChamService.layTheoMa(maPhieCHamAdd));
				ketQua.setSinhVien(sinhVienService.layTheoMa(phieuCham.getSinhVien().get(0).getMaSinhVien()));
				ketQuas.add(ketQuaService.luu(ketQua));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			phieuChamAddSV1.setDsKetQua(ketQuas);
			phieuChamAddSV1.setDiemPhieuCham(diemTong);
			phieuChamAddSV1 = phieuChamService.capNhat(phieuChamAddSV1);

			phieuChams.add(phieuChamAddSV1);
			if (phieuCham.getSinhVien().size() >= 2) {

				phieuChamAddSV2 = phieuChamService.luu(new PhieuCham(Math.random() + "", phieuCham.getTenPhieu(), null,
						null, null, deTaiService.layTheoMa(phieuCham.getMaDeTai()),
						giangVienService.layTheoMa(phieuCham.getMaGiangVien())));

				dsDiemThanhPhans = new ArrayList<>();
				diemTong = (double) 0;

				for (int i = 0; i < tieuChiChamDiems.size(); i++) {
					TieuChiChamDiem tieuChiChamDiem = tieuChiChamDiemService
							.layTheoMa(tieuChiChamDiems.get(i).getMaChuanDauRa() + "");

					DiemThanhPhan diemThanhPhan = new DiemThanhPhan(phieuChamAddSV2, tieuChiChamDiem,
							phieuCham.getBangDiem().get(i).getDiemSV2() == null ? "0"
									: "" + phieuCham.getBangDiem().get(i).getDiemSV2(),
							phieuCham.getBangDiem().get(i).getYkien());
					diemThanhPhanService.luu(diemThanhPhan);
					dsDiemThanhPhans.add(diemThanhPhan);
					diemTong += phieuCham.getBangDiem().get(i).getDiemSV2();
				}
				if (diemTong > 10) {
					throw new Exception("Cham Diem Sai");
				}
				phieuChamAddSV2.setDsDiemThanhPhan(dsDiemThanhPhans);
				phieuChamAddSV2 = phieuChamService.capNhat(phieuChamAddSV2);

				ketQuas = new ArrayList<>();

				maPhieCHamAdd = phieuChamAddSV2.getMaPhieu();
				ketQua = new KetQua();
				try {
					ketQua.setPhieuCham(phieuChamService.layTheoMa(maPhieCHamAdd));
					ketQua.setSinhVien(sinhVienService.layTheoMa(phieuCham.getSinhVien().get(1).getMaSinhVien()));
					ketQuas.add(ketQuaService.luu(ketQua));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				phieuChamAddSV2.setDsKetQua(ketQuas);
				phieuChamAddSV2.setDiemPhieuCham(diemTong);
				phieuChamAddSV2 = phieuChamService.capNhat(phieuChamAddSV2);
				phieuChams.add(phieuChamAddSV2);
			}
			return phieuChams;
		} catch (

		Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@PutMapping("/sua-phieu-cham")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public PhieuCham suaPhieuCham(@RequestBody PhieuChamDiemDto phieuCham) throws Exception {

		try {
			PhieuCham phieuChamUpdate = phieuChamService
					.capNhat(new PhieuCham(phieuCham.getMaPhieuCham(), phieuCham.getTenPhieu(),
							phieuCham.getKetQuaTong(), null, null, deTaiService.layTheoMa(phieuCham.getMaDeTai()),
							giangVienService.layTheoMa(phieuCham.getMaGiangVien())));

			List<DiemThanhPhan> dsDiemThanhPhans = new ArrayList<>();
			for (TieuChiChamDiemDto tc : phieuCham.getDsTieuChiChamDiem()) {
				TieuChiChamDiem tieuChiChamDiem = null;
				if (tc.getMaChuanDauRa() == null) {
					tieuChiChamDiem = tieuChiChamDiemService
							.luu(new TieuChiChamDiem(tc.getTenChuanDauRa(), tc.getDiemToiDa()));
				}
				dsDiemThanhPhans.add(new DiemThanhPhan(phieuChamUpdate, tieuChiChamDiem, null,
						phieuCham.getBangDiem().get(0).getYkien()));
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

			List<DiemThanhPhan> dsDiemThanhPhans = diemThanhPhanService
					.layDsDiemThanhPhan(phieuChamUpdate.getMaPhieu());
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

			List<DiemThanhPhan> dsDiemThanhPhans = diemThanhPhanService
					.layDsDiemThanhPhan(phieuChamUpdate.getMaPhieu());
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

	@GetMapping("/lay/{maGiangVien}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_GIANGVIEN')")
	public List<DiemSinhVienDto> layPhieuChamTheoGiangVien(@PathVariable("maGiangVien") String maGiangVien)
			throws Exception {
		List<DiemSinhVienDto> rs = new ArrayList<>();
		try {
			phieuChamService.layDsPhieuCham(maGiangVien, hocKyService.layHocKyCuoiCungTrongDS().getMaHocKy())
					.forEach(phieuCham -> {
						DiemSinhVienDto diemSVDTO = new DiemSinhVienDto();
						phieuCham.getDsKetQua().forEach(sv -> {
							diemSVDTO.setMaSV(sv.getSinhVien().getMaSinhVien());
							diemSVDTO.setTenSV(sv.getSinhVien().getTenSinhVien());
							diemSVDTO.setMaNhom(sv.getSinhVien().getNhom().getMaNhom());
							diemSVDTO.setTenNhom(sv.getSinhVien().getNhom().getTenNhom());
							diemSVDTO.setDiem(sv.getDiemTongKet());
						});
						diemSVDTO.setGvhd(phieuCham.getDeTai().getGiangVien().getTenGiangVien());
						diemSVDTO.setMaDeTai(phieuCham.getDeTai().getMaDeTai());
						diemSVDTO.setTenDeTai(phieuCham.getDeTai().getTenDeTai());
						diemSVDTO.setDiemThanhPhans(phieuCham.getDsDiemThanhPhan());
						rs.add(diemSVDTO);
					});
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping("/lay-cu-the")
	@PreAuthorize("hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_GIANGVIEN')")
	public List<DiemSinhVienDto> layPhieuChamCuThe(@RequestBody NhomVaiTroRequest request) throws Exception {
		List<DiemSinhVienDto> rs = new ArrayList<>();
		HocKy hocKy = null;
		if (request.getMaHocKy() == null) {
			hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		} else {
			hocKy = hocKyService.layTheoMa(request.getMaHocKy());
		}
		String viTriCham = request.getDotCham() != null && !request.getDotCham().isEmpty() 
				&& !request.getDotCham().isBlank() && !request.getDotCham().equals("") ?  request.getDotCham() : "All";
		if (viTriCham.equals("All")) {
			System.out.println("ALL");
			phieuChamService.layDsPhieuCham(request.getMaNguoiDung(), hocKy.getMaHocKy()).forEach(phieuCham -> {
				DiemSinhVienDto diemSVDTO = new DiemSinhVienDto();
				phieuCham.getDsKetQua().forEach(sv -> {
					diemSVDTO.setMaSV(sv.getSinhVien().getMaSinhVien());
					diemSVDTO.setTenSV(sv.getSinhVien().getTenSinhVien());
					diemSVDTO.setMaNhom(sv.getSinhVien().getNhom().getMaNhom());
					diemSVDTO.setTenNhom(sv.getSinhVien().getNhom().getTenNhom());
					diemSVDTO.setDiem(sv.getDiemTongKet());
				});
				diemSVDTO.setGvhd(phieuCham.getDeTai().getGiangVien().getTenGiangVien());
				diemSVDTO.setMaDeTai(phieuCham.getDeTai().getMaDeTai());
				diemSVDTO.setTenDeTai(phieuCham.getDeTai().getTenDeTai());
				diemSVDTO.setDiemThanhPhans(phieuCham.getDsDiemThanhPhan());
				rs.add(diemSVDTO);
			});
			return rs;
		} else if (viTriCham.equals("HD")) {
			System.out.println("HUOng Dan");
			phieuChamService
					.layDsPhieuChamVaiTro(request.getMaNguoiDung(), hocKy.getMaHocKy(), request.getDotCham())
					.forEach(phieuCham -> {
						DiemSinhVienDto diemSVDTO = new DiemSinhVienDto();
						phieuCham.getDsKetQua().forEach(sv -> {
							diemSVDTO.setMaSV(sv.getSinhVien().getMaSinhVien());
							diemSVDTO.setTenSV(sv.getSinhVien().getTenSinhVien());
							diemSVDTO.setMaNhom(sv.getSinhVien().getNhom().getMaNhom());
							diemSVDTO.setTenNhom(sv.getSinhVien().getNhom().getTenNhom());
							diemSVDTO.setDiem(sv.getDiemTongKet());
						});
						diemSVDTO.setGvhd(phieuCham.getDeTai().getGiangVien().getTenGiangVien());
						diemSVDTO.setMaDeTai(phieuCham.getDeTai().getMaDeTai());
						diemSVDTO.setTenDeTai(phieuCham.getDeTai().getTenDeTai());
						diemSVDTO.setDiemThanhPhans(phieuCham.getDsDiemThanhPhan());
						rs.add(diemSVDTO);
					});
		} else if (viTriCham.equals("PB")) {
			System.out.println("PB");
			phieuChamService
					.layDsPhieuChamPosterVaiTro(request.getMaNguoiDung(), hocKy.getMaHocKy(), request.getDotCham())
					.forEach(phieuCham -> {
						DiemSinhVienDto diemSVDTO = new DiemSinhVienDto();
						phieuCham.getDsKetQua().forEach(sv -> {
							diemSVDTO.setMaSV(sv.getSinhVien().getMaSinhVien());
							diemSVDTO.setTenSV(sv.getSinhVien().getTenSinhVien());
							diemSVDTO.setMaNhom(sv.getSinhVien().getNhom().getMaNhom());
							diemSVDTO.setTenNhom(sv.getSinhVien().getNhom().getTenNhom());
							diemSVDTO.setDiem(sv.getDiemTongKet());
						});
						diemSVDTO.setGvhd(phieuCham.getDeTai().getGiangVien().getTenGiangVien());
						diemSVDTO.setMaDeTai(phieuCham.getDeTai().getMaDeTai());
						diemSVDTO.setTenDeTai(phieuCham.getDeTai().getTenDeTai());
						diemSVDTO.setDiemThanhPhans(phieuCham.getDsDiemThanhPhan());
						rs.add(diemSVDTO);
					});
		} else {
			System.out.println("Hoi Dong ");
			for (String vaiTro : request.getVaitro()) {
				String viTriPhanCong = "";
				switch (vaiTro) {
				case "CT":
					viTriPhanCong = "chu tich";
					break;
				case "TK":
					viTriPhanCong = "thu ky";
					break;
				case "TV3":
					viTriPhanCong = "thanh vien 3";
					break;
				}
				if (request.getPpcham().size() == 1) {
					if (request.getPpcham().get(0).equals("chamPoster")) {
						phieuChamService
								.layDsPhieuChamPosterVaiTro(request.getMaNguoiDung(), hocKy.getMaHocKy(), viTriPhanCong)
								.forEach(phieuCham -> {
									DiemSinhVienDto diemSVDTO = new DiemSinhVienDto();
									phieuCham.getDsKetQua().forEach(sv -> {
										diemSVDTO.setMaSV(sv.getSinhVien().getMaSinhVien());
										diemSVDTO.setTenSV(sv.getSinhVien().getTenSinhVien());
										diemSVDTO.setMaNhom(sv.getSinhVien().getNhom().getMaNhom());
										diemSVDTO.setTenNhom(sv.getSinhVien().getNhom().getTenNhom());
										diemSVDTO.setDiem(sv.getDiemTongKet());
									});
									diemSVDTO.setGvhd(phieuCham.getDeTai().getGiangVien().getTenGiangVien());
									diemSVDTO.setMaDeTai(phieuCham.getDeTai().getMaDeTai());
									diemSVDTO.setTenDeTai(phieuCham.getDeTai().getTenDeTai());
									diemSVDTO.setDiemThanhPhans(phieuCham.getDsDiemThanhPhan());
									rs.add(diemSVDTO);
								});
					} else {
						phieuChamService
								.layDsPhieuChamHoiDongVaiTro(request.getMaNguoiDung(), hocKy.getMaHocKy(), viTriPhanCong)
								.forEach(phieuCham -> {
									DiemSinhVienDto diemSVDTO = new DiemSinhVienDto();
									phieuCham.getDsKetQua().forEach(sv -> {
										diemSVDTO.setMaSV(sv.getSinhVien().getMaSinhVien());
										diemSVDTO.setTenSV(sv.getSinhVien().getTenSinhVien());
										diemSVDTO.setMaNhom(sv.getSinhVien().getNhom().getMaNhom());
										diemSVDTO.setTenNhom(sv.getSinhVien().getNhom().getTenNhom());
										diemSVDTO.setDiem(sv.getDiemTongKet());
									});
									diemSVDTO.setGvhd(phieuCham.getDeTai().getGiangVien().getTenGiangVien());
									diemSVDTO.setMaDeTai(phieuCham.getDeTai().getMaDeTai());
									diemSVDTO.setTenDeTai(phieuCham.getDeTai().getTenDeTai());
									diemSVDTO.setDiemThanhPhans(phieuCham.getDsDiemThanhPhan());
									rs.add(diemSVDTO);
								});
					}
				} else {
					phieuChamService
							.layDsPhieuChamPosterVaiTro(request.getMaNguoiDung(), hocKy.getMaHocKy(), viTriPhanCong)
							.forEach(phieuCham -> {
								DiemSinhVienDto diemSVDTO = new DiemSinhVienDto();
								phieuCham.getDsKetQua().forEach(sv -> {
									diemSVDTO.setMaSV(sv.getSinhVien().getMaSinhVien());
									diemSVDTO.setTenSV(sv.getSinhVien().getTenSinhVien());
									diemSVDTO.setMaNhom(sv.getSinhVien().getNhom().getMaNhom());
									diemSVDTO.setTenNhom(sv.getSinhVien().getNhom().getTenNhom());
									diemSVDTO.setDiem(sv.getDiemTongKet());
								});
								diemSVDTO.setGvhd(phieuCham.getDeTai().getGiangVien().getTenGiangVien());
								diemSVDTO.setMaDeTai(phieuCham.getDeTai().getMaDeTai());
								diemSVDTO.setTenDeTai(phieuCham.getDeTai().getTenDeTai());
								diemSVDTO.setDiemThanhPhans(phieuCham.getDsDiemThanhPhan());
								rs.add(diemSVDTO);
							});
					phieuChamService
							.layDsPhieuChamHoiDongVaiTro(request.getMaNguoiDung(), hocKy.getMaHocKy(), viTriPhanCong)
							.forEach(phieuCham -> {
								DiemSinhVienDto diemSVDTO = new DiemSinhVienDto();
								phieuCham.getDsKetQua().forEach(sv -> {
									diemSVDTO.setMaSV(sv.getSinhVien().getMaSinhVien());
									diemSVDTO.setTenSV(sv.getSinhVien().getTenSinhVien());
									diemSVDTO.setMaNhom(sv.getSinhVien().getNhom().getMaNhom());
									diemSVDTO.setTenNhom(sv.getSinhVien().getNhom().getTenNhom());
									diemSVDTO.setDiem(sv.getDiemTongKet());
								});
								diemSVDTO.setGvhd(phieuCham.getDeTai().getGiangVien().getTenGiangVien());
								diemSVDTO.setMaDeTai(phieuCham.getDeTai().getMaDeTai());
								diemSVDTO.setTenDeTai(phieuCham.getDeTai().getTenDeTai());
								diemSVDTO.setDiemThanhPhans(phieuCham.getDsDiemThanhPhan());
								rs.add(diemSVDTO);
							});
				}
			}
		}
		return rs;

	}

}
