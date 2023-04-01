package com.iuh.backendkltn32.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "vaitro_quyen")
public class VaiTro_Quyen implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vaitro")
	private Long maQuyen;
	
//	@Id
//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "quyen")
//	private Long maVaiTro;

}
