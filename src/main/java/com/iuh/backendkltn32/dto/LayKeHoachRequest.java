package com.iuh.backendkltn32.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class LayKeHoachRequest {

	private String maHocKy;
	private String maNguoiDung;
	private String dotChamDiem;
	private String loaiChamHoiDong;
	private String vaiTro;
	
}
