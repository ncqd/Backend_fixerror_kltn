package com.iuh.backendkltn32.service;

import java.util.List;

import com.iuh.backendkltn32.entity.KeHoach;

public interface KeHoachService extends AbstractService<KeHoach>{
	
	List<KeHoach> layKeHoachTheoMaHocKy(String maHocKy);

}
