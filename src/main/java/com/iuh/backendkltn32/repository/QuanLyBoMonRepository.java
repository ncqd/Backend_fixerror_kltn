package com.iuh.backendkltn32.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.QuanLyBoMon;

public interface QuanLyBoMonRepository extends JpaRepository<QuanLyBoMon, String>{

	@Query(value = "select maQuanLyBoMon, q.maGiangVien, anhDaiDien, cmnd, email, gioiTinh, hocVi, namCongTac, ngaySinh, soDienThoai, tenGiangVien, maKhoa"
			+ " from giangvien q join quanlybomon g on q.magiangvien = g.magiangvien where q.magiangvien = :maGianVien", nativeQuery = true)
	QuanLyBoMon getQuanLyTheoMaGiangVien(@Param("maGianVien") String maGiangVien);
}
