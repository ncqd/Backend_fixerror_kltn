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
@Table(name = "HocPhanTienQuyet_DeTai")
public class HocPhanTienQuyet_DeTai {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "maHocPhan", nullable = false)
	@JsonIgnore
	private HocPhanTienQuyet hocPhanTienQuyet;
	
	@ManyToOne
	@JoinColumn(name = "maDeTai", nullable = false)
	@JsonIgnore
	private DeTai deTai;
	
	private Double diemTrungBinh;

	public HocPhanTienQuyet_DeTai(HocPhanTienQuyet hocPhanTienQuyet, DeTai deTai, Double diemTrungBinh) {
		super();
		this.hocPhanTienQuyet = hocPhanTienQuyet;
		this.deTai = deTai;
		this.diemTrungBinh = diemTrungBinh;
	}
	
	
}
