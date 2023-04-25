package com.iuh.backendkltn32.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.PhieuChamMau;

public interface PhieuChamMauRepository extends JpaRepository<PhieuChamMau, Integer>{

	@Query(value = "select * from phieuchammau where vaiTroDung = :vaiTroNguoiDung  ", nativeQuery = true)
	List<PhieuChamMau> phieuChamMaus(@Param("vaiTroNguoiDung")String vaiTroNguoiDung);
}
