package com.iuh.backendkltn32.dto;

import com.iuh.backendkltn32.entity.DiemThanhPhan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DiemSinhVienDto {
    private String maSV;
    private String tenSV;
    private String maNhom;
    private String tenNhom;
    private String gvhd;
    private String maDeTai;
    private String tenDeTai;
    private double diem;

    private List<DiemThanhPhan> diemThanhPhans;
}
