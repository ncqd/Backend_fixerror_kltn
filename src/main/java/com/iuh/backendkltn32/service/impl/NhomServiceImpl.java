package com.iuh.backendkltn32.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.repository.NhomRepository;
import com.iuh.backendkltn32.service.NhomService;

import javax.transaction.Transactional;

@Service
@Transactional
public class NhomServiceImpl implements NhomService{
	
	@Autowired
	private NhomRepository repository;
	

	@Override
	public Nhom layTheoMa(String ma) throws RuntimeException {
		if (ma == null || ma.equals("")) {
			throw new RuntimeException("Mã không được phép rỗng");
		}
		System.out.println("Nhom service - layTheoMa: " + ma);
		Nhom nhom = repository.findById(ma).orElse(null);

		return nhom;
	}

	@Override
	public Nhom luu(Nhom obj) throws RuntimeException {
		Nhom nhomDaTonTai = layTheoMa(obj.getMaNhom());

		System.out.println("Nhom service - luu: " + nhomDaTonTai);

		if (nhomDaTonTai != null) {
			throw new RuntimeException("Nhóm đã tồn tại");
		}
		repository.save(obj);

		return obj;
	}

	@Override
	public String xoa(String ma) throws RuntimeException {
		Nhom nhomKhongTonTai = layTheoMa(ma);

		if (nhomKhongTonTai == null) {
			throw new RuntimeException("Nhóm không tồn tại");
		}
		repository.deleteById(ma);

		return "Xóa thành công";
	}

	@Override
	public Nhom capNhat(Nhom obj) throws RuntimeException {
		Nhom nhomKhongTonTai = layTheoMa(obj.getMaNhom());

		if (nhomKhongTonTai == null) {
			throw new RuntimeException("Nhóm không tồn tại");
		}
		nhomKhongTonTai.setDeTai(obj.getDeTai());
		nhomKhongTonTai.setTenNhom(obj.getTenNhom());

		return repository.save(nhomKhongTonTai);
	}

	@Override
	public List<Nhom> layTatCaNhom(String hocKy, String namHoc) {
		return repository.layNhomTheoNamHocKy(hocKy, namHoc);
	}

	@Override
	public Integer laySoSinhVienTrongNhomTheoMa(String maNhom) {
		return repository.laySoSinhVienTrongNhomTheoMa(maNhom);
	}

	@Override
	public Nhom layNhomTheoThoiGianHienThuc(String maNhom) {
		return repository.layNhomTheoThoiGianThuc(maNhom);
	}

	@Override
	public List<Nhom> layDSNhomTheMaGiangVien(String maHocKy, String soHocKy, String maGiangVien) {
		System.out.println(maHocKy+ " " + soHocKy+ " "  + maGiangVien + "aaa");
		return repository.layNhomTheoMaGiangVien(maHocKy, soHocKy, maGiangVien);
	}

	@Override
	public List<Nhom> layTatCaNhomTheoTinhTrang(String maHocKy, String soHocKy, Integer tinhTrang) {
		return repository.layNhomTheoTinhTrang(maHocKy, soHocKy, tinhTrang);
	}

	@Override
	public List<Nhom> layNhomTheoVaiTro(String maHocKy, String viTriPhaCong, String maGiangVien) {
		return repository.layNhomTheoPhanCongHK(maHocKy, viTriPhaCong, maGiangVien);
	}

	@Override
	public List<Nhom> layNhomTheoHK(String hocKy, String magiangvien) {
		return repository.layNhomTheoHK(hocKy, magiangvien);
	}

	@Override
	public List<Nhom> layNhomTheoPPChamHD(String hocKy, String magiangvien, String viTriPhanCong) {
		return repository.layNhomTheoPPChamHD(hocKy, magiangvien, viTriPhanCong);
	}

	@Override
	public List<Nhom> layNhomTheoPPChamPoster(String hocKy, String magiangvien, String viTriPhanCong) {
		return repository.layNhomTheoPPChamPoster(hocKy, magiangvien, viTriPhanCong);
	}

	@Override
	public List<Nhom> layNhomRaDuocPBHD(String hocKy) {
		return repository.layNhomRaDuocPBHD(hocKy);
	}

	@Override
	public List<Nhom> layNhomRaDuocPBPoster(String hocKy) {
		// TODO Auto-generated method stub
		return repository.layNhomRaDuocPBPoster(hocKy);
	}

}
