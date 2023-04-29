package com.iuh.backendkltn32.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "TinNhan")
public class TinNhan {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "noiDung", nullable = false)
	private String noiDung;
	
	@Column(name = "maNguoiGui", nullable = false)
	private String maNguoiGui;
	
	@Column(name = "maNGuoiNhan", nullable = false)
	private String maNGuoiNhan;
	
	@Column(name = "trangThai", nullable = false)
	private Integer trangThai;
	
	@Column(name = "createdAt", nullable = false)
	private Timestamp createdAt;

}
