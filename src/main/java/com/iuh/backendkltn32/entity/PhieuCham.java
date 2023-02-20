package com.iuh.backendkltn32.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "PhieuCham")
public class PhieuCham {
	
	@Id
	private String maPhieu;
	
	@Column(name = "tenPhieu", columnDefinition = "nvarchar(255)" ,nullable = false)
	private String tenPhieu;
	
	@Column(name = "diemPhieuCham", nullable = false)
	private Double diemPhieuCham;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "phieuCham")
	private List<KetQua> dsKetQua;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "phieuCham")
	private List<DiemThanhPhan> dsDiemThanhPhan;

}
