package com.iuh.backendkltn32.service;

import java.util.List;

import com.iuh.backendkltn32.entity.PhieuChamMau;

public interface PhieuChamMauService extends AbstractService<PhieuChamMau>{
	
	List<PhieuChamMau> layHet(String vaiTroNguoiDung, String maHocKy);

}
