package com.iuh.backendkltn32.service;

import java.util.List;

import com.iuh.backendkltn32.entity.DeTai;

public interface DeTaiService extends AbstractService<DeTai> {
	
	List<DeTai> layDsDeTaiTheoNamHocKy(String namHocKy) throws Exception ;
	
	List<DeTai> layDsDeTaiTheoNamHocKyDaDuyet(String namHocKy) throws Exception ;
	Integer laySoNhomDaDangKyDeTai(String maDeTai);
	
	DeTai getDeTaiCuoiCungTrongHocKy(String namHocKy);
}
