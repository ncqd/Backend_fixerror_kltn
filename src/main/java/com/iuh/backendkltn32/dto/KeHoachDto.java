package com.iuh.backendkltn32.dto;

import java.sql.Timestamp;
import java.util.List;

import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.Phong;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class KeHoachDto {
	
	private String maLich;
	private String tenLich;
	private String tiet;
	private String ngay;
	private String phong;
	private List<GiangVien> dsGiangVienPB;

}
