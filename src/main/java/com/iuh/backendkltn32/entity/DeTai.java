package com.iuh.backendkltn32.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
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
@Table(name = "DeTai")
public class DeTai implements Serializable {
	
	@Id
	private String maDeTai;
	
	@Column(name = "tenDeTai", nullable = false)
	private String tenDeTai;
	
	@Column(name = "mucTieuDeTai", columnDefinition = "nvarchar(255)" ,nullable = false)
	private String mucTieuDeTai;
	
	@Column(name = "sanPhamDuKien", columnDefinition = "nvarchar(255)" ,nullable = false)
	private String sanPhamDuKien;
	
	@Column(name = "moTa", columnDefinition = "nvarchar(255)")
	private String moTa;
	
	@Column(name = "yeuCauDauVao", columnDefinition = "nvarchar(255)" ,nullable = false)
	private String yeuCauDauVao;
	
	@Column(name = "trangThai", nullable = false)
	private int trangThai;
	
	@Column(name = "gioiHanSoNhomThucHien", nullable = false)
	private Integer gioiHanSoNhomThucHien;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "maGiangVien", nullable = false)
	private GiangVien giangVien;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {
	        CascadeType.PERSIST, 
	        CascadeType.MERGE
	    })
	@JoinColumn(name = "maHocKy", nullable = true)
	private HocKy hocKy;

	@Override
	public String toString() {
		return "DeTai [maDeTai=" + maDeTai + ", tenDeTai=" + tenDeTai + ", mucTieuDeTai=" + mucTieuDeTai
				+ ", sanPhamDuKien=" + sanPhamDuKien + ", moTa=" + moTa + ", yeuCauDauVao=" + yeuCauDauVao
				+ ", trangThai=" + trangThai + ", gioiHanSoNhomThucHien=" + gioiHanSoNhomThucHien + ", giangVien="
				+ giangVien + ", hocKy=" + hocKy + "]";
	}

	
	

}
