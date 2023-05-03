package com.iuh.backendkltn32.service;

import java.util.List;

import com.iuh.backendkltn32.entity.Phong;

public interface PhongService extends AbstractService<Phong>{
	
	List<Phong> layHetPhong();

}
