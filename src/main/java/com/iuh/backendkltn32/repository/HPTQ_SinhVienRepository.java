package com.iuh.backendkltn32.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iuh.backendkltn32.entity.HocPhanTienQuyet_SinhVien;
import com.iuh.backendkltn32.entity.SinhVien;

@Repository
public interface HPTQ_SinhVienRepository extends JpaRepository<HocPhanTienQuyet_SinhVien, Long> {

	List<HocPhanTienQuyet_SinhVien> findBySinhVien(SinhVien sinhVien);

}
