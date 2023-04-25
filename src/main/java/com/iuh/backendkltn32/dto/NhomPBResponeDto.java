package com.iuh.backendkltn32.dto;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NhomPBResponeDto {

	private String maNhom;
	private String tenNhom;
	private String maDeTai;
	private String tenDeTai;
	private Map<String, String> dsMaSinhVien;
	private String tenGiangVienHD;
	private List<String> dsTenGiangVienPB;
	private String maGiangVienHD;
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
		NhomPBResponeDto other = (NhomPBResponeDto) obj;
		return Objects.equals(maNhom, other.maNhom);
	}
	
	
}
