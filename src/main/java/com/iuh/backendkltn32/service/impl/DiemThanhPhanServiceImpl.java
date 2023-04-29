package com.iuh.backendkltn32.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.backendkltn32.entity.DiemThanhPhan;
import com.iuh.backendkltn32.repository.DiemThanhPhanRepository;

@Service
public class DiemThanhPhanServiceImpl implements com.iuh.backendkltn32.service.DiemThanhPhanService {

	@Autowired
	private DiemThanhPhanRepository repository;

	@Override
	public DiemThanhPhan layTheoMa(String ma) throws Exception {
		if (ma == null || ma.equals("")) {
			throw new RuntimeException("Mã không được phép rỗng");
		}
		System.out.println("Khoa service - layTheoMa: " + ma);
		DiemThanhPhan diemThanhPhan = repository.findById(Long.parseLong(ma)).orElse(null);

		return diemThanhPhan;
	}

	@Override
	public DiemThanhPhan luu(DiemThanhPhan obj) throws Exception {
		// TODO Auto-generated method stub
		return repository.save(obj);
	}

	@Override
	public String xoa(String ma) throws Exception {
		DiemThanhPhan khKhongTonTai = layTheoMa(ma);

		if (khKhongTonTai == null) {
			throw new RuntimeException("Kế hoạch không tồn tại");
		}
		repository.deleteById(Long.parseLong(ma));

		return "Xóa thành công";
	}

	@Override
	public DiemThanhPhan capNhat(DiemThanhPhan obj) throws Exception {
		DiemThanhPhan khKhongTonTai = layTheoMa(obj.getId().toString());

		if (khKhongTonTai == null) {
			throw new RuntimeException("Kế hoạch không tồn tại");
		}
		khKhongTonTai.setPhieuCham(obj.getPhieuCham());
		khKhongTonTai.setTieuChiChamDiem(obj.getTieuChiChamDiem());
		khKhongTonTai.setDiemThanhPhan(obj.getDiemThanhPhan());

		return repository.save(khKhongTonTai);
	}

	@Override
	public List<DiemThanhPhan> layDsDiemThanhPhan(String maPhieu) {
		return repository.layTheoMaPhieuCham(maPhieu);
	}

	@Override
	public List<DiemThanhPhan> capNhatAll(List<DiemThanhPhan> diemThanhPhans) {
		// TODO Auto-generated method stub
		return repository.saveAll(diemThanhPhans);
	}

}
