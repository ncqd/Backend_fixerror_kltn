package com.iuh.backendkltn32.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.KeHoach;

public interface KeHoachRepository extends JpaRepository<KeHoach, Integer>{
	
	List<KeHoach> findByHocKy(HocKy hocKy);
	
	@Query(value = "select * from kehoach where maHocKy = :maHocKy and vaiTro = :vaiTro and maNguoiDung is null ;", nativeQuery = true)
	List<KeHoach> layKeHoachTheoVaiTro(@Param("maHocKy") String maHocKy,@Param("vaiTro") String vaiTro);
	
	@Query(value = "select * from kehoach where maHocKy = :maHocKy and  maNguoiDung = :maNguoiDung ;", nativeQuery = true)
	List<KeHoach> layKeHoachTheoMaNguoiDung(@Param("maHocKy") String maHocKy,@Param("maNguoiDung") String maNguoiDung);

	@Query(value = "select * from kehoach where maHocKy = :maHocKy and tenKeHoach = :tenKeHoach and vaiTro = :vaiTro   ;", nativeQuery = true)
	List<KeHoach> layTheoTenVaMaHocKyVaiTro(@Param("maHocKy") String maHocKy,@Param("tenKeHoach") String tenKeHoach, @Param("vaiTro") String vaiTro);
	
	@Query(value = "select * from kehoach where maHocKy = :maHocKy and maLoai = :maLoai and  maNguoiDung = :maNguoiDung ;", nativeQuery = true)
	List<KeHoach> layKeHoachTheoMaNguoiDungVaMaLoai(@Param("maHocKy") String maHocKy,@Param("maLoai")String maLoai, @Param("maNguoiDung") String maNguoiDung);
	
	@Query(value = "select maNguoiDung from kehoach where thoiGianBatDau = :thoiGianBatDau and vaiTro = 'ROLE_GIANGVIEN' ;", nativeQuery = true)
	List<String> layMaNGuoiDung(@Param("thoiGianBatDau") Timestamp thoiGianBatDau);
	
	@Query(value = "select phong from kehoach where thoiGianBatDau = :thoiGianBatDau and vaiTro = 'ROLE_GIANGVIEN' ;", nativeQuery = true)
	List<String> layPhong(@Param("thoiGianBatDau") Timestamp thoiGianBatDau);
	
	@Query(value = "select * from kehoach where maHocKy = :maHocKy and tenKeHoach = :tenKeHoach and vaiTro = :vaiTro and maNguoiDung is not null  ;", nativeQuery = true)
	List<KeHoach> layTheoTenKhongMaNguoiDung(@Param("maHocKy") String maHocKy,@Param("tenKeHoach") String tenKeHoach, @Param("vaiTro") String vaiTro);
	
	@Query(value = "select maNguoiDung from kehoach where phong = :phong and thoiGianBatDau = :thoiGianBatDau ;", nativeQuery = true)
	List<String> layTheoPhongTg(@Param("phong") String phong,@Param("thoiGianBatDau") Timestamp thoiGianBatDau);
}
