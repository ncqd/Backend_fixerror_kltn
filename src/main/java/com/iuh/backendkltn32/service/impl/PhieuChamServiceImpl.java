package com.iuh.backendkltn32.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iuh.backendkltn32.entity.PhieuCham;
import com.iuh.backendkltn32.repository.PhieuChamRepository;
import com.iuh.backendkltn32.service.PhieuChamService;

@Service
@Transactional
public class PhieuChamServiceImpl implements PhieuChamService{
	
	@Autowired
	private PhieuChamRepository repository;

	@Override
	public PhieuCham layTheoMa(String ma) throws Exception {
		if (ma == null || ma.equals("")) {
			throw new RuntimeException("Mã không được phép rỗng");
		}
		System.out.println("PhieuCham service - layTheoMa: " + ma);
		PhieuCham phieuCham = repository.findById(ma).orElse(null);

		return phieuCham;
	}

	@Override
	public PhieuCham luu(PhieuCham obj) throws Exception {
		PhieuCham phieuChamDaTonTai = layTheoMa(obj.getMaPhieu());

		System.out.println("PhieuCham service - luu: " + phieuChamDaTonTai);

		if (phieuChamDaTonTai != null) {
			throw new RuntimeException("Phiếu đã tồn tại");
		}

		return repository.save(obj);
	}

	@Override
	public String xoa(String ma) throws Exception {
		PhieuCham phieuChamKhongTonTai = layTheoMa(ma);

		if (phieuChamKhongTonTai == null) {
			throw new RuntimeException("Phiếu không tồn tại");
		}
		repository.deleteById(ma);

		return "Xóa thành công";
	}

	@Override
	public PhieuCham capNhat(PhieuCham obj) throws Exception {
		PhieuCham phieuChamKhongTonTai = layTheoMa(obj.getMaPhieu());

		if (phieuChamKhongTonTai == null) {
			throw new RuntimeException("Phiếu không tồn tại");
		}
		phieuChamKhongTonTai.setDiemPhieuCham(obj.getDiemPhieuCham());
		phieuChamKhongTonTai.setDsDiemThanhPhan(obj.getDsDiemThanhPhan());
		phieuChamKhongTonTai.setDsKetQua(obj.getDsKetQua());
		phieuChamKhongTonTai.setTenPhieu(obj.getTenPhieu());
		
		repository.save(phieuChamKhongTonTai);

		return phieuChamKhongTonTai;
	}

	@Override
	public List<PhieuCham> layDsPhieuCham(String maGiangVien, String maHocky) {
		return repository.layDsDeTaiTheoNamHocKy(maGiangVien, maHocky);
	}

	@Override
	public List<PhieuCham> layDsPhieuChamVaiTro(String maGiangVien, String maHocky, String tenPhieu) {
		return repository.layPhieuTheoVaiTro(maGiangVien, maHocky, tenPhieu);
	}

	@Override
	public List<PhieuCham> layDsPhieuChamPosterVaiTro(String maGiangVien, String maHocky, String viTriPhanCong) {
		return repository.layPhieuTheoPPChamPOSTER(maGiangVien, maHocky, viTriPhanCong);
	}

	@Override
	public List<PhieuCham> layDsPhieuChamHoiDongVaiTro(String maGiangVien, String maHocky, String viTriPhanCong) {
		return repository.layPhieuTheoPPChamHD(maGiangVien, maHocky, viTriPhanCong);
	}

	@Override
	public List<PhieuCham> layPhieuTheoMaSinhVienTenVaiTro(String maSinhVien, String tenPhieu) {
		return repository.layPhieuTheoMaSinhVienTenVaiTro(maSinhVien, tenPhieu);
	}

	@Override
	public List<String> layMaPhieuPhieuTheoMaSinhVienTenVaiTro(String maHocKy, String tenPhieu, String maNhom) {
		return repository.layMaPhieuPhieuTheoMaSinhVienTenVaiTro(maHocKy, tenPhieu, maNhom);
	}

}
