package com.iuh.backendkltn32.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iuh.backendkltn32.entity.SinhVien;

public interface SinhVienRepository extends JpaRepository<SinhVien, String> {

	Optional<SinhVien> findByMaSinhVien(String maSinhVien);
	
	
	@Query(value = "select maSinhVien from sinhvien where maNhom = ?1", nativeQuery = true)
	List<String> findByNhom(String maNhom);
}
