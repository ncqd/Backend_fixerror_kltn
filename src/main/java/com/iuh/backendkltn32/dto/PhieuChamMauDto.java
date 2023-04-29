package com.iuh.backendkltn32.dto;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;

import com.iuh.backendkltn32.entity.TieuChiChamDiem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PhieuChamMauDto {

	private Integer maPhieuMau;

	private String tenPhieuCham;

	private List<String> tieuChiChamDiems;
	
	private String vaiTroDung;
	
	private String maHocKy;
	
	public PhieuChamMauDto(String tenPhieuCham, List<String> tieuChiChamDiems, String vaiTroDung, String maHocKy) {
		super();
		this.tenPhieuCham = tenPhieuCham;
		this.tieuChiChamDiems = tieuChiChamDiems;
		this.vaiTroDung = vaiTroDung;
		this.maHocKy = maHocKy;
	}

}
