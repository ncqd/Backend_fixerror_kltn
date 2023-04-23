package com.iuh.backendkltn32.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.TieuChiChamDiem;

public interface TieuChiChamDiemRepository extends JpaRepository<TieuChiChamDiem, Integer> {
	
	@Query(value = "select * from tieuchichamdiem", nativeQuery = true)
	List<TieuChiChamDiem> layTieuChiChamDiemTheoPhieuCham(@Param("maPhieu") String maPhieu);

}
