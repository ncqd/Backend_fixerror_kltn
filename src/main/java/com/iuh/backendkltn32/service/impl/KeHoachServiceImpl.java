package com.iuh.backendkltn32.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.KeHoach;
import com.iuh.backendkltn32.repository.HocKyRepository;
import com.iuh.backendkltn32.repository.KeHoachRepository;
import com.iuh.backendkltn32.service.KeHoachService;

@Service
public class KeHoachServiceImpl implements KeHoachService {

	@Autowired
	private KeHoachRepository repository;
	
	@Autowired
	private HocKyRepository hocKyRepository;

	@Override
	public KeHoach layTheoMa(String ma) throws Exception {
		if (ma == null || ma.equals("")) {
			throw new RuntimeException("Mã không được phép rỗng");
		}
		System.out.println("Khoa service - layTheoMa: " + ma);
		KeHoach keHoach = repository.findById(Integer.parseInt(ma)).orElse(null);

		return keHoach;
	}

	@Override
	public KeHoach luu(KeHoach obj) throws Exception {

		return repository.save(obj);
	}

	@Override
	public String xoa(String ma) throws Exception {
		KeHoach khKhongTonTai = layTheoMa(ma);

		if (khKhongTonTai == null) {
			throw new RuntimeException("Kế hoạch không tồn tại");
		}
		repository.deleteById(Integer.parseInt(ma));

		return "Xóa thành công";
	}

	@Override
	public KeHoach capNhat(KeHoach obj) throws Exception {
		KeHoach khKhongTonTai = layTheoMa(obj.getId().toString());
		
		

		if (khKhongTonTai == null) {
			throw new RuntimeException("Kế hoạch không tồn tại");
		}
		khKhongTonTai.setHocKy(obj.getHocKy());
		khKhongTonTai.setDsNgayThucHienKhoaLuan(obj.getDsNgayThucHienKhoaLuan());
		khKhongTonTai.setTenKeHoach(obj.getTenKeHoach());
		khKhongTonTai.setThoiGianBatDau(obj.getThoiGianBatDau());
		khKhongTonTai.setThoiGianKetThuc(obj.getThoiGianKetThuc());
		khKhongTonTai.setTinhTrang(obj.getTinhTrang());

		return repository.save(khKhongTonTai);
	}

	@Override
	public List<KeHoach> layKeHoachTheoMaHocKy(String maHocKy) {
		HocKy hocKy = hocKyRepository.findById(maHocKy).orElseThrow();
		return repository.findByHocKy(hocKy);
	}

	@Override
	public List<KeHoach> layKeHoachTheoVaiTro(String maHocKy, String vaiTro) {
		return repository.layKeHoachTheoVaiTro(maHocKy, vaiTro);
	}
	
	@Override
	public List<KeHoach> layKeHoachTheoMaNguoiDung(String maHocKy, String maNguoiDung) {
		return repository.layKeHoachTheoMaNguoiDung(maHocKy, maNguoiDung);
	}

	@Override
	public List<KeHoach> layTheoTenVaMaHocKyVaiTro(String maHocKy, String tenKeHoach, String vaiTro) {
		// TODO Auto-generated method stub
		return repository.layTheoTenVaMaHocKyVaiTro(maHocKy, tenKeHoach, vaiTro);
	}

	@Override
	public List<KeHoach> layKeHoachTheoMaHocKyVaMaLoai(String maHocKy, String maLoai, String maNguoiDung) {
		// TODO Auto-generated method stub
		return repository.layKeHoachTheoMaNguoiDungVaMaLoai(maHocKy, maLoai, maNguoiDung);
	}

	@Override
	public List<String> layMaNGuoiDung(Timestamp thoiGianBatDau, String phong) {
		// TODO Auto-generated method stub
		return repository.layMaNGuoiDung(thoiGianBatDau, phong);
	}

}
