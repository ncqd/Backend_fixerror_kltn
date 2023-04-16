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
import com.iuh.backendkltn32.dto.LayKeHoachRequest;
import com.iuh.backendkltn32.entity.KeHoach;
import com.iuh.backendkltn32.service.KeHoachService;

@RestController
@RequestMapping("/api/ke-hoach")
public class KeHoachController {

	@Autowired
	private KeHoachService keHoachService;

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
}
