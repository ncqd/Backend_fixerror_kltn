package com.iuh.backendkltn32.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.PhanCong;
import com.iuh.backendkltn32.entity.PhieuChamMau;
import com.iuh.backendkltn32.repository.PhanCongRepository;
import com.iuh.backendkltn32.service.PhanCongService;

@Service
public class PhanCongServiceImpl implements PhanCongService {
	
	@Autowired
	private PhanCongRepository repository;

	@Override
	public PhanCong layTheoMa(String ma) throws RuntimeException {
		if (ma == null || ma.equals("")) {
			throw new RuntimeException("Mã không được phép rỗng");
		}
		System.out.println("TieuChiChamDiem service - layTheoMa: " + ma);
		PhanCong phanCong = repository.findById(Integer.parseInt(ma)).orElse(null);

		return phanCong;
	}

	@Override
	public PhanCong luu(PhanCong obj) throws RuntimeException {
		return repository.save(obj);
	}

	@Override
	public String xoa(String ma) throws RuntimeException {
		PhanCong phieuChamKhongTonTai = layTheoMa(ma);

		if (phieuChamKhongTonTai == null) {
			throw new RuntimeException("Phiếu không tồn tại");
		}
		repository.deleteById(Integer.parseInt(ma));

		return "Xóa thành công";
	}

	@Override
	public PhanCong capNhat(PhanCong obj) throws RuntimeException {
		PhanCong phieuChamKhongTonTai = layTheoMa(obj.getMaPhanCong()+"");

		if (phieuChamKhongTonTai == null) {
			throw new RuntimeException("Phiếu không tồn tại");
		}
		phieuChamKhongTonTai.setChamCong(obj.getChamCong());
		phieuChamKhongTonTai.setGiangVien(obj.getGiangVien());
		phieuChamKhongTonTai.setNhom(obj.getNhom());
		phieuChamKhongTonTai.setViTriPhanCong(obj.getViTriPhanCong());
		
		repository.save(phieuChamKhongTonTai);

		return phieuChamKhongTonTai;
	}

	@Override
	public List<PhanCong> layPhanCongTheoMaNhom(Nhom nhom) {
		// TODO Auto-generated method stub
		return repository.findByNhom(nhom);
	}

	@Override
	public List<PhanCong> layPhanCongTheoMaNhomVaTen(String maNshom, String ten) {
		// TODO Auto-generated method stub
		return repository.timTheoTenVaMaNhom(maNshom, ten);
	}

}
