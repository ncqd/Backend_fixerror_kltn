package com.iuh.backendkltn32.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.QuanLyBoMon;
import com.iuh.backendkltn32.repository.QuanLyBoMonRepository;
import com.iuh.backendkltn32.service.QuanLyBoMonService;

@Service
@Transactional
public class QuanLyBoMonImpl implements QuanLyBoMonService{
	
	@Autowired
	private QuanLyBoMonRepository quanLyBoMonRepository;

	@Override
	public QuanLyBoMon layTheoMa(String ma) throws RuntimeException {
		if (ma == null || ma.equals("")) {
			throw new RuntimeException("Mã không được phép rỗng");
		}
		System.out.println("QuanLyBoMonservice - layTheoMa: " + ma);
		QuanLyBoMon quanLyBoMon =  quanLyBoMonRepository.findById(ma).orElse(null);
		
		return quanLyBoMon;
	}

	@Override
	public QuanLyBoMon luu(QuanLyBoMon obj) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String xoa(String ma) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuanLyBoMon capNhat(QuanLyBoMon obj) throws RuntimeException {
		QuanLyBoMon quanLyBoMon = quanLyBoMonRepository.getQuanLyTheoMaGiangVien(obj.getMaGiangVien());
		
		if (quanLyBoMon == null) {
			throw new RuntimeException("Không tồn tại người dùng");
		}
//		quanLyBoMon.setChoXemDiem(obj.getChoXemDiem());
		return quanLyBoMonRepository.save(quanLyBoMon);
	}

	@Override
	public GiangVien layTheoMaGiangVien(String maGiangVien) {
		// TODO Auto-generated method stub
		return quanLyBoMonRepository.getQuanLyTheoMaGiangVien(maGiangVien);
	}

}
