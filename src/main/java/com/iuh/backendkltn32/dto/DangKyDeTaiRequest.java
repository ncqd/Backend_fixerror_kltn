package com.iuh.backendkltn32.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DangKyDeTaiRequest {
	
	private String maDeTai;
	private String maNhom;
	private String vaiTro;

}
