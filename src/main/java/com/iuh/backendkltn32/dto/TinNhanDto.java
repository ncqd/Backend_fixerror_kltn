package com.iuh.backendkltn32.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TinNhanDto {
	
	private int id;
	private Object nguoiNhan;
	private String noiDung;
	private Integer trangThai;
	private Object nguoiGui;
	private String createdAt;

}
