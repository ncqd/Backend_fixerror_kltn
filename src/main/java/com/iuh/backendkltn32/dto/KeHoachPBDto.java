package com.iuh.backendkltn32.dto;

import java.sql.Timestamp;
import java.util.List;

import com.iuh.backendkltn32.entity.DeTai;
import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.SinhVien;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class KeHoachPBDto {

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
	
	private NhomVaiTro nhomSinhVien;
	
	private List<SinhVienNhomVaiTroDto> nhomSinhVienPB;
	
	private List<GiangVienPBDto> nhomGiangVienPb;

	
	

}
