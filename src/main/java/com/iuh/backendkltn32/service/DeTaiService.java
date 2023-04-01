package com.iuh.backendkltn32.service;

import java.util.List;

import com.iuh.backendkltn32.entity.DeTai;

public interface DeTaiService extends AbstractService<DeTai> {
	
	List<DeTai> layDsDeTaiTheoNamHocKy(String maHocKy, String soHocKy, String maGiangVien) throws Exception;
	
	List<DeTai> layDsDeTaiTheoNamHocKyDaDuyet(String maHocKy, String soHocKy) throws Exception;
	Integer laySoNhomDaDangKyDeTai(String maDeTai);
	
	DeTai getDeTaiCuoiCungTrongHocKy(String maHocKy, String soHocKy);
	
	List<DeTai> layDsDeTaiTheoNamHocKyChuaDuyet(String maHocKy, String soHocKy, String maGiangVien) throws Exception;
	
	List<DeTai> layDsDeTaiTheoNamHocKyChuaDat(String maHocKy, String soHocKy, String maGiangVien) throws Exception;
}
