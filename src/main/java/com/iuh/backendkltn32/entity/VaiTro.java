package com.iuh.backendkltn32.entity;

import com.iuh.backendkltn32.common.EVaiTro;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "VaiTro")
public class VaiTro {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long maVaiTro;
	
	@Column(name = "tenVaiTro", columnDefinition = "varchar(255)" ,nullable = false)
	@Enumerated(EnumType.STRING)
	private EVaiTro tenVaiTro;
	
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "maQuyen", nullable = false)
//	private List<VaiTro_Quyen> quyen;

}
