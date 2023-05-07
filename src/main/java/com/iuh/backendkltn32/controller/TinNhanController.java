package com.iuh.backendkltn32.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.dto.TinNhanDto;
import com.iuh.backendkltn32.dto.TinNhanResponesDto;
import com.iuh.backendkltn32.entity.TinNhan;
import com.iuh.backendkltn32.service.GiangVienService;
import com.iuh.backendkltn32.service.SinhVienService;
import com.iuh.backendkltn32.service.TinNhanSerivce;

@RestController
@RequestMapping("/api/tin-nhan")
public class TinNhanController {

	@Autowired
	private TinNhanSerivce tinNhanSerivce;

	@Autowired
	private GiangVienService giangVienService;

	@Autowired
	private SinhVienService sinhVienService;

	@GetMapping("/lay-tin-nhan/{maNguoiNhan}")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public TinNhanResponesDto layTinNhanTheoMaNguoiNhan(@PathVariable("maNguoiNhan") String maNguoiNhan) throws Exception {
		List<TinNhanDto> tinNhans = new ArrayList<>();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Integer thongBaoChuaDoc = 0;
		for (TinNhan tn : tinNhanSerivce.layTinNhanTheoMaNguoiNhan(maNguoiNhan)) {
			if (tn.getMaNGuoiNhan().startsWith("11")) {
				tinNhans.add(new TinNhanDto(tn.getId(), giangVienService.layTheoMa(tn.getMaNguoiGui()), tn.getNoiDung(),
						tn.getTrangThai(), giangVienService.layTheoMa("12392401"), format.format(tn.getCreatedAt())));

			} else {
				tinNhans.add(new TinNhanDto(tn.getId(), sinhVienService.layTheoMa(tn.getMaNGuoiNhan()), tn.getNoiDung(),
						tn.getTrangThai(), sinhVienService.layTheoMa(tn.getMaNguoiGui()),
						format.format(tn.getCreatedAt())));
			}
			thongBaoChuaDoc = tn.getTrangThai() == 0 ? thongBaoChuaDoc + 1 : thongBaoChuaDoc;
		}

		return new TinNhanResponesDto(tinNhans, thongBaoChuaDoc);
	}

	@PutMapping("/da-doc-tin-nhan")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public TinNhanDto layTinNhanTheoMaNguoiNhan(@RequestBody TinNhanDto tinNhanDto) {

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			TinNhan tin = tinNhanSerivce.layTheoMa(tinNhanDto.getId() + "");
			tin.setTrangThai(tinNhanDto.getTrangThai());
			tin = tinNhanSerivce.capNhat(tin);
			if (tin.getMaNguoiGui().startsWith("11")) {
				return new TinNhanDto(tin.getId(), null, tin.getNoiDung(), tin.getTrangThai(),
						giangVienService.layTheoMa(tin.getMaNguoiGui()), format.format(tin.getCreatedAt()));
			}
			return new TinNhanDto(tin.getId(), null, tin.getNoiDung(), tin.getTrangThai(),
					sinhVienService.layTheoMa(tin.getMaNguoiGui()), format.format(tin.getCreatedAt()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tinNhanDto;

	}

}
