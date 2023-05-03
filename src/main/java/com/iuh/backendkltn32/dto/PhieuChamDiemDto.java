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
public class PhieuChamDiemDto {
	
	private String maPhieuCham;
	private List<TieuChiChamDiemDto> dsTieuChiChamDiem; 
	private String tenPhieu;
	private Double ketQuaTong;
	private String maDeTai;
	private String maGiangVien;
	private List<SinhVienNhomVaiTroDto> sinhVien;
	private List<BangDiemDto> bangDiem;

}
