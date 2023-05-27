package com.iuh.backendkltn32.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.backendkltn32.entity.HocPhanTienQuyet_SinhVien;
import com.iuh.backendkltn32.entity.KeHoach;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.repository.HPTQ_SinhVienRepository;
import com.iuh.backendkltn32.repository.SinhVienRepository;
import com.iuh.backendkltn32.service.HPTQ_SinhVienService;

@Service
public class HPTQ_SinhVienServiceImpl implements HPTQ_SinhVienService {
	
	@Autowired
	private HPTQ_SinhVienRepository repository;
	
	@Autowired
	private SinhVienRepository svRepository;

	@Override
	public HocPhanTienQuyet_SinhVien layTheoMa(String ma) throws RuntimeException {
		if (ma == null || ma.equals("")) {
			throw new RuntimeException("Mã không được phép rỗng");
		}
		System.out.println("Khoa service - layTheoMa: " + ma);
		HocPhanTienQuyet_SinhVien hocPhanTienQuyet_SinhVien = repository.findById(Long.parseLong(ma)).orElse(null);

		return hocPhanTienQuyet_SinhVien;
	}

	@Override
	public HocPhanTienQuyet_SinhVien luu(HocPhanTienQuyet_SinhVien obj) throws RuntimeException {
		return repository.save(obj);
	}

	@Override
	public String xoa(String ma) throws RuntimeException {
		HocPhanTienQuyet_SinhVien khKhongTonTai = layTheoMa(ma);

		if (khKhongTonTai == null) {
			throw new RuntimeException("Kế hoạch không tồn tại");
		}
		repository.deleteById(Long.parseLong(ma));

		return "Xóa thành công";
	}

	@Override
	public HocPhanTienQuyet_SinhVien capNhat(HocPhanTienQuyet_SinhVien obj) throws RuntimeException {
		return null;
	}

	@Override
	public List<HocPhanTienQuyet_SinhVien> layTheoMaSV(String maSv) {
		SinhVien sv = svRepository.findById(maSv).get();
		return repository.findBySinhVien(sv);
	}


}
