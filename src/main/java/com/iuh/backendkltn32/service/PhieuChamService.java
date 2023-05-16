package com.iuh.backendkltn32.service;

import java.util.List;

import com.iuh.backendkltn32.entity.PhieuCham;

public interface PhieuChamService extends AbstractService<PhieuCham> {

	List<PhieuCham> layDsPhieuCham(String maGiangVien, String maHocky);

	List<PhieuCham> layDsPhieuChamVaiTro(String maGiangVien, String maHocky, String tenPhieu);

	List<PhieuCham> layDsPhieuChamPosterVaiTro(String maGiangVien, String maHocky, String viTriPhanCong);

	List<PhieuCham> layDsPhieuChamHoiDongVaiTro(String maGiangVien, String maHocky, String viTriPhanCong);

	List<PhieuCham> layPhieuTheoMaSinhVienTenVaiTro(String maSinhVien, String tenPhieu);

	List<String> layMaPhieuPhieuTheoMaSinhVienTenVaiTro(String maHocKy, String tenPhieu, String maNhom);

	List<PhieuCham> layDsPhieuChamQL(String maHocky);

	List<PhieuCham> layDsPhieuChamVaiTroQL(String maHocky, String tenPhieu);

	List<PhieuCham> layDsPhieuChamPosterVaiTroQL(String maHocky, String viTriPhanCong);

	List<PhieuCham> layDsPhieuChamHoiDongVaiTroQL(String maHocky, String viTriPhanCong);

}
