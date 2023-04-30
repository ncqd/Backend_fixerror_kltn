package com.iuh.backendkltn32.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DuyetRequest {
	
	private String ma;
	private Integer trangThai;
	private String maHocKy;
	private String loiNhan;

}
