package com.iuh.backendkltn32.service;

import java.util.List;

import com.iuh.backendkltn32.entity.LopHocPhan;

public interface LopHocPhanService extends AbstractService<LopHocPhan>{
	
	List<LopHocPhan> getAll();

}
