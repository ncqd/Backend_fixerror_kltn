package com.iuh.backendkltn32.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iuh.backendkltn32.entity.KetQua;
import com.iuh.backendkltn32.entity.LopDanhNghia;
import com.iuh.backendkltn32.entity.LopHocPhan;
import com.iuh.backendkltn32.entity.Nhom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SinhVienDto {
	
	private String maSinhVien;
	
	private String tenSinhVien;
	
	private String noiSinh;
	
	private String dienThoai;
	
	private String email;
	
	private Date ngaySinh;
	
	private Integer namNhapHoc;
	
	private Integer gioiTinh;
	
	private String anhDaiDien;
	
	private String maLopDanhNghia;
	
	private String maLopHocPhan;
	
}
