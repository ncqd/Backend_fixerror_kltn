package com.iuh.backendkltn32.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.backendkltn32.entity.HocPhanTienQuyet;
import com.iuh.backendkltn32.repository.HocPhanTienQuyetRepository;
import com.iuh.backendkltn32.service.HocPhanTienQuyetService;

@Service
public class HocPhanTienQuyetServiceImpl implements HocPhanTienQuyetService {
	
	@Autowired
	private HocPhanTienQuyetRepository repository;

	@Override
	public HocPhanTienQuyet layTheoMa(String ma) throws RuntimeException {
		if (ma == null || ma.equals("")) {
			throw new RuntimeException("Mã không được phép rỗng");
		}
		System.out.println("HPTQ service - layTheoMa: " + ma);
		HocPhanTienQuyet hocPhanTienQuyet = repository.findById(ma).orElse(null);
		return hocPhanTienQuyet;
	}

	@Override
	public HocPhanTienQuyet luu(HocPhanTienQuyet obj) throws RuntimeException {
		HocPhanTienQuyet hocPhanKhoaLuanTotNghiepDaTonTai = layTheoMa(obj.getMaHocPhan());

		System.out.println("HocPhanTienQuyet service - luu: " + hocPhanKhoaLuanTotNghiepDaTonTai);

		if (hocPhanKhoaLuanTotNghiepDaTonTai != null) {
			throw new RuntimeException("Học Phần đã tồn tại");
		}
		
		return repository.save(obj);
	}

	@Override
	public String xoa(String ma) throws RuntimeException {
		HocPhanTienQuyet hocPhanKhoaLuanTotNghiepKhongTonTai = layTheoMa(ma);

		if (hocPhanKhoaLuanTotNghiepKhongTonTai == null) {
			throw new RuntimeException("Học Phần không tồn tại");
		}
		repository.deleteById(ma);

		return "Xóa thành công";
	}

	@Override
	public HocPhanTienQuyet capNhat(HocPhanTienQuyet obj) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HocPhanTienQuyet> getAll() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	

}
