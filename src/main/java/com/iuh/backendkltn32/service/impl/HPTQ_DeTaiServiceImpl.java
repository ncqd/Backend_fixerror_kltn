package com.iuh.backendkltn32.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.backendkltn32.entity.DeTai;
import com.iuh.backendkltn32.entity.HocPhanTienQuyet_DeTai;
import com.iuh.backendkltn32.entity.HocPhanTienQuyet_SinhVien;
import com.iuh.backendkltn32.entity.KeHoach;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.repository.DeTaiRepository;
import com.iuh.backendkltn32.repository.HPTQ_DeTaiRepository;
import com.iuh.backendkltn32.repository.HPTQ_SinhVienRepository;
import com.iuh.backendkltn32.repository.SinhVienRepository;
import com.iuh.backendkltn32.service.HPTQ_DeTaiService;
import com.iuh.backendkltn32.service.HPTQ_SinhVienService;

@Service
public class HPTQ_DeTaiServiceImpl implements HPTQ_DeTaiService {
	
	@Autowired
	private HPTQ_DeTaiRepository repository;
	
	@Autowired
	private DeTaiRepository deTaiRepository;
	
	
	@Override
	public HocPhanTienQuyet_DeTai layTheoMa(String ma) throws RuntimeException {
		if (ma == null || ma.equals("")) {
			throw new RuntimeException("Mã không được phép rỗng");
		}
		System.out.println("Khoa service - layTheoMa: " + ma);
		HocPhanTienQuyet_DeTai hocPhanTienQuyet_SinhVien = repository.findById(Long.parseLong(ma)).orElse(null);

		return hocPhanTienQuyet_SinhVien;
	}

	@Override
	public HocPhanTienQuyet_DeTai luu(HocPhanTienQuyet_DeTai obj) throws RuntimeException {
		return repository.save(obj);
	}

	@Override
	public String xoa(String ma) throws RuntimeException {
		HocPhanTienQuyet_DeTai khKhongTonTai = layTheoMa(ma);

		if (khKhongTonTai == null) {
			throw new RuntimeException("Kế hoạch không tồn tại");
		}
		repository.deleteById(Long.parseLong(ma));

		return "Xóa thành công";
	}


	@Override
	public HocPhanTienQuyet_DeTai capNhat(HocPhanTienQuyet_DeTai obj) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HocPhanTienQuyet_DeTai> layTheoMaDT(String maDeTai) {
		DeTai deTai = deTaiRepository.findById(maDeTai).get();
		return repository.findByDeTai(deTai);
	}


}
