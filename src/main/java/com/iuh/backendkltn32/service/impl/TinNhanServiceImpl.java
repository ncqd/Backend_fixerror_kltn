package com.iuh.backendkltn32.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.backendkltn32.entity.TinNhan;
import com.iuh.backendkltn32.repository.TinNhanRepository;
import com.iuh.backendkltn32.service.TinNhanSerivce;

@Service
public class TinNhanServiceImpl implements TinNhanSerivce {

	@Autowired
	private TinNhanRepository repository;

	@Override
	public TinNhan layTheoMa(String ma) throws RuntimeException {
		if (ma == null || ma.equals("")) {
			throw new RuntimeException("Mã không được phép rỗng");
		}
		System.out.println("Khoa service - layTheoMa: " + ma);
		TinNhan tinNhan = repository.findById(Integer.parseInt(ma)).orElse(null);

		return tinNhan;
	}

	@Override
	public TinNhan luu(TinNhan obj) throws Exception {
		return repository.save(obj);
	}

	@Override
	public String xoa(String ma) throws Exception {
		TinNhan khKhongTonTai = layTheoMa(ma);

		if (khKhongTonTai == null) {
			throw new RuntimeException("Kế hoạch không tồn tại");
		}
		repository.deleteById(Integer.parseInt(ma));

		return "Xóa thành công";
	}

	@Override
	public TinNhan capNhat(TinNhan obj) throws Exception {
		TinNhan khKhongTonTai = layTheoMa(obj.getId().toString());

		if (khKhongTonTai == null) {
			throw new RuntimeException("Kế hoạch không tồn tại");
		}
		khKhongTonTai.setMaNguoiGui(obj.getMaNguoiGui());
		khKhongTonTai.setMaNGuoiNhan(obj.getMaNGuoiNhan());
		khKhongTonTai.setNoiDung(obj.getNoiDung());
		khKhongTonTai.setTrangThai(obj.getTrangThai());

		return repository.save(khKhongTonTai);
	}

	@Override
	public List<TinNhan> layTinNhanTheoMaNguoiNhan(String maNguoiNhan) {
		// TODO Auto-generated method stub
		return repository.layTinNhanTheoMaNguoiNhan(maNguoiNhan);
	}

	@Override
	public List<TinNhan> layTinNhanTheoMaNguoiNhanCuaXacNhan(String maNguoiNhan) {
		// TODO Auto-generated method stub
		return repository.layTinNhanTheoMaNguoiNhanChuaXacNhan(maNguoiNhan);
	}

}
