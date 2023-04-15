package com.iuh.backendkltn32.dto;

import java.sql.Timestamp;
import java.util.List;

import com.iuh.backendkltn32.entity.HocKy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class LapKeHoachDto {

	private Integer id;

	private String tenKeHoach;

	private List<String> dsNgayThucHienKhoaLuan;

	private HocKy hocKy;

	private Timestamp thoiGianBatDau;

	private Timestamp thoiGianKetThuc;

	private Integer tinhTrang;

}
