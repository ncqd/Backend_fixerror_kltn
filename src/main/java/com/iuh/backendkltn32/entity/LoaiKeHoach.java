package com.iuh.backendkltn32.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "LoaiKeHoach")
public class LoaiKeHoach {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "tenKeHoach", nullable = false)
	private String tenLoai;
	
	@Column(name = "chuThich", nullable = true)
	private String chuThich;

	public LoaiKeHoach(Integer id) {
		super();
		this.id = id;
	}
	
	
	
}
