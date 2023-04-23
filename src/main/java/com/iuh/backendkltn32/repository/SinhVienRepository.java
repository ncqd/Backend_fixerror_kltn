package com.iuh.backendkltn32.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.SinhVien;

public interface SinhVienRepository extends JpaRepository<SinhVien, String> {

	Optional<SinhVien> findByMaSinhVien(String maSinhVien);
	
	
	@Query(value = "select maSinhVien from sinhvien where maNhom = ?1", nativeQuery = true)
	List<String> findByNhom(String maNhom);
	
	@Query(value = "select * from sinhvien s join lophocphan l  "
			+ "on s.maLopHocPhan = l.maLopHocPhan join hocphankhoaluantotnghiep h  "
			+ "on h.maHocPhan = l.maHocPhan join hocky k  "
			+ "on k.maHocKy = h.maHocKy where k.maHocKy = :maHocKy and k.soHocKy =  :soHocKy  ;"
			+ "; ", nativeQuery = true)
	List<SinhVien> layTatCaSinhVienTheoHocKy(@Param("maHocKy") String maHocKy,@Param("soHocKy") String soHocKy);
}
