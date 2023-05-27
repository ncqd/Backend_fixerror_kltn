package com.iuh.backendkltn32.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.GiangVien;

public interface GiangVienRepository extends JpaRepository<GiangVien, String> {
	
	@Query(nativeQuery = true, value = "select count(maDeTai) from detai where maGiangvien = :maGiangVien and maHocKy = :maHocKy")
	Integer soDTGVCo(@Param("maGiangVien") String maGiangVien,@Param("maHocKy") String maHocKy );
	
}

