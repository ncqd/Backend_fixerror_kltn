package com.iuh.backendkltn32.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.dto.LapKeHoachDto;
import com.iuh.backendkltn32.dto.LapKeHoachValidateDto;
import com.iuh.backendkltn32.dto.LayKeHoachRequest;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.service.HocKyService;
import com.iuh.backendkltn32.service.KeHoachService;

@RestController
@RequestMapping("/api/ke-hoach")
public class KeHoachController {

	@Autowired
	private KeHoachService keHoachService;
	
	@Autowired
	private HocKyService hocKyService;

	@PostMapping("/lay-ke-hoach")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public List<LapKeHoachDto> layKeHoachTheoMaNDHoacVaiTro(@RequestBody LayKeHoachRequest request) {
		List<LapKeHoachDto> dsKeHoach = new ArrayList<>();
		keHoachService.layKeHoachTheoVaiTro(request.getMaHocKy(), request.getVaiTro()).stream().forEach(kh -> {
			String[] ngayThucHienKL = kh.getDsNgayThucHienKhoaLuan() != null
					? kh.getDsNgayThucHienKhoaLuan().split(",\\s")
					: new String[0];
			LapKeHoachDto lapKeHoachDto = new LapKeHoachDto(kh.getId(), kh.getTenKeHoach(), kh.getChuThich(),
					Arrays.asList(ngayThucHienKL), kh.getHocKy(), new Timestamp(kh.getThoiGianBatDau().getTime()),
					new Timestamp(kh.getThoiGianKetThuc().getTime()), kh.getTinhTrang(), kh.getVaiTro(),
					kh.getMaNguoiDung());
			dsKeHoach.add(lapKeHoachDto);
		});
		if (request.getMaNguoiDung() != null) {
			keHoachService.layKeHoachTheoMaNguoiDung(request.getMaHocKy(), request.getMaNguoiDung()).stream()
					.forEach(kh -> {
						String[] ngayThucHienKL = kh.getDsNgayThucHienKhoaLuan() != null
								? kh.getDsNgayThucHienKhoaLuan().split(",\\s")
								: new String[0];
						LapKeHoachDto lapKeHoachDto = new LapKeHoachDto(kh.getId(), kh.getTenKeHoach(),
								kh.getChuThich(), Arrays.asList(ngayThucHienKL), kh.getHocKy(),
								new Timestamp(kh.getThoiGianBatDau().getTime()),
								new Timestamp(kh.getThoiGianKetThuc().getTime()), kh.getTinhTrang(), kh.getVaiTro(),
								kh.getMaNguoiDung());
						dsKeHoach.add(lapKeHoachDto);
					});
		}

		return dsKeHoach;
	}

	@PostMapping("/lay-ke-hoach-theo-ten")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public List<LapKeHoachValidateDto> layKeHoachTheoTen(@RequestBody LayKeHoachRequest request) {
		List<LapKeHoachValidateDto> dsKeHoach = new ArrayList<>();
		String maHocKy = request.getMaHocKy();
		if (maHocKy == null) {
			
			maHocKy = hocKyService.layHocKyCuoiCungTrongDS().getMaHocKy();
		}
		keHoachService.layKeHoachTheoVaiTro(maHocKy, request.getVaiTro()).stream().forEach(kh -> {
			boolean isValidate = false;
			if (kh.getThoiGianBatDau().getTime() > System.currentTimeMillis()) {
				isValidate = true;
			} 
			if (kh.getThoiGianKetThuc().getTime() < System.currentTimeMillis()) {
				isValidate = true;
			} 
			LapKeHoachValidateDto lapKeHoachValidateDto = new LapKeHoachValidateDto(kh.getTenKeHoach(), isValidate);
			dsKeHoach.add(lapKeHoachValidateDto);
		});
//		if (request.getMaNguoiDung() != null) {
//			keHoachService.layKeHoachTheoMaNguoiDung(request.getMaHocKy(), request.getMaNguoiDung()).stream()
//					.forEach(kh -> {
//						String[] ngayThucHienKL = kh.getDsNgayThucHienKhoaLuan() != null
//								? kh.getDsNgayThucHienKhoaLuan().split(",\\s")
//								: new String[0];
//						LapKeHoachDto lapKeHoachDto = new LapKeHoachDto(kh.getId(), kh.getTenKeHoach(),
//								kh.getChuThich(), Arrays.asList(ngayThucHienKL), kh.getHocKy(),
//								new Timestamp(kh.getThoiGianBatDau().getTime()),
//								new Timestamp(kh.getThoiGianKetThuc().getTime()), kh.getTinhTrang(), kh.getVaiTro(),
//								kh.getMaNguoiDung());
//						dsKeHoach.add(lapKeHoachDto);
//					});
//		}

		return dsKeHoach;
	}

}
