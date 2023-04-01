package com.iuh.backendkltn32.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LayDeTaiRquestDto {
	
	private String maGiangVien;
	private String maHocKy;
	private String soHocKy;
	private Integer trangThai;

}
