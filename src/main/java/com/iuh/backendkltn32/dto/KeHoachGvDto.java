package com.iuh.backendkltn32.dto;

import java.sql.Timestamp;
import java.util.List;

import com.iuh.backendkltn32.entity.HocKy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class KeHoachGvDto {
	
	private String tiet;
	private Timestamp ngay;
	private String phong;
	private List<String> dsMaGiangVienPB;
	private String maHocKy;

}
