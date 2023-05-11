package com.iuh.backendkltn32.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.HocPhanKhoaLuanTotNghiep;

public interface HocPhanKhoaLuanTotNghiepRepository extends JpaRepository<HocPhanKhoaLuanTotNghiep, String>{
	
	@Query(value = "select * from hocphankhoaluantotnghiep order by maHocPhan desc limit 1 ; ", nativeQuery = true)
	HocPhanKhoaLuanTotNghiep layHocPhanCuoiTrongDS();
	
	@Query(value = "select * from hocphankhoaluantotnghiep where maHocKy = :maHocKy ; ", nativeQuery = true)
	HocPhanKhoaLuanTotNghiep layHocPhanTheoMaHK(@Param("maHocKy") String maHocKy);

}
