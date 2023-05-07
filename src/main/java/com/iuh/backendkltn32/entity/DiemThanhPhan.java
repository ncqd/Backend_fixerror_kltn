package com.iuh.backendkltn32.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DiemThanhPhan")
public class DiemThanhPhan implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "maPhieu", nullable = false)
	@JsonIgnore
	private PhieuCham phieuCham;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "maChuanDauRa", nullable = false)
	private TieuChiChamDiem tieuChiChamDiem;
	
	@Column(name = "diemThanhPhan", nullable = true)
	private String diemThanhPhan;
	
	@Column(name = "yKien", columnDefinition = "nvarchar(500)", nullable = true)
	private String yKien;

	public DiemThanhPhan(PhieuCham phieuCham, TieuChiChamDiem tieuChiChamDiem, String diemThanhPhan, String yKien) {
		super();
		this.phieuCham = phieuCham;
		this.tieuChiChamDiem = tieuChiChamDiem;
		this.diemThanhPhan = diemThanhPhan;
		this.yKien = yKien;
	}
	
	
}
