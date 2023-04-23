package com.iuh.backendkltn32.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.PhanCong;

public interface PhanCongRepository extends JpaRepository<PhanCong, Integer>{

	List<PhanCong> findByNhom(Nhom nhom);
}
