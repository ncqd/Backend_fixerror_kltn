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

}
