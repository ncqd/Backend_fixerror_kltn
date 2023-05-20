package com.iuh.backendkltn32.dto;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.HocKy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeTaiRes {

private String maDeTai;
	
	private String tenDeTai;
	
	private String mucTieuDeTai;
	
	private String sanPhamDuKien;
	
	private String moTa;
	
	private String yeuCauDauVao;
	
	private int trangThai;
	
	private Integer gioiHanSoNhomThucHien;
	
	private GiangVien giangVien;
	
	private String maHocKy;
}
