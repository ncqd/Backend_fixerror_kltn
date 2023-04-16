package com.iuh.backendkltn32.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iuh.backendkltn32.entity.HocKy;

public interface HocKyRepository extends JpaRepository<HocKy, String>{
	
	@Query(value = "select * from hocky order by maHocKy desc limit 1; " ,nativeQuery = true)
	HocKy layHocKyCuoiCungTrongDS();
	
	
	@Query(value = "select * from hocky order by maHocKy desc; " ,nativeQuery = true)
	List<HocKy> findAll();

}
