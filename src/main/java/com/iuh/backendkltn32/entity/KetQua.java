package com.iuh.backendkltn32.entity;



import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Entity
@Table(name = "KetQua")
public class KetQua implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "maSinhVien", nullable = false)
	private SinhVien sinhVien;
	
	@ManyToOne
	@JoinColumn(name = "maPhieu", nullable = false)
	@JsonIgnore
	private PhieuCham phieuCham;

	private Double diemTongKet;
	
	public Double getDiemTongKet() {
		this.diemTongKet = (double) 0;
		phieuCham.getDsDiemThanhPhan().stream().forEach(diem -> {
			this.diemTongKet += Double.parseDouble(diem.getDiemThanhPhan());
		});
		return diemTongKet;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SinhVien getSinhVien() {
		return sinhVien;
	}

	public void setSinhVien(SinhVien sinhVien) {
		this.sinhVien = sinhVien;
	}

	public PhieuCham getPhieuCham() {
		return phieuCham;
	}

	public void setPhieuCham(PhieuCham phieuCham) {
		this.phieuCham = phieuCham;
	}
}
