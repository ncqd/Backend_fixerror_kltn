package com.iuh.backendkltn32.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TieuChiChamDiem")
public class TieuChiChamDiem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "maChuanDauRa")
	private Integer maChuanDauRa;

	@Column(name = "tenChuanDauRa", columnDefinition = "nvarchar(255)", nullable = false)
	private String tenChuanDauRa;

	@Column(name = "diemToiDa", nullable = false)
	private Double diemToiDa;

	@ManyToOne
	@JoinColumn(name = "maPhieuMau", nullable = false)
	@JsonIgnore
	private PhieuChamMau phieuChamMau;

	public TieuChiChamDiem(String tenChuanDauRa, Double diemToiDa) {
		super();
		this.tenChuanDauRa = tenChuanDauRa;
		this.diemToiDa = diemToiDa;
	}

}
