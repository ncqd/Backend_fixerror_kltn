package com.iuh.backendkltn32.dto;




import com.iuh.backendkltn32.entity.HocKy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LopHocPhanDto {

	private String maLopHocPhan;
	private String tenLopHocPhan;
	private String phong;
	private String ghiChu;
	private String maHocKy;

}
