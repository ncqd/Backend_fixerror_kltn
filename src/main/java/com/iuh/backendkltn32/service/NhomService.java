package com.iuh.backendkltn32.service;

import java.util.List;

import com.iuh.backendkltn32.entity.Nhom;

public interface NhomService extends AbstractService<Nhom>{
	
	List<Nhom> layTatCaNhom(String maHocKy, String soHocKy);

	Integer laySoSinhVienTrongNhomTheoMa(String maNhom);
	
	Nhom layNhomTheoThoiGianHienThuc(String maNhom);
	
	List<Nhom> layDSNhomTheMaGiangVien(String maHocKy, String soHocKy, String maGiangVien);
	
	List<Nhom> layTatCaNhomTheoTinhTrang(String maHocKy, String soHocKy, Integer tinhTrang);

}
