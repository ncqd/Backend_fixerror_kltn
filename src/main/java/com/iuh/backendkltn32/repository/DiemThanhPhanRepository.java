package com.iuh.backendkltn32.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.DiemThanhPhan;

public interface DiemThanhPhanRepository extends JpaRepository<DiemThanhPhan, Long>{

	@Query(value = "select * from diemthanhphan where maPhieu = :maPhieu  ; ", nativeQuery = true)
	public List<DiemThanhPhan> layTheoMaPhieuCham(@Param("maPhieu") String maPhiehCham);
}
