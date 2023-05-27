package com.iuh.backendkltn32.dto;

import java.util.List;

import com.iuh.backendkltn32.entity.GiangVien;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThemDeTaiRequest {

	private String maDeTai;

	private String tenDeTai;

	private String mucTieuDeTai;

	private String sanPhamDuKien;

	private String moTa;

	private String yeuCauDauVao;

	private int trangThai;

	private Integer gioiHanSoNhomThucHien;

	private String maHocKy;
	
	private List<HocPhanTienQuyetDeTaiRequest> hocPhanTienQuyet;
}
