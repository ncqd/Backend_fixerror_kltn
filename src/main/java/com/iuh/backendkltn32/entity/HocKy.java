package com.iuh.backendkltn32.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HocKy")
public class HocKy {
	
	@Id
	private String maHocKy;
	
	private String soHocKy;

	@Override
	public String toString() {
		return "HocKy [maHocKy=" + maHocKy + ", soHocKy=" + soHocKy + "]";
	}
	



}
