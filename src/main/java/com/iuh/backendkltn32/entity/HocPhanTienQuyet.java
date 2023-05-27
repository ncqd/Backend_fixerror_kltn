package com.iuh.backendkltn32.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
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
@Table(name = "HocPhanTienQuyet")
public class HocPhanTienQuyet {
	
	@Id
	@Column(name = "maHocPhan")
	private String maHocPhan;
	
	@Column(name = "tenHocPhan")
	private String tenHocPhan;

	

}
