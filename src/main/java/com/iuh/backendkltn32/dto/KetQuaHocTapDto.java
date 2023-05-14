package com.iuh.backendkltn32.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KetQuaHocTapDto {
	
	private String maLopHocPhan;
	private String tenMonHoc;
	private String soTinChi;
	private Double diemTongKet;
	private Double thangDiem4;

}
