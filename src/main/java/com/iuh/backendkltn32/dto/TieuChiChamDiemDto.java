package com.iuh.backendkltn32.dto;

import java.util.List;


import com.iuh.backendkltn32.entity.DiemThanhPhan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TieuChiChamDiemDto {

	private Integer maChuanDauRa;
	
	private String tenChuanDauRa;
	
	private Double diemToiDa;
	
	private List<DiemThanhPhan> dsDiemThanhPhan;
}
