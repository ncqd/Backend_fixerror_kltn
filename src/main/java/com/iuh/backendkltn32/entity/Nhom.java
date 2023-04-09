package com.iuh.backendkltn32.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "Nhom")
public class Nhom implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String maNhom;
	
	@Column(name = "tenNhom", columnDefinition = "nvarchar(255)", nullable = false)
	private String tenNhom;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "maDeTai", nullable = true)
	private DeTai deTai;
	
	@Column(name = "tinhTrang", nullable = false)
	private Integer tinhTrang;
	
	@Column(name = "dkDeTai", nullable = false)
	private Integer tinhTrangDeTai;
	
	@Column(name = "matKhauNhom", nullable = false)
	private String matKhauNhom;
	

}
