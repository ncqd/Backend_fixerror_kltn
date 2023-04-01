package com.iuh.backendkltn32.controller;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.dto.DuyetDeTaiRequest;
import com.iuh.backendkltn32.dto.LayDeTaiRquestDto;
import com.iuh.backendkltn32.dto.LoginRequest;
import com.iuh.backendkltn32.entity.DeTai;
import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.Khoa;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.entity.TaiKhoan;
import com.iuh.backendkltn32.export.SinhVienExcelExporoter;
import com.iuh.backendkltn32.service.DeTaiService;
import com.iuh.backendkltn32.service.GiangVienService;
import com.iuh.backendkltn32.service.KhoaService;
import com.iuh.backendkltn32.service.SinhVienService;
import com.iuh.backendkltn32.service.TaiKhoanService;
import com.iuh.backendkltn32.service.VaiTroService;

@RestController
@RequestMapping("/api/quan-ly")
public class QuanLyBoMonController {


	@Autowired
	private SinhVienService sinhVienService;
	
	@Autowired
	private TaiKhoanService taiKhoanService;
	
	@Autowired
	private VaiTroService vaiTroService;
	
	@Autowired
	private GiangVienService giangVienService;
	
	@Autowired
	private KhoaService khoaService;
	
	@Autowired
	private DeTaiService deTaiService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@GetMapping("/thong-tin-ca-nhan/{maQuanLy}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public GiangVien hienThiThongTinCaNhan(@PathVariable String maQuanLy, @RequestBody LoginRequest loginRequest) {
		try {
			if (!loginRequest.getTenTaiKhoan().equals(maQuanLy)) {
				throw new Exception("Khong Dung Ma Giang Vien");
			}
			GiangVien giangVien = giangVienService.layTheoMa(maQuanLy);

			return giangVien;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping("/them-sinh-vien")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public SinhVien themSinhVienVaoHeThong(@RequestBody SinhVien sinhVien) {
		
		try {
			SinhVien ketQuaLuuSinhVien = sinhVienService.luu(sinhVien);
			
			TaiKhoan taiKhoan = new TaiKhoan(sinhVien.getMaSinhVien(), encoder.encode("1111"), vaiTroService.layTheoMa(4L));
			
			taiKhoanService.luu(taiKhoan);
			return ketQuaLuuSinhVien;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	@PostMapping("/them-giang-vien")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public GiangVien themSGiangVienVaoHeThong(@RequestBody GiangVien giangVien) {
		
		try {
			Khoa khoa = khoaService.layTheoMa(giangVien.getKhoa().getMaKhoa());
			giangVien.setKhoa(khoa);
			GiangVien ketQuaLuuGiangVien = giangVienService.luu(giangVien);
			
			TaiKhoan taiKhoan = new TaiKhoan(giangVien.getMaGiangVien(), encoder.encode("1111"), vaiTroService.layTheoMa(3L));

			
			taiKhoanService.luu(taiKhoan);
			return ketQuaLuuGiangVien;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	@GetMapping("/xuat-ds-sinhvien")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	 public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=danh-sach-sinh-vien_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
         
        List<SinhVien> dsSinhVien = sinhVienService.layTatCaSinhVien();
         
        SinhVienExcelExporoter excelExporter = new SinhVienExcelExporoter(dsSinhVien);
         
        excelExporter.export(response);    
    }  
	
	@GetMapping("/lay-ds-de-tai-theo-nam-hk-chua-duyet")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY')")
	public List<DeTai> layDanhSachDeTaiTheoNamHocKy(@RequestBody LayDeTaiRquestDto layDeTaiRquestDto) {
		try {
			List<DeTai> dsDeTai = deTaiService.layDsDeTaiTheoNamHocKyChuaDuyet(layDeTaiRquestDto.getMaHocKy(), 
					layDeTaiRquestDto.getSoHocKy(), layDeTaiRquestDto.getMaGiangVien());

			return dsDeTai;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	@PostMapping("/lay-ds-de-tai-theo-nam-hk-chua-duyet")
	@PreAuthorize("hasAuthority('ROLE_GIANGVIEN') or hasAuthority('ROLE_QUANLY')")
	public DeTai pheDuyetDeTai(@RequestBody DuyetDeTaiRequest duyetDeTaiRequest) {
		try {
			DeTai deTai = deTaiService.layTheoMa(duyetDeTaiRequest.getMaDeTai());
			deTai.setTrangThai(duyetDeTaiRequest.getTrangThai());
			deTaiService.capNhat(deTai);
			return deTai;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	
}
