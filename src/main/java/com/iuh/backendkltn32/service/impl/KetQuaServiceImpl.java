package com.iuh.backendkltn32.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.backendkltn32.entity.KetQua;
import com.iuh.backendkltn32.repository.KetQuaRepository;
import com.iuh.backendkltn32.service.KetQuaService;

@Service
public class KetQuaServiceImpl implements KetQuaService {

	@Autowired
	private KetQuaRepository repository;


	@Override
	public KetQua layTheoMa(String ma) throws RuntimeException {
		if (ma == null || ma.equals("")) {
			throw new RuntimeException("Mã không được phép rỗng");
		}
		System.out.println("Khoa service - layTheoMa: " + ma);
		KetQua KetQua = repository.findById(Long.parseLong(ma)).orElse(null);

		return KetQua;
	}

	@Override
	public KetQua luu(KetQua obj) throws RuntimeException {

		return repository.save(obj);
	}

	@Override
	public String xoa(String ma) throws RuntimeException {
		KetQua khKhongTonTai = layTheoMa(ma);

		if (khKhongTonTai == null) {
			throw new RuntimeException("Kế hoạch không tồn tại");
		}
		repository.deleteById(Long.parseLong(ma));

		return "Xóa thành công";
	}

	@Override
	public KetQua capNhat(KetQua obj) throws RuntimeException {
		KetQua khKhongTonTai = layTheoMa(obj.getId().toString());

		if (khKhongTonTai == null) {
			throw new RuntimeException("Kế hoạch không tồn tại");
		}
		
		return repository.save(khKhongTonTai);
	}

	@Override
	public List<KetQua> layKetQuaMaSinHVien(String maSinhVien) {
		return repository.layDiemBaoByMaSv(maSinhVien);
	}

	@Override
	public List<KetQua> capNhatDiemBao(List<KetQua> ketQuas) {
		return repository.saveAll(ketQuas);
	}


}
