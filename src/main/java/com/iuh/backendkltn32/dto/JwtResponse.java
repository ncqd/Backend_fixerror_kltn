package com.iuh.backendkltn32.dto;




import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {

	private String jwtToken;
	private UserDto<?> user;
	

}
