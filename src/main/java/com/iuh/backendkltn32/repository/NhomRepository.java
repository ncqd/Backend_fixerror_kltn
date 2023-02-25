package com.iuh.backendkltn32.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.Nhom;

public interface NhomRepository extends JpaRepository<Nhom, String>{
	
	@Query(value = "\r\n"
			+ "select n.maNhom, tenNhom, maDeTai, tinhTrang from nhom n join sinhvien s on "
			+ "n.maNhom = s.maNhom join lopdanhnghia l on l.maLopDanhNghia = s.maLopDanhNghia "
			+ "where HocKy = :hocKy and namHoc = :NamHoc ; ", nativeQuery = true)
	List<Nhom> layNhomTheoNamHocKy(@Param("hocKy") Integer hocKy,@Param("NamHoc") String namHoc);

}
