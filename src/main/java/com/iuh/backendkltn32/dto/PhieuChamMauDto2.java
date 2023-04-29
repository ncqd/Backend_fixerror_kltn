package com.iuh.backendkltn32.dto;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;

import com.iuh.backendkltn32.entity.DiemThanhPhan;
import com.iuh.backendkltn32.entity.TieuChiChamDiem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PhieuChamMauDto2 {

	private Integer maPhieuMau;

	private String tenPhieuCham;
	
	private String vaiTroDung;
	
	private List<TieuChiChamDiem> tieuChiChamDiems;
	
	private List<DiemThanhPhan> diemThanhPhans;
	private String maHocKy;

	public PhieuChamMauDto2(String tenPhieuCham, List<TieuChiChamDiem> tieuChiChamDiems, String vaiTroDung) {
		super();
		this.tenPhieuCham = tenPhieuCham;
		this.tieuChiChamDiems = tieuChiChamDiems;
		this.vaiTroDung = vaiTroDung;
	}

}
