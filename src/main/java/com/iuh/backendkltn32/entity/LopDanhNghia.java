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
@Table(name = "LopDanhNghia")
public class LopDanhNghia {
	
	@Id
	private String maLopDanhNghia;
	
	@Column(name = "namHoc", nullable = false)
	private String namHoc;
	
	@Column(name = "HocKy", nullable = false)
	private Integer HocKy;
	
	@Column(name = "moTa", columnDefinition = "nvarchar(255)" ,nullable = false)
	private String moTa;
	
	@Column(name = "gioiHanSinhVien", nullable = false)
	private Integer gioiHanSinhVien;
	
}
