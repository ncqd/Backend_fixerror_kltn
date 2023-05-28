package com.iuh.backendkltn32.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeTaiChuaDuocDangKyDto {

	private String maDeTai;
	private String tenDeTai;
	private String doKho;
	private Integer soLuongCon;
	private String gvhd;
}
