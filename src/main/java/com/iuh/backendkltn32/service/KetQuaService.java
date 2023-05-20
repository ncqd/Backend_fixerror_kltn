package com.iuh.backendkltn32.service;

import java.util.List;

import com.iuh.backendkltn32.entity.KetQua;

public interface KetQuaService extends AbstractService<KetQua>{

	List<KetQua> layKetQuaMaSinHVien(String maSinhVien);
	
	List<KetQua> capNhatDiemBao(List<KetQua> ketQuas);
}
