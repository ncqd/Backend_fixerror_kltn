package com.iuh.backendkltn32.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "KeHoach")
public class KeHoach {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "tenKeHoach", nullable = false)
	private String tenKeHoach;
	
	private Boolean thuHai;

	private Boolean thuBa;

	private Boolean thuTu;

	private Boolean thuNam;
	

	private Boolean thuSau;

	private Boolean thuBay;
	
	private Boolean chuNhat;
	
	@ManyToOne
	@JoinColumn(name = "maHocKy", nullable = true)
	private HocKy hocKy;
	
	@Column(name = "thoiGianBatDau", nullable = false)
	private Timestamp thoiGianBatDau;
	
	@Column(name = "thoiGianKetThuc", nullable = false)
	private Timestamp thoiGianKetThuc;
	
	@Column(name = "tinhTrang", nullable = false)
	private Integer tinhTrang;
	

}
