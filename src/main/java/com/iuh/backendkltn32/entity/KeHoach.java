package com.iuh.backendkltn32.entity;

import java.sql.Timestamp;

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

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "KeHoach")
public class KeHoach {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "tenKeHoach", nullable = false)
	private String tenKeHoach;
	
	@Column(name = "chuThich", nullable = true)
	private String chuThich;
	
	@Column(name = "dsNgayThucHienKhoaLuan", nullable = true)
	private String dsNgayThucHienKhoaLuan;
	
	@ManyToOne
	@JoinColumn(name = "maHocKy", nullable = true)
	private HocKy hocKy;
	
	@Column(name = "thoiGianBatDau", nullable = false)
	private Timestamp thoiGianBatDau;
	
	@Column(name = "thoiGianKetThuc", nullable = false)
	private Timestamp thoiGianKetThuc;
	
	@Column(name = "tinhTrang", nullable = false)
	private Integer tinhTrang;
	
	@Column(name = "vaiTro", nullable = true)
	private String vaiTro;
	
	@Column(name = "maNguoiDung", nullable = true)
	private String maNguoiDung;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "maLoai", nullable = true)
	private LoaiKeHoach loaiKeHoach;
	
	@Column(name = "phong", nullable = true)
	private String phong;

	public KeHoach(String tenKeHoach, String chuThich, String dsNgayThucHienKhoaLuan, HocKy hocKy,
			Timestamp thoiGianBatDau, Timestamp thoiGianKetThuc, Integer tinhTrang, String vaiTro, String maNguoiDung, LoaiKeHoach loaiKeHoach) {
		super();
		this.tenKeHoach = tenKeHoach;
		this.chuThich = chuThich;
		this.dsNgayThucHienKhoaLuan = dsNgayThucHienKhoaLuan;
		this.hocKy = hocKy;
		this.thoiGianBatDau = thoiGianBatDau;
		this.thoiGianKetThuc = thoiGianKetThuc;
		this.tinhTrang = tinhTrang;
		this.vaiTro = vaiTro;
		this.maNguoiDung = maNguoiDung;
		this.loaiKeHoach = loaiKeHoach;
	}
	
	public KeHoach(String tenKeHoach, String chuThich, String dsNgayThucHienKhoaLuan, HocKy hocKy,
			Timestamp thoiGianBatDau, Timestamp thoiGianKetThuc, Integer tinhTrang, String vaiTro, String maNguoiDung, LoaiKeHoach loaiKeHoach, String phong) {
		super();
		this.tenKeHoach = tenKeHoach;
		this.chuThich = chuThich;
		this.dsNgayThucHienKhoaLuan = dsNgayThucHienKhoaLuan;
		this.hocKy = hocKy;
		this.thoiGianBatDau = thoiGianBatDau;
		this.thoiGianKetThuc = thoiGianKetThuc;
		this.tinhTrang = tinhTrang;
		this.vaiTro = vaiTro;
		this.maNguoiDung = maNguoiDung;
		this.loaiKeHoach = loaiKeHoach;
		this.phong= phong;
	}
	
}
