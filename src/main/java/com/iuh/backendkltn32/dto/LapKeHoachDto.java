package com.iuh.backendkltn32.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.TimeZone;

import com.iuh.backendkltn32.entity.HocKy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class LapKeHoachDto {

	private Integer id;

	private String tenKeHoach;
	
	private String chuThich;

	private List<String> dsNgayThucHienKhoaLuan;

	private HocKy hocKy;
	
	private String maHocKy;

	private Timestamp thoiGianBatDau;

	private Timestamp thoiGianKetThuc;

	private Integer tinhTrang;

	private String vaiTro;

	private String maNguoiDung;
	
	private Integer maLoaiLich;
	private String phong;

	public LapKeHoachDto(Integer id, String tenKeHoach, String chuThich, List<String> dsNgayThucHienKhoaLuan,
			HocKy hocKy, Timestamp thoiGianBatDau, Timestamp thoiGianKetThuc, Integer tinhTrang, String vaiTro,
			String maNguoiDung, Integer maLoaiLich, String phong) {
		super();
		this.id = id;
		this.tenKeHoach = tenKeHoach;
		this.chuThich = chuThich;
		this.dsNgayThucHienKhoaLuan = dsNgayThucHienKhoaLuan;
		this.hocKy = hocKy;
		this.thoiGianBatDau = thoiGianBatDau;
		this.thoiGianKetThuc = thoiGianKetThuc;
		this.tinhTrang = tinhTrang;
		this.vaiTro = vaiTro;
		this.maNguoiDung = maNguoiDung;
		this.maLoaiLich = maLoaiLich;
		this.phong = phong;
	}
	
	

}
