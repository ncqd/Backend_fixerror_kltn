package com.iuh.backendkltn32.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DangKyNhomRequest {
	
	private String maSinhVien1;
	private String maSinhVien2;
	private Integer hocKy;
	private String namHoc;

}
