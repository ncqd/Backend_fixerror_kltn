package com.iuh.backendkltn32.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoiMatKhauRequest {
	
	private String tenTaiKhoan; 
	private String matKhauCu;
	private String matKhauMoi;

}
