package com.iuh.backendkltn32.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.PhieuCham;

public interface PhieuChamRepository extends JpaRepository<PhieuCham, String>{

	@Query(value = "select * from phieucham where maGiangVien = :maGiangVien  ; " ,nativeQuery = true)
	List<PhieuCham> layDsDeTaiTheoNamHocKy(@Param("maGiangVien") String maGiangVien);
}
