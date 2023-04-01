package com.iuh.backendkltn32.entity;

import java.util.List;

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
@Table(name = "quyen")
public class Quyen {
	
	@Id
	private Long maQuyen;
	
	@Column(name = "ten_quyen")
	private String tenQuyen;
	
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "maQuyen", nullable = false)
//	private List<VaiTro_Quyen> vaiTro;

}
