package com.iuh.backendkltn32.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DiemThanhPhanDto {
	
	private String maTieuChiCham;

	private String tenTieuChi;
	private String maPhieuCham;
	private String diem;

	public DiemThanhPhanDto(String maTieuChiCham, String maPhieuCham, String diem) {
		this.maTieuChiCham = maTieuChiCham;
		this.maPhieuCham = maPhieuCham;
		this.diem = diem;
	}
}
