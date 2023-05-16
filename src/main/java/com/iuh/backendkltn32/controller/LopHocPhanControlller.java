package com.iuh.backendkltn32.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.dto.KeHoachGvDto;
import com.iuh.backendkltn32.dto.LopHocPhanDto;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.LopHocPhan;
import com.iuh.backendkltn32.entity.Phong;
import com.iuh.backendkltn32.service.HocKyService;
import com.iuh.backendkltn32.service.HocPhanKhoaLuanTotNghiepService;
import com.iuh.backendkltn32.service.LopHocPhanService;
import com.iuh.backendkltn32.service.PhongService;
import com.iuh.backendkltn32.service.SinhVienService;

@RestController
@RequestMapping("/api/lop-hoc-phan")
public class LopHocPhanControlller {

	@Autowired
	private LopHocPhanService lopHocPhanService;

	@Autowired
	private HocKyService hocKyService;

	@Autowired
	private HocPhanKhoaLuanTotNghiepService hocPhanKhoaLuanTotNghiepService;
	
	@Autowired
	private PhongService phongService;

	@PostMapping("/them")
	public LopHocPhan taoLopHocPhan(@RequestBody LopHocPhanDto lopHocPhanDto) throws RuntimeException {

		HocKy hocKy = hocKyService.layTheoMa(lopHocPhanDto.getMaHocKy());
		LopHocPhan lopHocPhan = new LopHocPhan("4232000" + hocKy.getMaHocKy(), lopHocPhanDto.getTenLopHocPhan(),
				hocKy.getThoiGianBatDau(), hocKy.getThoiGianKetThuc(), lopHocPhanDto.getPhong(),
				lopHocPhanDto.getGhiChu(), hocPhanKhoaLuanTotNghiepService.layHocPhanTheoMaHocKy(hocKy.getMaHocKy()));
		return lopHocPhanService.luu(lopHocPhan);
	}

	@PostMapping("/sua")
	public LopHocPhan suaLopHocPhan(@RequestBody LopHocPhanDto lopHocPhanDto) throws RuntimeException {

		HocKy hocKy = hocKyService.layTheoMa(lopHocPhanDto.getMaHocKy());
		LopHocPhan lopHocPhan = new LopHocPhan(lopHocPhanDto.getMaLopHocPhan(), lopHocPhanDto.getTenLopHocPhan(),
				hocKy.getThoiGianBatDau(), hocKy.getThoiGianKetThuc(), lopHocPhanDto.getPhong(),
				lopHocPhanDto.getGhiChu(), hocPhanKhoaLuanTotNghiepService.layHocPhanTheoMaHocKy(hocKy.getMaHocKy()));

		return lopHocPhanService.capNhat(lopHocPhan);
	}

	@PostMapping("/xoa")
	public String xoaLopHocPhan(@RequestBody LopHocPhanDto lopHocPhanDto) throws RuntimeException {

		return lopHocPhanService.xoa(lopHocPhanDto.getMaLopHocPhan());
	}

	@GetMapping("/lay-ds")
	public List<LopHocPhan> layDs() {

		return lopHocPhanService.getAll();
	}
	
	@GetMapping("/lay-phong")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public List<Phong> layPhong() {
		
		return phongService.layHetPhong();
	}

}
