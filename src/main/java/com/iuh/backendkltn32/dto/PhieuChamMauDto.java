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

	public PhieuChamMauDto(String tenPhieuCham, List<String> tieuChiChamDiems, String vaiTroDung) {
		super();
		this.tenPhieuCham = tenPhieuCham;
		this.tieuChiChamDiems = tieuChiChamDiems;
		this.vaiTroDung = vaiTroDung;
	}

}
