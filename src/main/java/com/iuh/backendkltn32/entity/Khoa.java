package com.iuh.backendkltn32.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Khoa")
public class Khoa {
	
	@Id
	private String maKhoa;
	
	@Column(name = "tenKhoa")
	private String tenKhoa;

}
