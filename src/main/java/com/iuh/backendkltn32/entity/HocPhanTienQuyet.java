package com.iuh.backendkltn32.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HocPhanTienQuyet")
public class HocPhanTienQuyet {
	
	@Id
	@Column(name = "maHocPhan")
	private String maHocPhan;
	
	@Column(name = "tenHocPhan")
	private String tenHocPhan;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "hocPhanTienQuyet")
	@Column( nullable = true)
	private List<HocPhanTienQuyet_SinhVien> hocPhanTienQuyet_SinhViens; 

}
