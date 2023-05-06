package com.iuh.backendkltn32.service;

import java.util.List;

import com.iuh.backendkltn32.entity.TieuChiChamDiem;

public interface TieuChiChamDiemService extends AbstractService<TieuChiChamDiem> {
	
	List<TieuChiChamDiem> laydsTieuChiChamDiemTheoPhieuCham(String maPhieu);
	
	List<TieuChiChamDiem> layHet();
	
	List<TieuChiChamDiem> luuDs(List<TieuChiChamDiem> dsChamDiems);

}
