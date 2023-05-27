package com.iuh.backendkltn32.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iuh.backendkltn32.entity.HocPhanTienQuyet;

@Repository
public interface HocPhanTienQuyetRepository extends JpaRepository<HocPhanTienQuyet, String> {

}
