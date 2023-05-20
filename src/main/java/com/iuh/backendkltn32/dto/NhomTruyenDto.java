package com.iuh.backendkltn32.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class NhomTruyenDto {
	
	private String maHocKy;
	private String maNguoiDung;
	private String dotCham;
	private List<String> ppcham;
	private List<String> vaitro;
	private String maNhom;

}
