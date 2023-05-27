package com.iuh.backendkltn32.service;

import java.util.List;

import com.iuh.backendkltn32.entity.HocPhanTienQuyet_SinhVien;

public interface HPTQ_SinhVienService extends AbstractService<HocPhanTienQuyet_SinhVien> {
	
	List<HocPhanTienQuyet_SinhVien> layTheoMaSV(String maSv);

}
