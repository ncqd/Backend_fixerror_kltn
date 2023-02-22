package com.iuh.backendkltn32.entity;

import java.util.Date;


import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "GiangVien")
public class GiangVien {
	
	@Id
	private String maGiangVien;
	
	@Column(name = "tenGiangVien", columnDefinition = "nvarchar(255)" ,nullable = false)
	private String tenGiangVien;
	
	@Column(name = "soDienThoai", columnDefinition = "nvarchar(255)" ,nullable = false)
	private String soDienThoai;
	
	@Column(name = "email", columnDefinition = "nvarchar(255)" ,nullable = false)
	private String email;
	
	@Column(name = "cmnd", columnDefinition = "nvarchar(255)" ,nullable = false)
	private String cmnd;
	
	@Column(name = "hocVi", columnDefinition = "nvarchar(255)" ,nullable = false)
	private String hocVi;
	
	@Column(name = "ngaySinh", nullable = true)
	private Date ngaySinh;
	
	@Column(name = "namCongTac", nullable = false)
	private Integer namCongTac;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "maKhoa", nullable = true)
	private Khoa khoa;
	
	@Column(name = "gioiTinh", nullable = false)
	private Integer gioiTinh;
	
//	@OneToOne( mappedBy = "giangVien")
//	@JsonIgnore
//	private TaiKhoan taiKhoan;
	

}
