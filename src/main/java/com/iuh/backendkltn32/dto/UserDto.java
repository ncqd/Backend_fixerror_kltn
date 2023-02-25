package com.iuh.backendkltn32.dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {

	private List<RoleDto> roles;
	private String fullName;
	private String userName;
	private String password;
}
