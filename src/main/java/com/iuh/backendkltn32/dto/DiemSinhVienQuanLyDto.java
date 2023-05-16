package com.iuh.backendkltn32.dto;



import com.iuh.backendkltn32.entity.PhieuCham;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DiemSinhVienQuanLyDto {

	private String maSV;
	private String tenSV;
	private String maNhom;
	private String tenNhom;
	private String gvhd;
	private String maDeTai;
	private String tenDeTai;
	private PhieuChamKetQuaQL phieuChamDiemHD;
	private PhieuChamKetQuaQL phieuChamDiemPB1;
	private PhieuChamKetQuaQL phieuChamDiemPB2;
	private PhieuChamKetQuaQL phieuChamDiemCT;
	private PhieuChamKetQuaQL phieuChamDiemTK;
	private Double diemTBBM;
	private Double diemTBTVHD;
	private Double ketQua;
	

}
