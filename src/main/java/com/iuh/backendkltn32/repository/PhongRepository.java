package com.iuh.backendkltn32.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iuh.backendkltn32.entity.Phong;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PhongRepository extends JpaRepository<Phong, Integer>{

    @Query(value = "select * from phong where tenPhong = :tenPhong", nativeQuery = true)
	Phong layPhongTheoTen(@Param("tenPhong") String tenPhong);

}
