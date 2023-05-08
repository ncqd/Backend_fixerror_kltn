package com.iuh.backendkltn32.dto;

import java.util.List;

import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.SinhVien;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NhomVaiTro {
	
	private String maNhom;
	private String tenNhom;
	private String maDeTai;
	private String tenDeTai;
	private List<SinhVienNhomVaiTroDto> sinhVien;
	private String vaiTro;
	
	
	
	@Override
	public String toString() {
		return "NhomVaiTro [maNhom=" + maNhom + ", tenNhom=" + tenNhom + ", maDeTai=" + maDeTai + ", tenDeTai="
				+ tenDeTai + ", sinhVien=" + sinhVien + "]";
	}



	public NhomVaiTro(String maNhom, String tenNhom, String maDeTai, String tenDeTai,
			List<SinhVienNhomVaiTroDto> sinhVien) {
		super();
		this.maNhom = maNhom;
		this.tenNhom = tenNhom;
		this.maDeTai = maDeTai;
		this.tenDeTai = tenDeTai;
		this.sinhVien = sinhVien;
	}
	
	

}
