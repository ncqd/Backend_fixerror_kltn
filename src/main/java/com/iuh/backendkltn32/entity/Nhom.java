package com.iuh.backendkltn32.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Nhom")
public class Nhom {
	
	@Id
	private String maNhom;
	
	@Column(name = "tenNhom", columnDefinition = "nvarchar(255)", nullable = false)
	private String tenNhom;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "maDeTai", nullable = true)
	private DeTai deTai;
	
	@JoinColumn(name = "tinhTrang", nullable = false)
	private Integer tinhTrang;
	

}
