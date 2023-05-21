package com.iuh.backendkltn32.service;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.PhieuCham;

public interface PhieuChamService extends AbstractService<PhieuCham> {

	List<PhieuCham> layDsPhieuCham(String maGiangVien, String maHocky);

	List<PhieuCham> layDsPhieuChamVaiTro(String maGiangVien, String maHocky, String tenPhieu);

	List<PhieuCham> layDsPhieuChamPosterVaiTro(String maGiangVien, String maHocky, String viTriPhanCong, String tenPhieu);

	List<PhieuCham> layDsPhieuChamHoiDongVaiTro(String maGiangVien, String maHocky, String viTriPhanCong, String tenPhieu);

	List<PhieuCham> layPhieuTheoMaSinhVienTenVaiTro(String maSinhVien, String tenPhieu);

	List<String> layMaPhieuPhieuTheoMaSinhVienTenVaiTro(String maHocKy, String tenPhieu, String maNhom);

	List<PhieuCham> layDsPhieuChamQL(String maHocky, String maSInhVien);

	List<PhieuCham> layDsPhieuChamVaiTroQL(String maHocky, String tenPhieu, String maSInhVien);

	List<PhieuCham> layDsPhieuChamPosterVaiTroQL(String maHocky, String viTriPhanCong, String maSInhVien, String tenPhieu);

	List<PhieuCham> layDsPhieuChamHoiDongVaiTroQL(String maHocky, String viTriPhanCong, String maSInhVien, String tenPhieu);

}
