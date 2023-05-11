package com.iuh.backendkltn32.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.LopHocPhan;
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
	
	@Query(value = "select distinct s.maNhom from sinhvien s join ketqua k "
			+ "on s.maSinhVien = k.maSinhVien join phieucham p "
			+ "on k.maPhieu = p.maPhieu where maNhom = :maNhom and tenPhieu = :tenPhieu and p.maGiangVien = :maGiangVien ", nativeQuery = true)
	List<String> timMaSinhVienChuaCoPhieuChamDiemTheoNhuCau(@Param("maNhom") String maNhom,@Param("tenPhieu") String tenPhieu
			,@Param("maGiangVien") String maGiangVien);
	
	@Query(value = "select distinct s.maNhom from sinhvien s join ketqua k  "
			+ "on s.maSinhVien = k.maSinhVien join phieucham p  "
			+ "on k.maPhieu = p.maPhieu where maNhom = :maNhom and tenPhieu like :tenPhieu1 and diemPhieuCham >= 5 ", nativeQuery = true)
	String timMaSinhVienChuaCoPhieuChamDiemTheoNhuCauCoDiemLon(@Param("maNhom") String maNhom,@Param("tenPhieu1") String tenPhieu);
	
	List<SinhVien> findByLopHocPhan(LopHocPhan lopHocPhan);
}
