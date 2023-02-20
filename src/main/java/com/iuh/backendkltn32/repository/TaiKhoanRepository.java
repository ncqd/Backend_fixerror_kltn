package com.iuh.backendkltn32.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.iuh.backendkltn32.entity.TaiKhoan;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, String>{
	
	Optional<TaiKhoan> findByTenTaiKhoan(String tenTaiKhoan);
	
}
