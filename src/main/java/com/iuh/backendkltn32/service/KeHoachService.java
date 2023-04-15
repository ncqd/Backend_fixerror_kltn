package com.iuh.backendkltn32.service;

import java.util.List;


import com.iuh.backendkltn32.entity.KeHoach;

public interface KeHoachService extends AbstractService<KeHoach>{
	
	List<KeHoach> layKeHoachTheoMaHocKy(String maHocKy);
	
	List<KeHoach> layKeHoachTheoVaiTro( String maHocKy,String vaiTro);
	
	List<KeHoach> layKeHoachTheoMaNguoiDung( String maHocKy,String maNguoiDung);

}