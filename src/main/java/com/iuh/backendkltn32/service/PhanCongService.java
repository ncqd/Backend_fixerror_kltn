package com.iuh.backendkltn32.service;

import java.util.List;

import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.PhanCong;

public interface PhanCongService extends AbstractService<PhanCong> {
	
	List<PhanCong> layPhanCongTheoMaNhom(Nhom nhom);

}
