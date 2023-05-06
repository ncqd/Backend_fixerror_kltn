package com.iuh.backendkltn32.dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DangKyNhomRequest {
	
	private List<String> dsMaSinhVien;
	private String maNhom;
	private String maDeTai;
	private String vaiTro;
	@Override
	public String toString() {
		return "DangKyNhomRequest [dsMaSinhVien=" + dsMaSinhVien + ", maNhom=" + maNhom + ", maDeTai=" + maDeTai
				+ ", vaiTro=" + vaiTro + "]";
	}
	
	
	
}
