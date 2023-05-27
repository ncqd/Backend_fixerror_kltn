package com.iuh.backendkltn32.entity;

import javax.persistence.Entity;
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

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HocPhanTienQuyet_SinhVien")
public class HocPhanTienQuyet_SinhVien {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "maHocPhan", nullable = false)
	@JsonIgnore
	private HocPhanTienQuyet hocPhanTienQuyet;
	
	@ManyToOne
	@JoinColumn(name = "maSinhVien", nullable = false)
	private SinhVien sinhVien;
	
	private Double diemTrungBinh;

	public HocPhanTienQuyet_SinhVien(HocPhanTienQuyet hocPhanTienQuyet, SinhVien sinhVien, Double diemTrungBinh) {
		super();
		this.hocPhanTienQuyet = hocPhanTienQuyet;
		this.sinhVien = sinhVien;
		this.diemTrungBinh = diemTrungBinh;
	}
	
	

}
