package com.iuh.backendkltn32.service;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.DeTai;

public interface DeTaiService extends AbstractService<DeTai> {
	
	List<DeTai> layDsDeTaiTheoNamHocKy(String maHocKy, String soHocKy, String maGiangVien) throws Exception;
	
	List<DeTai> layDsDeTaiTheoNamHocKyDaDuyet(String maHocKy, String soHocKy) throws Exception;
	
	Integer laySoNhomDaDangKyDeTai(String maDeTai);
	
	DeTai getDeTaiCuoiCungTrongHocKy(String maHocKy, String soHocKy);
	
	List<DeTai> layDsDeTaiTheoNamHocKyTheoTrangThai(String maHocKy, String soHocKy, String maGiangVien, Integer trangThai) throws Exception;
	
	List<DeTai> luuDanhSach(List<DeTai> deTais);
	
	List<DeTai> layDsDeTaiTheoNamHocKy(String maHocKy, String soHocKy) throws Exception;
	
	List<DeTai> layDsDeTaiTheoTrangThaiKhongMaGV(String maHocKy, String soHocKy, Integer trangThai) throws Exception;
	
	List<DeTai> layDsDeTaiXuaExcel(String maHocKy);
	
}
