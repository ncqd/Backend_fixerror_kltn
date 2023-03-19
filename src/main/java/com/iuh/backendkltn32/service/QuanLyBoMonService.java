package com.iuh.backendkltn32.service;

import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.QuanLyBoMon;

public interface QuanLyBoMonService extends AbstractService<QuanLyBoMon> {
	
		GiangVien layTheoMaGiangVien(String maGiangVien);

}
