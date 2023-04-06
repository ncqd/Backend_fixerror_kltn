package com.iuh.backendkltn32.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.dto.LoginRequest;
import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.service.GiangVienService;

@RestController
@RequestMapping("/api/giang-vien")
public class GiangVienController {

	

	@Autowired
	private GiangVienService giangVienService;

	@GetMapping("/thong-tin-ca-nhan/{maGiangVien}")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN')")
	public GiangVien hienThiThongTinCaNhan(@PathVariable String maGiangVien, @RequestBody LoginRequest loginRequest) {
		try {
			if (!loginRequest.getTenTaiKhoan().equals(maGiangVien)) {
				throw new Exception("Khong Dung Ma Giang Vien");
			}
			GiangVien giangVien = giangVienService.layTheoMa(maGiangVien);

			return giangVien;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
