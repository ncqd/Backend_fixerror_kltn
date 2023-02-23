package com.iuh.backendkltn32.entity;

import java.util.Date;

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

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "LopHocPhan")
public class LopHocPhan {
	
	@Id
	private String maLopHocPhan;
	
	@Column(name = "tenLopHocPhan", columnDefinition = "nvarchar(255)" ,nullable = false)
	private String tenLopHocPhan;
	
	@Column(name = "thoiGianBatDau")
	private Date thoiGianBatDau;
	
	@Column(name = "thoiGianKetThuc")
	private Date thoiGianKetThuc;
	
	@Column(name = "phong", nullable = false)
	private String phong;
	
	@Column(name = "ghiChu", columnDefinition = "nvarchar(255)" ,nullable = false)
	private String ghiChu;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "maHocPhan", nullable = false)
	private HocPhanKhoaLuanTotNghiep hocPhanKhoaLuanTotNghiep;
	
//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lopHocPhan")
//	private List<SinhVien> dsSinhVien;

}
