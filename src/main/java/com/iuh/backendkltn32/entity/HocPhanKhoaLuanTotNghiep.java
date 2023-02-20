package com.iuh.backendkltn32.entity;

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
@Table(name = "HocPhanKhoaLuanTotNghiep")
public class HocPhanKhoaLuanTotNghiep {
	
	@Id
	private String maHocPhan;
	
	@Column(name = "tenHocPhan", columnDefinition = "nvarchar(255)" ,nullable = false)
	private String tenHocPhan;
	
	@Column(name = "soTinChi", nullable = false)
	private String soTinChi;
	
	@Column(name = "hocPhantienQuyet", nullable = false)
	private boolean hocPhantienQuyet;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "maHocKy", nullable = false)
	private HocKy hocKy;
	
	

}
