package com.iuh.backendkltn32.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.QuanLyBoMon;
import com.iuh.backendkltn32.repository.QuanLyBoMonRepository;
import com.iuh.backendkltn32.service.QuanLyBoMonService;

@Service
public class QuanLyBoMonImpl implements QuanLyBoMonService{
	
	@Autowired
	private QuanLyBoMonRepository quanLyBoMonRepository;

	@Override
	public QuanLyBoMon layTheoMa(String ma) throws Exception {
		if (ma == null || ma.equals("")) {
			throw new RuntimeException("Mã không được phép rỗng");
		}
		System.out.println("QuanLyBoMonservice - layTheoMa: " + ma);
		QuanLyBoMon quanLyBoMon =  quanLyBoMonRepository.findById(ma).orElse(null);
		
		return quanLyBoMon;
	}

	@Override
	public QuanLyBoMon luu(QuanLyBoMon obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String xoa(String ma) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuanLyBoMon capNhat(QuanLyBoMon obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GiangVien layTheoMaGiangVien(String maGiangVien) {
		// TODO Auto-generated method stub
		return quanLyBoMonRepository.getQuanLyTheoMaGiangVien(maGiangVien);
	}

}
