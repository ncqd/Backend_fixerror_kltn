package com.iuh.backendkltn32.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iuh.backendkltn32.entity.DeTai;
import com.iuh.backendkltn32.entity.HocPhanTienQuyet_DeTai;

@Repository
public interface HPTQ_DeTaiRepository extends JpaRepository<HocPhanTienQuyet_DeTai, Long> {

	List<HocPhanTienQuyet_DeTai> findByDeTai(DeTai deTai);
}
