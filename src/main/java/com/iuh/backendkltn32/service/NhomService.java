package com.iuh.backendkltn32.service;

import java.util.List;

import com.iuh.backendkltn32.entity.Nhom;

public interface NhomService extends AbstractService<Nhom>{
	
	List<Nhom> layTatCaNhom(Integer hocKy, String namHoc);

}
