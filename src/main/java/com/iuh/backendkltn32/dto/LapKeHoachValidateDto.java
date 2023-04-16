package com.iuh.backendkltn32.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class LapKeHoachValidateDto {
	
	private String tenKeHoach;
	private boolean isInvalid;

}
