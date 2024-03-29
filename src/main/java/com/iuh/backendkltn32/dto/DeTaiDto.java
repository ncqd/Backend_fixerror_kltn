package com.iuh.backendkltn32.dto;

import com.iuh.backendkltn32.entity.DeTai;
import com.iuh.backendkltn32.entity.GiangVien;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeTaiDto {
	
	private DeTai deTai;
	private boolean isFull;
	private GiangVien giangVien;
	

}
