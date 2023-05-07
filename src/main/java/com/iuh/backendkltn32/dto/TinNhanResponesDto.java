package com.iuh.backendkltn32.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TinNhanResponesDto {
	
	private List<TinNhanDto> tinNhanDtos;
	private Integer thongBaoChuaDoc;

}
