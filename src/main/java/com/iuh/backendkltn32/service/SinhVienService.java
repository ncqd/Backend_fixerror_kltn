package com.iuh.backendkltn32.service;

import java.util.List;

import com.iuh.backendkltn32.entity.SinhVien;

public interface SinhVienService extends AbstractService<SinhVien>{
	
	List<String> layTatCaSinhVienTheoNhom(String maNhom);
	
	List<SinhVien> layTatCaSinhVien();
	
	List<SinhVien> luuDanhSach(List<SinhVien> deTais);
	
	List<SinhVien> layTatCaSinhVienTheoHocKy(String maHocKy, String soHocKy);
	
	

}
