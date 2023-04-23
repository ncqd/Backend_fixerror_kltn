package com.iuh.backendkltn32.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iuh.backendkltn32.entity.HocPhanKhoaLuanTotNghiep;

public interface HocPhanKhoaLuanTotNghiepRepository extends JpaRepository<HocPhanKhoaLuanTotNghiep, String>{
	
	@Query(value = "select * from hocphankhoaluantotnghiep order by maHocPhan desc limit 1 ; ", nativeQuery = true)
	HocPhanKhoaLuanTotNghiep layHocPhanCuoiTrongDS();

}
