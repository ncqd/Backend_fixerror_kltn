package com.iuh.backendkltn32.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.backendkltn32.entity.TieuChiChamDiem;
import com.iuh.backendkltn32.repository.TieuChiChamDiemRepository;
import com.iuh.backendkltn32.service.TieuChiChamDiemService;

@Service
public class TieuChiChamDiemServiceImpl implements TieuChiChamDiemService{
	
	@Autowired
	private TieuChiChamDiemRepository repository;

	@Override
	public TieuChiChamDiem layTheoMa(String ma) throws Exception {
		if (ma == null || ma.equals("")) {
			throw new RuntimeException("Mã không được phép rỗng");
		}
		System.out.println("TieuChiChamDiem service - layTheoMa: " + ma);
		TieuChiChamDiem tieuChiChamDiem = repository.findById(Integer.parseInt(ma)).orElse(null);

		return tieuChiChamDiem;
	}

	@Override
	public TieuChiChamDiem luu(TieuChiChamDiem obj) throws Exception {
		return repository.save(obj);
	}

	@Override
	public String xoa(String ma) throws Exception {
		TieuChiChamDiem tieuChiChamDiemKhongTonTai = layTheoMa(ma);

		if (tieuChiChamDiemKhongTonTai == null) {
			throw new RuntimeException("Tiêu chí không tồn tại");
		}
		repository.deleteById(Integer.parseInt(ma));

		return "Xóa thành công";
	}

	@Override
	public TieuChiChamDiem capNhat(TieuChiChamDiem obj) throws Exception {
		TieuChiChamDiem tieuChiChamDiemKhongTonTai = layTheoMa(obj.getMaChuanDauRa()+"");

		if (tieuChiChamDiemKhongTonTai == null) {
			throw new RuntimeException("Tiêu chí không tồn tại");
		}
		tieuChiChamDiemKhongTonTai.setDiemToiDa(obj.getDiemToiDa());
		tieuChiChamDiemKhongTonTai.setTenChuanDauRa(obj.getTenChuanDauRa());
		
		repository.save(tieuChiChamDiemKhongTonTai);

		return tieuChiChamDiemKhongTonTai;
	}

	@Override
	public List<TieuChiChamDiem> laydsTieuChiChamDiemTheoPhieuCham(String maPhieu) {
		return repository.layTieuChiChamDiemTheoPhieuCham(maPhieu);
	}

}
