package com.iuh.backendkltn32.service;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.SinhVien;

public interface SinhVienService extends AbstractService<SinhVien>{
	
	List<String> layTatCaSinhVienTheoNhom(String maNhom);
	
	List<SinhVien> layTatCaSinhVien();
	
	List<SinhVien> luuDanhSach(List<SinhVien> deTais);
	
	List<SinhVien> layTatCaSinhVienTheoHocKy(String maHocKy, String soHocKy);
	
	List<String> timMaSinhVienChuaCoPhieuChamDiemTheoNhuCau(String maNhom,String tenPhieu);	
	
	List<String> timMaSinhVienChuaCoPhieuChamDiemTheoNhuCauCoDiemTheoNhuCau(String maNhom,String tenPhieu);
}
