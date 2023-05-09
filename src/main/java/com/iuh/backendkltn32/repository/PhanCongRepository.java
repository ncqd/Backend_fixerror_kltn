package com.iuh.backendkltn32.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.PhanCong;

public interface PhanCongRepository extends JpaRepository<PhanCong, Integer>{

	List<PhanCong> findByNhom(Nhom nhom);
	
	@Query(nativeQuery = true, value = "select * from phancong where manhom = :maNhom and viTriPhanCong = :tenPhanCong")
	List<PhanCong> timTheoTenVaMaNhom(@Param("maNhom") String maNhom, @Param("tenPhanCong") String tenPhanCong);
	
}
