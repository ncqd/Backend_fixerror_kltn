package com.iuh.backendkltn32.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class QuanLyBoMon extends GiangVien implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String maQuanLyBoMon;

	public QuanLyBoMon() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LopHocPhan moLopHocPhan(LopHocPhan lopHocPhan) {

		return lopHocPhan;

	}

	public GiangVien phanGiangVien(GiangVien giangVien) {

		return giangVien;

	}

	public DeTai duyetDeTaiDaDangKy(DeTai deTai) {

		return deTai;

	}

	public List<SinhVien> themDanhSachSinhVien(List<SinhVien> dsSinhVien) {

		return dsSinhVien;

	}

}
