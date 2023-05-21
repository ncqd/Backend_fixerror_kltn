package com.iuh.backendkltn32.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.Nhom;

public interface NhomRepository extends JpaRepository<Nhom, String> {

	@Query(value = "select n.maNhom, tenNhom, maDeTai, tinhTrang, dkDeTai from nhom n join sinhvien s "
			+ "on n.maNhom = s.maNhom join lopHocPhan l "
			+ "on l.maLopHocPhan = s.maLopHocPhan join hocphankhoaluantotnghiep h "
			+ "on l.maHocPhan = h.maHocPhan join hocKy k " + "on k.maHocKy = h.maHocKy "
			+ "where k.maHocKy = :maHocKy and k.soHocKy = :soHocKy    ; ", nativeQuery = true)
	List<Nhom> layNhomTheoNamHocKy(@Param("maHocKy") String hocKy, @Param("soHocKy") String soHocKy);

	@Query(value = "select count(*) from sinhvien s join Nhom n on s.maNhom = n.maNhom where n.maNhom = :maNhom group by (s.maNhom)", nativeQuery = true)
	Integer laySoSinhVienTrongNhomTheoMa(@Param("maNhom") String maNhom);

	@Query(value = "select  * from nhom where maNhom like %:maNhomTemp% order by maNhom desc limit 1", nativeQuery = true)
	Nhom layNhomTheoThoiGianThuc(@Param("maNhomTemp") String maNhom);

	@Query(value = "select distinct n.maNhom, tenNhom, n.maDeTai, tinhTrang, dkDeTai from nhom n join sinhvien s  "
			+ "on n.maNhom = s.maNhom join lopHocPhan l  "
			+ "on l.maLopHocPhan = s.maLopHocPhan join hocphankhoaluantotnghiep h  "
			+ "on l.maHocPhan = h.maHocPhan join hocKy k  " 
			+ "on k.maHocKy = h.maHocKy join detai d "
			+ "on d.maDeTai = n.maDeTai  "
			+ "where k.maHocKy = :maHocKy and k.soHocKy = :soHocKy and maGiangVien = :maGiangVien  ; ", nativeQuery = true)
	List<Nhom> layNhomTheoMaGiangVien(@Param("maHocKy") String maHocKy, @Param("soHocKy") String soHocKy,
			@Param("maGiangVien") String maGiangVien);
	
	@Query(value = "select distinct n.maNhom, tenNhom, maDeTai, tinhTrang, dkDeTai from nhom n join sinhvien s "
			+ "on n.maNhom = s.maNhom join lopHocPhan l "
			+ "on l.maLopHocPhan = s.maLopHocPhan join hocphankhoaluantotnghiep h "
			+ "on l.maHocPhan = h.maHocPhan join hocKy k " + "on k.maHocKy = h.maHocKy "
			+ "where k.maHocKy = :maHocKy and k.soHocKy = :soHocKy and n.tinhTrang = :tinhTrang   ; ", nativeQuery = true)
	List<Nhom> layNhomTheoTinhTrang(@Param("maHocKy") String hocKy, @Param("soHocKy") String soHocKy, @Param("tinhTrang") Integer tinhTrang);
	
	@Query(value = "select distinct n.maNhom, tenNhom, maDeTai, tinhTrang, dkDeTai from nhom n join sinhvien s  "
			+ "on n.maNhom = s.maNhom join lopHocPhan l "
			+ "on l.maLopHocPhan = s.maLopHocPhan join hocphankhoaluantotnghiep h "
			+ "on l.maHocPhan = h.maHocPhan join hocKy k " + "on k.maHocKy = h.maHocKy join phancong p " 
			+ "on n.maNhom = p.maNhom "
			+ "where k.maHocKy = :maHocKy and p.magiangvien = :magiangvien  and vitriPhanCong = :viPhanCong ; ", nativeQuery = true)
	List<Nhom> layNhomTheoPhanCongHK(@Param("maHocKy") String hocKy, @Param("viPhanCong") String viPhanCong, @Param("magiangvien") String magiangvien);
	
	@Query(value = "select n.maNhom, tenNhom, maDeTai, tinhTrang, dkDeTai from nhom n join sinhvien s  "
			+ "on n.maNhom = s.maNhom join lopHocPhan l "
			+ "on l.maLopHocPhan = s.maLopHocPhan join hocphankhoaluantotnghiep h "
			+ "on l.maHocPhan = h.maHocPhan join hocKy k " + "on k.maHocKy = h.maHocKy join phancong p " 
			+ "on n.maNhom = p.maNhom "
			+ "where k.maHocKy = :maHocKy and p.magiangvien = :magiangvien ; ", nativeQuery = true)
	List<Nhom> layNhomTheoHK(@Param("maHocKy") String hocKy, @Param("magiangvien") String magiangvien);
	
	@Query(value = "select n.maNhom, tenNhom, n.maDeTai, tinhTrang, dkDeTai from nhom n join sinhvien s "
			+ "on n.maNhom = s.maNhom join lopHocPhan l on l.maLopHocPhan = s.maLopHocPhan join hocphankhoaluantotnghiep h "
			+ "on l.maHocPhan = h.maHocPhan join hocKy k on k.maHocKy = h.maHocKy join phancong p on n.maNhom = p.maNhom "
			+ "join phieucham a on p.maGiangVien = a.maGiangVien  "
			+ "where k.maHocKy = :maHocKy and p.magiangvien = :magiangvien and a.tenPhieu = 'PB' and diemPhieuCham >= 8 "
			+ "and p.vitriphancong = :viTriPhanCong ;  ", nativeQuery = true)
	List<Nhom> layNhomTheoPPChamHD(@Param("maHocKy") String hocKy, @Param("magiangvien") String magiangvien, @Param("viTriPhanCong") String viTriPhanCong);
	
	@Query(value = "select n.maNhom, tenNhom, n.maDeTai, tinhTrang, dkDeTai from nhom n join sinhvien s "
			+ "on n.maNhom = s.maNhom join lopHocPhan l on l.maLopHocPhan = s.maLopHocPhan join hocphankhoaluantotnghiep h "
			+ "on l.maHocPhan = h.maHocPhan join hocKy k on k.maHocKy = h.maHocKy join phancong p on n.maNhom = p.maNhom "
			+ "join phieucham a on p.maGiangVien = a.maGiangVien  "
			+ "where k.maHocKy = :maHocKy and p.magiangvien = :magiangvien and a.tenPhieu = 'PB' and diemPhieuCham < 8 "
			+ " and p.vitriphancong = :viTriPhanCong   ;  ", nativeQuery = true)
	List<Nhom> layNhomTheoPPChamPoster(@Param("maHocKy") String hocKy, @Param("magiangvien") String magiangvien, @Param("viTriPhanCong") String viTriPhanCong);
	
	@Query(value = "select distinct n.maNhom, tenNhom, n.maDeTai, tinhTrang, dkDeTai from nhom n join sinhvien s "
			+ "on n.maNhom = s.maNhom join lopHocPhan l on l.maLopHocPhan = s.maLopHocPhan join hocphankhoaluantotnghiep h "
			+ "on l.maHocPhan = h.maHocPhan join hocKy k on k.maHocKy = h.maHocKy join phancong p on n.maNhom = p.maNhom "
			+ "join phieucham a on p.maGiangVien = a.maGiangVien  "
			+ "where k.maHocKy = :maHocKy and a.tenPhieu = 'PB' and diemPhieuCham >=5 and  diemPhieuCham < 8", nativeQuery = true)
	List<Nhom> layNhomRaDuocPBPoster(@Param("maHocKy") String hocKy);
	
	@Query(value = "select distinct n.maNhom, tenNhom, n.maDeTai, tinhTrang, dkDeTai from nhom n join sinhvien s "
			+ "on n.maNhom = s.maNhom join lopHocPhan l on l.maLopHocPhan = s.maLopHocPhan join hocphankhoaluantotnghiep h "
			+ "on l.maHocPhan = h.maHocPhan join hocKy k on k.maHocKy = h.maHocKy join phancong p on n.maNhom = p.maNhom "
			+ "join phieucham a on p.maGiangVien = a.maGiangVien  "
			+ "where k.maHocKy = :maHocKy and a.tenPhieu = 'PB' and diemPhieuCham >= 8  ", nativeQuery = true)
	List<Nhom> layNhomRaDuocPBHD(@Param("maHocKy") String hocKy);
	
}
