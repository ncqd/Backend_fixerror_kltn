package com.iuh.backendkltn32.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.utils.Constants;

public interface NhomRepository extends JpaRepository<Nhom, String>{
	
	@Query(value = "\r\n"
			+ "select n.maNhom, tenNhom, maDeTai, tinhTrang from nhom n join sinhvien s on "
			+ "n.maNhom = s.maNhom join lopdanhnghia l on l.maLopDanhNghia = s.maLopDanhNghia "
			+ "where HocKy = :hocKy and namHoc = :NamHoc ; ", nativeQuery = true)
	List<Nhom> layNhomTheoNamHocKy(@Param("hocKy") Integer hocKy,@Param("NamHoc") String namHoc);

	@Query(value = "select count(*) from sinhvien s join Nhom n on s.maNhom = n.maNhom where n.maNhom = :maNhom group by (s.maNhom)", nativeQuery = true)
	Integer laySoSinhVienTrongNhomTheoMa(@Param("maNhom") String maNhom);
	
	@Query(value = "select  * from nhom where maNhom like %:maNhomTemp% order by maNhom desc limit 1", nativeQuery = true)
	Nhom layNhomTheoThoiGianThuc(@Param("maNhomTemp") String maNhom);
}
