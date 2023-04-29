package com.iuh.backendkltn32.service;

import java.util.List;

import com.iuh.backendkltn32.entity.DiemThanhPhan;

public interface DiemThanhPhanService extends AbstractService<DiemThanhPhan> {
	
	List<DiemThanhPhan> layDsDiemThanhPhan(String maPhieu);
	
	List<DiemThanhPhan> capNhatAll(List<DiemThanhPhan> diemThanhPhans);
	
	

}
