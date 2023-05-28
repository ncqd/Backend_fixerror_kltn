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
	private Integer doKho;
	private Integer soLuongConLai;
	private String tenGiangVien;
}
