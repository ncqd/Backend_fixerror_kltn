package com.iuh.backendkltn32.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.KeHoach;

public interface KeHoachRepository extends JpaRepository<KeHoach, Integer>{
	
	List<KeHoach> findByHocKy(HocKy hocKy);

}
