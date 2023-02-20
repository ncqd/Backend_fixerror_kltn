package com.iuh.backendkltn32.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "QuanLyBoMon")
public class QuanLyBoMon extends GiangVien {

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
