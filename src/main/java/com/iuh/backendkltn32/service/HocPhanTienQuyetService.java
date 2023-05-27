package com.iuh.backendkltn32.service;

import java.util.List;

import com.iuh.backendkltn32.entity.HocPhanTienQuyet;

public interface HocPhanTienQuyetService extends AbstractService<HocPhanTienQuyet> {
	
	List<HocPhanTienQuyet> getAll();

}
