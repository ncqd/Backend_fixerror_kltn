package com.iuh.backendkltn32.repository;

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

}