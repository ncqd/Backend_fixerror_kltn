package com.iuh.backendkltn32.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.backendkltn32.entity.PhieuCham;
import com.iuh.backendkltn32.entity.PhieuChamMau;
import com.iuh.backendkltn32.entity.TieuChiChamDiem;
import com.iuh.backendkltn32.repository.PhieuChamMauRepository;
import com.iuh.backendkltn32.repository.PhieuChamRepository;
import com.iuh.backendkltn32.service.PhieuChamMauService;

@Service
public class PhieuChamMauServiceImpl implements PhieuChamMauService{
	
	@Autowired
	private PhieuChamMauRepository repository;

	@Override
	public PhieuChamMau layTheoMa(String ma) throws RuntimeException {
		if (ma == null || ma.equals("")) {
			throw new RuntimeException("Mã không được phép rỗng");
		}
		System.out.println("TieuChiChamDiem service - layTheoMa: " + ma);
		PhieuChamMau phieuChamMau = repository.findById(Integer.parseInt(ma)).orElse(null);

		return phieuChamMau;
	}

	@Override
	public PhieuChamMau luu(PhieuChamMau obj) throws Exception {

		return repository.save(obj);
	}

	@Override
	public String xoa(String ma) throws Exception {
		PhieuChamMau phieuChamKhongTonTai = layTheoMa(ma);

		if (phieuChamKhongTonTai == null) {
			throw new RuntimeException("Phiếu không tồn tại");
		}
		repository.deleteById(Integer.parseInt(ma));

		return "Xóa thành công";
	}

	@Override
	public PhieuChamMau capNhat(PhieuChamMau obj) throws Exception {
		PhieuChamMau phieuChamKhongTonTai = layTheoMa(obj.getMaPhieuMau()+"");

		if (phieuChamKhongTonTai == null) {
			throw new RuntimeException("Phiếu không tồn tại");
		}
		phieuChamKhongTonTai.setTenPhieuCham(obj.getTenPhieuCham());
		phieuChamKhongTonTai.setTieuChiChamDiems(obj.getTieuChiChamDiems());
		
		repository.save(phieuChamKhongTonTai);

		return phieuChamKhongTonTai;
	}

	@Override
	public List<PhieuChamMau> layHet(String vaiTroNguoiDung, String maHocKy) {
		return repository.phieuChamMaus(vaiTroNguoiDung, maHocKy);
	}

}
