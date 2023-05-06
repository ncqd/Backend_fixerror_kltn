package com.iuh.backendkltn32.service;

import java.util.List;

import com.iuh.backendkltn32.entity.TinNhan;

public interface TinNhanSerivce extends AbstractService<TinNhan>{

	List<TinNhan> layTinNhanTheoMaNguoiNhan(String maNguoiNhan);
	
	List<TinNhan> layTinNhanTheoMaNguoiNhanCuaXacNhan(String maNguoiNhan);
}
