package com.iuh.backendkltn32.service;

import java.util.List;


import com.iuh.backendkltn32.entity.PhieuCham;

public interface PhieuChamService extends AbstractService<PhieuCham> {
	
	List<PhieuCham> layDsPhieuCham(String maGiangVien, String maHocky);
	
	List<PhieuCham> layDsPhieuChamVaiTro(String maGiangVien, String maHocky, String tenPhieu);
	
	List<PhieuCham> layDsPhieuChamPosterVaiTro(String maGiangVien, String maHocky, String viTriPhanCong);
	
	List<PhieuCham> layDsPhieuChamHoiDongVaiTro(String maGiangVien, String maHocky, String viTriPhanCong);
	
	List<PhieuCham> layPhieuTheoMaSinhVienTenVaiTro(String maSinhVien, String tenPhieu);

}
