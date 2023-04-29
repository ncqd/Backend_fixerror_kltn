package com.iuh.backendkltn32.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PhieuChamMau")
public class PhieuChamMau {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "maPhieuMau")
	private Integer maPhieuMau;

	@Column(name = "tenPhieuCham", nullable = true)
	private String tenPhieuCham;

	private String tieuChiChamDiems;
	
	private String vaiTroDung;
	
	@Column(name = "maHocKy", nullable = true)
	private String maHocKy;

	public PhieuChamMau(String tenPhieuCham, String tieuChiChamDiems, String vaiTroDung, String maHocKy) {
		super();
		this.tenPhieuCham = tenPhieuCham;
		this.tieuChiChamDiems = tieuChiChamDiems;
		this.vaiTroDung = vaiTroDung;
		this.maHocKy = maHocKy;
	}

	
	

}
