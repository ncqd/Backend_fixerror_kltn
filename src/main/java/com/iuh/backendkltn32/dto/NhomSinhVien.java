package com.iuh.backendkltn32.dto;

import java.util.List;

import com.iuh.backendkltn32.entity.SinhVien;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NhomSinhVien {
	
	private List<SinhVien> dsSinhVienNhom;
	private Integer status;

}
