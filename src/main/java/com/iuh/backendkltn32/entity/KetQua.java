package com.iuh.backendkltn32.entity;



import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "KetQua")
public class KetQua {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "maSinhVien", nullable = false)
	private SinhVien sinhVien;
	
	@ManyToOne
	@JoinColumn(name = "maPhieu", nullable = false)
	private PhieuCham phieuCham;
	

	private Double diemTongKet;

	public Double getDiemTongKet() {
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
