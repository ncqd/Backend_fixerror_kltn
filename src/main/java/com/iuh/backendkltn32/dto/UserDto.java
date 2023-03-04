package com.iuh.backendkltn32.dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDto<T> {

	private List<RoleDto> roles;
	private T user;
}
