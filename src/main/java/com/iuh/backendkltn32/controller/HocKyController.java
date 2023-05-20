package com.iuh.backendkltn32.controller;

import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.dto.ChoXemDiemDto;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.HocPhanKhoaLuanTotNghiep;
import com.iuh.backendkltn32.entity.KeHoach;
import com.iuh.backendkltn32.entity.LoaiKeHoach;
import com.iuh.backendkltn32.service.HocKyService;
import com.iuh.backendkltn32.service.HocPhanKhoaLuanTotNghiepService;
import com.iuh.backendkltn32.service.KeHoachService;

@RestController
@RequestMapping("/api/hoc-ky")
@Transactional
public class HocKyController {
	
	@Autowired
	private HocKyService hocKyService;
	
	@Autowired
	private HocPhanKhoaLuanTotNghiepService hocPhanKhoaLuanTotNghiepService;
	
	@Autowired
	private KeHoachService keHoachService;

	@GetMapping("/lay-nam-hoc-ky")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public List<HocKy> layHocKy(){
		return hocKyService.layTatCaHocKy();
	}
	
	@GetMapping("/lay-hoc-ky-moi-nhat")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY') or hasAuthority('ROLE_SINHVIEN')")
	public HocKy layHocKyCuoiCung(){
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		
		return hocKy;
	}
	
	@PostMapping("/them")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public HocKy taoHocKy(@RequestBody HocKy hocKy) throws RuntimeException {
		HocKy hocKyCuoiCung = hocKyService.layHocKyCuoiCungTrongDS();
		String maHK = "";
		if (hocKyCuoiCung == null) {
			maHK = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2);
			System.out.println(maHK + "this is ma HK Thoi Gian Thuc ");
		}
		else {
			
			maHK = hocKyCuoiCung.getSoHocKy().equals("2") ?  (Long.parseLong(hocKyCuoiCung.getMaHocKy().substring(0, 2)) + 1) + "" :
				Long.parseLong(hocKyCuoiCung.getMaHocKy().substring(0, 2)) + "";
			System.out.println(maHK + "this is ma HK ");
		}
		
		String soHocKy = hocKy.getThoiGianBatDau().getMonth()+1 >= 8 && hocKy.getThoiGianBatDau().getMonth()+1 < 12 ? "1" : "2";
		hocKy.setMaHocKy(maHK + soHocKy);
		hocKy.setSoHocKy(soHocKy);
		

		hocKy = hocKyService.luu(hocKy);
		
		hocPhanKhoaLuanTotNghiepService.luu(new HocPhanKhoaLuanTotNghiep("42000000" +maHK+soHocKy , "Học Phần Khóa Luận Tốt Nghiệp", "5", true, hocKy));
		
		keHoachService.luu(new KeHoach("Lịch thêm đề tài", "lịch dùng để cho giảng viên thêm, xóa, sửa đề tài của họ",
				null, hocKy, hocKy.getThoiGianBatDau(), hocKy.getThoiGianKetThuc(), 1, 
				"ROLE_GIANGVIEN", null, new LoaiKeHoach(5)));
		keHoachService.luu(new KeHoach("Lịch chấm phản biện", "lịch biết giảng viên sẽ phản biện ngày nào và thời gian nhập điểm",
				null, hocKy, hocKy.getThoiGianBatDau(), hocKy.getThoiGianKetThuc(), 1, 
				"ROLE_GIANGVIEN", null, new LoaiKeHoach(5)));
		keHoachService.luu(new KeHoach("Lịch chấm hội đồng", "lịch biết giảng viên sẽ phản biện ngày nào và thời gian nhập điểm",
				null, hocKy, hocKy.getThoiGianBatDau(), hocKy.getThoiGianKetThuc(), 1, 
				"ROLE_GIANGVIEN", null, new LoaiKeHoach(5)));
		keHoachService.luu(new KeHoach("Lịch gán đề tài cho nhóm", "lịch dùng để cho giảng viên gán đề tài cho nhóm sinh viên họ muốn hướng dẫn",
				null, hocKy, hocKy.getThoiGianBatDau(), hocKy.getThoiGianKetThuc(), 1, 
				"ROLE_GIANGVIEN", null, new LoaiKeHoach(5)));
		keHoachService.luu(new KeHoach("Lịch đăng ký đề tài", "lịch sinh viên đăng ký đề tài",
				null, hocKy, hocKy.getThoiGianBatDau(), hocKy.getThoiGianKetThuc(), 1, 
				"ROLE_SINHVIEN", null, new LoaiKeHoach(5)));
		keHoachService.luu(new KeHoach("Lịch đăng ký nhóm", "lịch cho sinh viên đăng ký nhhóm",
				null, hocKy, hocKy.getThoiGianBatDau(), hocKy.getThoiGianKetThuc(), 1, 
				"ROLE_SINHVIEN", null, new LoaiKeHoach(5)));
		keHoachService.luu(new KeHoach("Lịch thêm đề tài", "lịch dùng để cho giảng viên thêm, xóa, sửa đề tài của họ",
				null, hocKy, hocKy.getThoiGianBatDau(), hocKy.getThoiGianKetThuc(), 1, 
				"ROLE_QUANLY", null, new LoaiKeHoach(5)));

		return hocKy;
	}
	
	@PostMapping("/cho-xem-diem")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public ResponseEntity<?> choXemDiem(@RequestBody ChoXemDiemDto choXemDiemDto) throws RuntimeException {
		if (choXemDiemDto.getMaHocKy() == null) {
			throw new RuntimeException("Mã Học Kỳ Sai");
		}
		HocKy hocKy = hocKyService.layTheoMa(choXemDiemDto.getMaHocKy());
		hocKy.setChoXemDiem(choXemDiemDto.getChoXemDiem());
		hocKyService.capNhat(hocKy);
		
		return ResponseEntity.ok("Cập nhật thành công");
	}
}
