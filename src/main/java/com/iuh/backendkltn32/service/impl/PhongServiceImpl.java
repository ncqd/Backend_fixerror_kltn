package com.iuh.backendkltn32.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.backendkltn32.entity.KeHoach;
import com.iuh.backendkltn32.entity.Phong;
import com.iuh.backendkltn32.repository.PhongRepository;
import com.iuh.backendkltn32.service.PhongService;

@Service
public class PhongServiceImpl implements PhongService{

	@Autowired
	private PhongRepository repository;

	@Override
	public Phong layTheoMa(String ma) throws Exception {
		if (ma == null || ma.equals("")) {
			throw new RuntimeException("Mã không được phép rỗng");
		}
		System.out.println("Khoa service - layTheoMa: " + ma);
		Phong keHoach = repository.findById(Integer.parseInt(ma)).orElse(null);

		return keHoach;
	}

	@Override
	public Phong luu(Phong obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String xoa(String ma) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Phong capNhat(Phong obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Phong> layHetPhong() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

}
