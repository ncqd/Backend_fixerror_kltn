package com.iuh.backendkltn32.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhanCongPBDto {

	private Integer maPhanCong;

	private String viTriPhanCong;

	private Boolean chamCong;

	private String maNhom;

	private List<String> dsMaGiangVienPB;
	
	private List<String> dsTenGiangPB;
	
	private Timestamp thoiGianBatDauKeHoach;
	
	private Timestamp thoiGianBatDauKetThuc;
	
	private String tiet;
}
