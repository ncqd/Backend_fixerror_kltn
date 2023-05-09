package com.iuh.backendkltn32.service;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.Nhom;

public interface NhomService extends AbstractService<Nhom>{
	
	List<Nhom> layTatCaNhom(String maHocKy, String soHocKy);

	Integer laySoSinhVienTrongNhomTheoMa(String maNhom);
	
	Nhom layNhomTheoThoiGianHienThuc(String maNhom);
	
	List<Nhom> layDSNhomTheMaGiangVien(String maHocKy, String soHocKy, String maGiangVien);
	
	List<Nhom> layTatCaNhomTheoTinhTrang(String maHocKy, String soHocKy, Integer tinhTrang);

	List<Nhom> layNhomTheoVaiTro(String maHocKy, String viTriPhaCong, String maGiangVien);
	
	List<Nhom> layNhomTheoHK( String hocKy,String magiangvien);
	
	List<Nhom> layNhomTheoPPChamHD(String hocKy, String magiangvien, String viTriPhanCong);
	
	List<Nhom> layNhomTheoPPChamPoster(String hocKy,  String magiangvien, String viTriPhanCong);
	
	List<Nhom> layNhomRaDuocPBHD(String hocKy);
	
	List<Nhom> layNhomRaDuocPBPoster(String hocKy);
}
