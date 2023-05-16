package com.iuh.backendkltn32.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.backendkltn32.entity.DeTai;
import com.iuh.backendkltn32.repository.DeTaiRepository;
import com.iuh.backendkltn32.service.DeTaiService;

@Service
public class DeTaiServiceImpl implements DeTaiService {

	@Autowired
	private DeTaiRepository repository;

	@Override
	public DeTai layTheoMa(String ma) throws RuntimeException {
		if (ma == null || ma.equals("")) {
			throw new RuntimeException("Mã không được phép rỗng");
		}
		System.out.println("DeTai service - layTheoMa: " + ma);
		DeTai deTai = repository.findById(ma).orElse(null);

		return deTai;
	}

	@Override
	public DeTai luu(DeTai obj) throws RuntimeException {
		DeTai deTaiDaTonTai = layTheoMa(obj.getMaDeTai());

		System.out.println("DeTai service - luu: " + deTaiDaTonTai);

		if (deTaiDaTonTai != null) {
			throw new RuntimeException("Đề Tài đã tồn tại");
		}
		repository.save(obj);
		return obj;
	}

	@Override
	public String xoa(String ma) throws RuntimeException {
		DeTai deTaiKhongTonTai = layTheoMa(ma);
		if (deTaiKhongTonTai == null) {
			throw new RuntimeException("Đề tài không tồn tại");
		}
		repository.deleteById(ma);
		
		return "Xóa thành công";
	}

	@Override
	public DeTai capNhat(DeTai obj) throws RuntimeException {
		DeTai deTaiCanCapNhat = layTheoMa(obj.getMaDeTai());

		if (deTaiCanCapNhat == null) {
			throw new RuntimeException("Đề tài không tồn tại");
		}
		
		deTaiCanCapNhat.setGiangVien(deTaiCanCapNhat.getGiangVien());
		deTaiCanCapNhat.setGioiHanSoNhomThucHien(obj.getGioiHanSoNhomThucHien());
		deTaiCanCapNhat.setHocKy(obj.getHocKy());
		deTaiCanCapNhat.setMoTa(obj.getMoTa());
		deTaiCanCapNhat.setMucTieuDeTai(obj.getMucTieuDeTai());
		deTaiCanCapNhat.setSanPhamDuKien(obj.getSanPhamDuKien());
		deTaiCanCapNhat.setTenDeTai(obj.getTenDeTai());
		deTaiCanCapNhat.setYeuCauDauVao(obj.getYeuCauDauVao());
		deTaiCanCapNhat.setTrangThai(obj.getTrangThai());
		
		repository.save(deTaiCanCapNhat);
		return deTaiCanCapNhat;
	}

	@Override
	public List<DeTai> layDsDeTaiTheoNamHocKy(String maHocKy, String soHocKy, String maGiangVien) throws RuntimeException {
	
		List<DeTai> dsDeTai = repository.layDsDeTaiTheoNamHocKyTheoMaGiangVien(maHocKy, soHocKy, maGiangVien);
		
		return dsDeTai;
	}

	@Override
	public List<DeTai> layDsDeTaiTheoNamHocKyDaDuyet(String maHocKy, String soHocKy) throws RuntimeException {
		// TODO Auto-generated method stub
		return repository.layDsDeTaiTheoNamHocKyDaPheDuyet(maHocKy, soHocKy);
	}

	@Override
	public Integer laySoNhomDaDangKyDeTai(String maDeTai) {
		
		return repository.laySoNhomDaDangKyDeTai(maDeTai);
	}

	@Override
	public DeTai getDeTaiCuoiCungTrongHocKy(String maHocKy, String soHocKy) {
		
		DeTai deTai = repository.layDeTaiCuoiCungTrongNamHocKy(maHocKy, soHocKy);
//		System.out.println("De tai service - getDeTaiCuoiCungTrongHocKy - " + deTai.toString());
		
		return deTai;
	}


//	@Override
//	public List<DeTai> layDsDeTaiTheoNamHocKyChuaDat(String maHocKy, String soHocKy, String maGiangVien)
//			throws RuntimeException {
//		// TODO Auto-generated method stub
//		return repository.layDsDeTaiTheoNamHocKyChuaDat(maHocKy, soHocKy, maGiangVien);
//	}

	@Override
	public List<DeTai> layDsDeTaiTheoNamHocKyTheoTrangThai(String maHocKy, String soHocKy, String maGiangVien, Integer trangThai)
			throws RuntimeException {
		return repository.layDsDeTaiTheoNamHocKyTheoTrangThaiCoMaGv(maHocKy, soHocKy, maGiangVien, trangThai);
	}

	@Override
	public List<DeTai> luuDanhSach(List<DeTai> deTais) {
		// TODO Auto-generated method stub
		return repository.saveAll(deTais);
	}

	@Override
	public List<DeTai> layDsDeTaiTheoNamHocKy(String maHocKy, String soHocKy) throws RuntimeException {
		// TODO Auto-generated method stub
		return repository.layDsDeTaiTheoNamHocKy(maHocKy, soHocKy);
	}

	@Override
	public List<DeTai> layDsDeTaiTheoTrangThaiKhongMaGV(String maHocKy, String soHocKy, Integer trangThai)
			throws RuntimeException {
		// TODO Auto-generated method stub
		return repository.layDsDeTaiTheoNamHocKyTheoTrangThai(maHocKy, soHocKy, trangThai);
	}

	@Override
	public List<DeTai> layDsDeTaiXuaExcel(String maHocKy) {
		// TODO Auto-generated method stub
		return repository.layDsDeTaiXuaExcel(maHocKy);
	}

}
