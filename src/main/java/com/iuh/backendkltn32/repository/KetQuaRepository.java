package com.iuh.backendkltn32.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.KetQua;

public interface KetQuaRepository extends JpaRepository<KetQua, Long>{

	@Query(value= "select * from ketqua where maSinhVien = :maSinhVien", nativeQuery = true)
	List<KetQua> layDiemBaoByMaSv(@Param("maSinhVien")String maSinhVien);
}
