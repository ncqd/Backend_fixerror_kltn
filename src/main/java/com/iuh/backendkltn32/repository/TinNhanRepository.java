package com.iuh.backendkltn32.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.TinNhan;

public interface TinNhanRepository extends JpaRepository<TinNhan, Integer> {

	@Query(value = "select * from tinnhan where maNguoiNhan = :maNguoiNhan", nativeQuery = true)
	List<TinNhan> layTinNhanTheoMaNguoiNhan(@Param("maNguoiNhan")String maNguoiNhan);
}
