package com.iuh.backendkltn32.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.iuh.backendkltn32.entity.Khoa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GiangVienDto {

	private String maGiangVien;

	private String tenGiangVien;

	private String soDienThoai;

	private String email;
	private String cmnd;

	private String hocVi;

	private Date ngaySinh;

	private Integer namCongTac;

	private String maKhoa;
	private String anhDaiDien;
	private Integer gioiTinh;

}
