package com.iuh.backendkltn32.dto;

import java.util.List;

import com.iuh.backendkltn32.entity.DiemThanhPhan;
import com.iuh.backendkltn32.entity.KetQua;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhieuChamKetQuaQL {

	private String maPhieu;

	private String tenPhieu;

	private Double diemPhieuCham;

	private List<DiemThanhPhan> dsDiemThanhPhan;

	private String maGiangVien;
	
	private String tenGiangVien;

}
