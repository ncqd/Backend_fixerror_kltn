package com.iuh.backendkltn32.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PhieuCham")
public class PhieuCham implements Serializable {
	
	@Id
	private String maPhieu;
	
	@Column(name = "tenPhieu", columnDefinition = "nvarchar(255)" ,nullable = true)
	private String tenPhieu;
	
	@Column(name = "diemPhieuCham", nullable = true)
	private Double diemPhieuCham;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "phieuCham")
	@Column( nullable = true)
	private List<KetQua> dsKetQua;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "phieuCham")
	@Column(nullable = true)
	private List<DiemThanhPhan> dsDiemThanhPhan;
	
	@ManyToOne
	@JoinColumn(name = "maDeTai", nullable = true)
	private DeTai deTai;
	
	@ManyToOne
	@JoinColumn(name = "maGiangVien", nullable = true)
	private GiangVien giangVien;

	public PhieuCham(String tenPhieu, Double diemPhieuCham, List<KetQua> dsKetQua, List<DiemThanhPhan> dsDiemThanhPhan,
			DeTai deTai, GiangVien giangVien) {
		super();
		this.tenPhieu = tenPhieu;
		this.diemPhieuCham = diemPhieuCham;
		this.dsKetQua = dsKetQua;
		this.dsDiemThanhPhan = dsDiemThanhPhan;
		this.deTai = deTai;
		this.giangVien = giangVien;
	}
	
	

}
