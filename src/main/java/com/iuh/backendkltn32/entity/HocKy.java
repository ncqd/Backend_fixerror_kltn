package com.iuh.backendkltn32.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.boot.context.properties.bind.DefaultValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HocKy")
public class HocKy {
	
	@Id
	private String maHocKy;
	
	private String soHocKy;
	
	private Timestamp thoiGianBatDau;
	
	private Timestamp thoiGianKetThuc;
	
	@Column(columnDefinition = "boolean default false")
	private Boolean choXemDiem;

	@Override
	public String toString() {
		return "HocKy [maHocKy=" + maHocKy + ", soHocKy=" + soHocKy + "]";
	}
	



}
