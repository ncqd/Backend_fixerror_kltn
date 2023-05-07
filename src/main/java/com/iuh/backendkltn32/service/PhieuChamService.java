package com.iuh.backendkltn32.service;

import java.util.List;

import com.iuh.backendkltn32.entity.PhieuCham;

public interface PhieuChamService extends AbstractService<PhieuCham> {
	
	List<PhieuCham> layDsPhieuCham(String maGiangVien);

}
