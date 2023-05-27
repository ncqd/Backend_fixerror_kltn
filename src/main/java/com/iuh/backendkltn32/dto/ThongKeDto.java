package com.iuh.backendkltn32.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThongKeDto {

	private List<String> labels;
	private List<DataThongKeDto> dataSets;

}
