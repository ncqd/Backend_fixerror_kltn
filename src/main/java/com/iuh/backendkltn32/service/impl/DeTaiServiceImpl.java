package com.iuh.backendkltn32.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.iuh.backendkltn32.entity.DeTai;
import com.iuh.backendkltn32.repository.DeTaiRepository;
import com.iuh.backendkltn32.service.DeTaiService;

@Service
public class DeTaiServiceImpl implements DeTaiService {

	@Autowired
	private DeTaiRepository repository;

	@Override
	public DeTai layTheoMa(String ma) throws Exception {
		if (ma == null || ma.equals("")) {
			throw new RuntimeException("Mã không được phép rỗng");
		}
		System.out.println("DeTai service - layTheoMa: " + ma);
		DeTai deTai = repository.findById(ma).orElse(null);

		return deTai;
	}

	@Override
	public DeTai luu(DeTai obj) throws Exception {
		DeTai deTaiDaTonTai = layTheoMa(obj.getMaDeTai());

		System.out.println("DeTai service - luu: " + deTaiDaTonTai);

		if (deTaiDaTonTai != null) {
			throw new RuntimeException("Đề Tài đã tồn tại");
		}
		repository.save(obj);
		return obj;
	}

	@Override
	public String xoa(String ma) throws Exception {
		DeTai deTaiKhongTonTai = layTheoMa(ma);
		if (deTaiKhongTonTai == null) {
			throw new RuntimeException("Đề tài không tồn tại");
		}
		repository.deleteById(ma);
		
		return "Xóa thành công";
	}

	@Override
	public DeTai capNhat(DeTai obj) throws Exception {
		DeTai deTaiCanCapNhat = layTheoMa(obj.getMaDeTai());

		if (deTaiCanCapNhat == null) {
			throw new RuntimeException("Đề tài không tồn tại");
		}
		
		deTaiCanCapNhat.setGiangVien(obj.getGiangVien());
		deTaiCanCapNhat.setGioiHanSoNhomThucHien(obj.getGioiHanSoNhomThucHien());
		deTaiCanCapNhat.setHocKy(obj.getHocKy());
		deTaiCanCapNhat.setMoTa(obj.getMoTa());
		deTaiCanCapNhat.setMucTieuDeTai(obj.getMucTieuDeTai());
		deTaiCanCapNhat.setSanPhamDuKien(obj.getSanPhamDuKien());
		deTaiCanCapNhat.setTenDeTai(obj.getTenDeTai());
		deTaiCanCapNhat.setYeuCauDauVao(obj.getYeuCauDauVao());
		
		repository.save(deTaiCanCapNhat);
		return deTaiCanCapNhat;
	}

	@Override
	public List<DeTai> layDsDeTaiTheoNamHocKy(String maHocKy, String soHocKy, String maGiangVien) throws Exception {
	
		List<DeTai> dsDeTai = repository.layDsDeTaiTheoNamHocKy(maHocKy, soHocKy, maGiangVien);
		
		return dsDeTai;
	}

	@Override
	public List<DeTai> layDsDeTaiTheoNamHocKyDaDuyet(String maHocKy, String soHocKy) throws Exception {
		// TODO Auto-generated method stub
		return repository.layDsDeTaiTheoNamHocKyDaPheDuyet(maHocKy, soHocKy);
	}

	@Override
	public Integer laySoNhomDaDangKyDeTai(String maDeTai) {
		// TODO Auto-generated method stub
		return repository.laySoNhomDaDangKyDeTai(maDeTai);
	}

	@Override
	public DeTai getDeTaiCuoiCungTrongHocKy(String namHocKy) {
		
		DeTai deTai = repository.layDeTaiCuoiCungTrongNamHocKy(namHocKy);
		
		return deTai;
	}

}
