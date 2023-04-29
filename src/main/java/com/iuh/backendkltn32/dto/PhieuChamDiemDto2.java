package com.iuh.backendkltn32.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PhieuChamDiemDto2 {
	
	private String maPhieuCham;
	private List<String> dsTieuChiChamDiem; 
	private Double ketQuaTong;
	private String maDeTai;
	private String maGiangVien;

}
