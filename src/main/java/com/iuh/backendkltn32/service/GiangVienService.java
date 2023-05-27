package com.iuh.backendkltn32.service;

import java.util.List;

import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.SinhVien;

public interface GiangVienService extends AbstractService<GiangVien> {
	
	List<GiangVien> luuDanhSach(List<GiangVien> deTais);
	
	List<GiangVien> layDanhSach();
	
	Integer layDanhSachTT(String magv, String maHocKy);

}
