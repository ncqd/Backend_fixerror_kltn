package com.iuh.backendkltn32.dto;

import com.iuh.backendkltn32.entity.GiangVien;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HocPhanTienQuyetDeTaiRequest {

	private String hocPhanTienQuyet;
	private String diemHocPhanTienQuyet;

}
