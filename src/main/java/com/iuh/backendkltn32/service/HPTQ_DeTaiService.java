package com.iuh.backendkltn32.service;

import java.util.List;

import com.iuh.backendkltn32.entity.HocPhanTienQuyet_DeTai;

public interface HPTQ_DeTaiService extends AbstractService<HocPhanTienQuyet_DeTai> {

	List<HocPhanTienQuyet_DeTai> layTheoMaDT(String maDeTai);
}
