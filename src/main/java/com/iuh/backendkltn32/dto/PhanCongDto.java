package com.iuh.backendkltn32.dto;

import java.util.List;

import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.Nhom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhanCongDto {

	private Integer maPhanCong;

	private String viTriPhanCong;

	private Boolean chamCong;

	private String maNhom;

	private List<String> dsMaGiangVienPB;

}
