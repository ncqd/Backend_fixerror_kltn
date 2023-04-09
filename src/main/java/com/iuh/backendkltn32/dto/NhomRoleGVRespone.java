package com.iuh.backendkltn32.dto;

import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NhomRoleGVRespone {

	private String maNhom;
	private String maDeTai;
	private List<String> dsMaSinhVien;
	@Override
	public int hashCode() {
		return Objects.hash(maNhom);
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
		return Objects.equals(maNhom, other.maNhom);
	}
	
	
}
