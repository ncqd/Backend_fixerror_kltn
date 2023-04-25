package com.iuh.backendkltn32.dto;

import java.util.List;
import java.util.Objects;

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
public class NhomRoleGVRespone {

	private Nhom nhom;
	private String maDeTai;
	private List<String> dsMaSinhVien;
	private List<SinhVien> sinhViens;
	@Override
	public int hashCode() {
		return Objects.hash(nhom.getMaNhom());
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NhomRoleGVRespone other = (NhomRoleGVRespone) obj;
		return Objects.equals(nhom.getMaNhom(), other.nhom.getMaNhom());
	}
	
	
}
