package com.iuh.backendkltn32.export;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GiangVienPBDto {
	
	private String maGiangVien;
	private String tenGiangVien;
	@Override
	public String toString() {
		return "SinhVienNhomVaiTroDto [maGiangVien=" + maGiangVien + ", tenGiangnhVien=" + tenGiangVien + "]";
	}

}
