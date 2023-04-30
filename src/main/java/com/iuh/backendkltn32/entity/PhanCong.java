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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PhanCong")
public class PhanCong {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maPhanCong;
	
	@Column(name = "viTriPhanCong", columnDefinition = "nvarchar(255)", nullable = false)
	private String viTriPhanCong;
	
	@Column(name = "chamCong",nullable = false)
	private Boolean chamCong;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "maNhom", nullable = true)
	private Nhom nhom;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "maGiangVien", nullable = false)
	private GiangVien giangVien;

	public PhanCong(String viTriPhanCong, Boolean chamCong, Nhom nhom, GiangVien giangVien) {
		this.viTriPhanCong = viTriPhanCong;
		this.chamCong = chamCong;
		this.nhom = nhom;
		this.giangVien = giangVien;
	}
	
	

}
