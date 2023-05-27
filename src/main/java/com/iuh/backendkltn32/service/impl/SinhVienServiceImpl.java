package com.iuh.backendkltn32.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.backendkltn32.entity.HocPhanTienQuyet;
import com.iuh.backendkltn32.entity.HocPhanTienQuyet_SinhVien;
import com.iuh.backendkltn32.entity.LopHocPhan;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.repository.HPTQ_SinhVienRepository;
import com.iuh.backendkltn32.repository.HocPhanTienQuyetRepository;
import com.iuh.backendkltn32.repository.SinhVienRepository;
import com.iuh.backendkltn32.service.SinhVienService;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

@Service
public class SinhVienServiceImpl implements  SinhVienService {

	@Autowired
	private SinhVienRepository sinhVienRepository;
	
	@Autowired
	private HocPhanTienQuyetRepository hocPhanTienQuyetRepository;
	
	@Autowired
	private HPTQ_SinhVienRepository  ptq_SinhVienRepository;

	@Override
	public SinhVien layTheoMa(String ma) throws RuntimeException {
		if (ma == null || ma.equals("")) {
			throw new RuntimeException("Mã không được phép rỗng");
		}
		System.out.println("sinh vien service - layTheoMa: " + ma);
		SinhVien sinhVien =  sinhVienRepository.findById(ma).orElse(null);
		
		return sinhVien;
	}

	@Override
	@Transactional
	public SinhVien luu(SinhVien obj) throws RuntimeException {

		SinhVien sinhVienDaTonTai = layTheoMa(obj.getMaSinhVien());
		
		System.out.println("sinh vien service - luu: " + sinhVienDaTonTai);

		if (sinhVienDaTonTai != null) {
			throw new RuntimeException("Sinh Viên đã tồn tại");
		}
		SinhVien sv = sinhVienRepository.save(obj);
		
		HocPhanTienQuyet hocPhanTienQuyet = hocPhanTienQuyetRepository.findById("44200056").get();
		sv.setDsHocPhanTienQuyet_SinhViens(Arrays.asList(
				ptq_SinhVienRepository.save(
						new HocPhanTienQuyet_SinhVien(hocPhanTienQuyet, sv, 8.0))));
		sv = sinhVienRepository.save(obj);
		return sv;
	}

	@Override
	public String xoa(String ma) throws RuntimeException {

		SinhVien sinhVienKhongTonTai = layTheoMa(ma);

		if (sinhVienKhongTonTai == null) {
			throw new RuntimeException("Sinh Viên không tồn tại");
		}
		sinhVienRepository.deleteById(ma);

		return "Xóa thành công";
	}

	@Override
	public SinhVien capNhat(SinhVien obj) throws RuntimeException  {

		SinhVien sinhVienCanCapNhat = layTheoMa(obj.getMaSinhVien());

		if (sinhVienCanCapNhat == null) {
			throw new RuntimeException("Sinh Viên không tồn tại");
		}
		sinhVienCanCapNhat.setDienThoai(obj.getDienThoai());
		sinhVienCanCapNhat.setEmail(obj.getEmail());
		sinhVienCanCapNhat.setGioiTinh(obj.getGioiTinh());
		sinhVienCanCapNhat.setLopDanhNghia(obj.getLopDanhNghia());
		sinhVienCanCapNhat.setLopHocPhan(obj.getLopHocPhan());
		sinhVienCanCapNhat.setDsKetQua(obj.getDsKetQua());
		sinhVienCanCapNhat.setNamNhapHoc(obj.getNamNhapHoc());
		sinhVienCanCapNhat.setNgaySinh(obj.getNgaySinh());
		sinhVienCanCapNhat.setNhom(obj.getNhom());
		sinhVienCanCapNhat.setNoiSinh(obj.getNoiSinh());
		sinhVienCanCapNhat.setTenSinhVien(obj.getTenSinhVien());
		
		sinhVienRepository.save(sinhVienCanCapNhat);

		return sinhVienCanCapNhat;
	}

	@Override
	public List<String> layTatCaSinhVienTheoNhom(String maNhom) {
		
		List<String> sinhViens = sinhVienRepository.findByNhom(maNhom);
		return sinhViens;
	}

	@Override
	public List<SinhVien> layTatCaSinhVien() {
		// TODO Auto-generated method stub
		return sinhVienRepository.findAll();
	}

	@Override
	public List<SinhVien> luuDanhSach(List<SinhVien> deTais) {
		
		List<SinhVien> result = sinhVienRepository.saveAll(deTais);
		HocPhanTienQuyet hocPhanTienQuyet = hocPhanTienQuyetRepository.findById("44200056").get();
		System.out.println(hocPhanTienQuyet);
		for (SinhVien sinhVien : result) {
			List<HocPhanTienQuyet_SinhVien> ds = Arrays.asList(
					ptq_SinhVienRepository.save(
							new HocPhanTienQuyet_SinhVien(hocPhanTienQuyet, sinhVien, 8.0)));
			sinhVien.setDsHocPhanTienQuyet_SinhViens(ds);
		}
		
		return result;
	}

	@Override
	public List<SinhVien> layTatCaSinhVienTheoHocKy(String maHocKy, String soHocKy) {
		// TODO Auto-generated method stub
		return sinhVienRepository.layTatCaSinhVienTheoHocKy(maHocKy, soHocKy);
	}

	@Override
	public List<String> timMaSinhVienChuaCoPhieuChamDiemTheoNhuCau(String maNhom, String tenPhieu, String maGiangVien) {
		return sinhVienRepository.timMaSinhVienChuaCoPhieuChamDiemTheoNhuCau(maNhom, tenPhieu, maGiangVien);
	}

	@Override
	public String timMaSinhVienChuaCoPhieuChamDiemTheoNhuCauCoDiemTheoNhuCau(String maNhom, String tenPhieu) {
		System.out.println(maNhom + " " + tenPhieu);
		return sinhVienRepository.timMaSinhVienChuaCoPhieuChamDiemTheoNhuCauCoDiemLon(maNhom, tenPhieu);
	}

	@Override
	public List<SinhVien> layTatCaSinhVienTheoLopHocPhan(LopHocPhan lopHocPhan) {
		return sinhVienRepository.findByLopHocPhan(lopHocPhan);
	}

	

}
