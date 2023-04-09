package com.iuh.backendkltn32.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.dto.DangKyNhomRequest;
import com.iuh.backendkltn32.dto.LayDeTaiRquestDto;
import com.iuh.backendkltn32.dto.NhomRoleGVRespone;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.jms.JmsListenerConsumer;
import com.iuh.backendkltn32.jms.JmsPublishProducer;
import com.iuh.backendkltn32.service.HocKyService;
import com.iuh.backendkltn32.service.NhomService;
import com.iuh.backendkltn32.service.SinhVienService;

@RestController
@RequestMapping("/api/nhom")
public class NhomController {
	
	@Autowired
	private JmsPublishProducer producer;
	
	@Autowired
	private JmsListenerConsumer listenerConsumer;
	
	@Autowired
	private NhomService nhomService;
	
	@Autowired
	private SinhVienService sinhVienService;
	
	@Autowired
	private HocKyService hocKyService;

	/*
	 * // case1: tao moi -> check: co nhom chua ???? // TH1: -> chua co //TH2: -> co
	 * roi -> XONG XONG XONG
	 */
	@PostMapping("/dang-ky-nhom")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_SINHVIEN')")
	public ResponseEntity<?> dangKyNhom(@RequestBody DangKyNhomRequest request) throws Exception {	
		System.out.println("NhomController - dang ky nhom - " + request);
		producer.sendMessageOnNhomChanel(request);
		return listenerConsumer.listenerNhomChannel();
	}
	
	@PostMapping("/lay-ds-nhom")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_SINHVIEN')")
	public List<NhomRoleGVRespone> layNhomTheoMaGv(@RequestBody LayDeTaiRquestDto request) throws Exception {	
		List<Nhom> nhoms = nhomService.layDSNhomTheMaGiangVien(request.getMaHocKy(), request.getSoHocKy(), request.getMaGiangVien());
		List<NhomRoleGVRespone> respones = new ArrayList<>();
		if(!nhoms.isEmpty() && nhoms != null) {
			nhoms.stream()
			.forEach(nhom -> {
				List<String> sinhViens = sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom());
				NhomRoleGVRespone nhomRoleGVRespone = new NhomRoleGVRespone(nhom.getMaNhom(), nhom.getDeTai().getMaDeTai(), sinhViens);
				respones.add(nhomRoleGVRespone);
			});
			
		}
		return respones;
	}
	
	
	@GetMapping("/lay-ds-nhom/{tinhTrang}")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_SINHVIEN')")
	public Set<NhomRoleGVRespone> layNhomTheoTinhTrang(@PathVariable("tinhTrang") Integer tinhTrang) throws Exception {	
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		List<Nhom> nhoms = nhomService.layTatCaNhomTheoTinhTrang(hocKy.getMaHocKy(), hocKy.getSoHocKy(),tinhTrang);
		Set<NhomRoleGVRespone> respones = new HashSet<>();
		if(!nhoms.isEmpty() && nhoms != null) {
			nhoms.stream()
			.forEach(nhom -> {
				List<String> sinhViens = sinhVienService.layTatCaSinhVienTheoNhom(nhom.getMaNhom());
				NhomRoleGVRespone nhomRoleGVRespone = new NhomRoleGVRespone(nhom.getMaNhom(), null, sinhViens);
				if(!respones.contains(nhomRoleGVRespone)) {
					
					respones.add(nhomRoleGVRespone);
				}
			});
			
		}
		return respones;
	}
	
}
