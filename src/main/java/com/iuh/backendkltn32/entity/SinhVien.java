package com.iuh.backendkltn32.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "sinhvien")
public class SinhVien implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String maSinhVien;
	
	@Column(name = "tenSinhVien", columnDefinition = "nvarchar(255)" ,nullable = false)
	private String tenSinhVien;
	
	@Column(name = "noiSinh", columnDefinition = "nvarchar(255)" ,nullable = false)
	private String noiSinh;
	
	@Column(name = "dienThoai",nullable = false)
	private String dienThoai;
	
	@Column(name = "email", nullable = false)
	private String email;
	
	@Column(name = "ngaySinh",nullable = true)
	private Date ngaySinh;
	
	@Column(name = "namNhapHoc", nullable = false)
	private Integer namNhapHoc;
	
	@Column(name = "gioiTinh",nullable = false)
	private Integer gioiTinh;
	
	@Column(name = "anhDaiDien", columnDefinition = "varchar(255)" ,nullable = true)
	private String anhDaiDien;
	
	
	@ManyToOne()
	@JoinColumn(name = "maLopDanhNghia", nullable = true)
	private LopDanhNghia lopDanhNghia;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "maLopHocPhan", nullable = true)
	private LopHocPhan lopHocPhan;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sinhVien")
	@JsonIgnore
	private List<KetQua> dsKetQua;
	
	@ManyToOne()
	@JoinColumn(name = "maNhom", nullable = true)
	private Nhom nhom;
	
//	@OneToOne( mappedBy = "sinhVien")
//	@JsonIgnore
//	private TaiKhoan taiKhoan;
	
	@Override
	public String toString() {
		return "SinhVien [maSinhVien=" + maSinhVien + ", tenSinhVien=" + tenSinhVien + ", noiSinh=" + noiSinh
				+ ", dienThoai=" + dienThoai + ", email=" + email + ", namNhapHoc="
				+ namNhapHoc + ", gioiTinh=" + gioiTinh + "]";
	}
	
	
}
