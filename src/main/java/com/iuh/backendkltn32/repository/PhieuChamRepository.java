package com.iuh.backendkltn32.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.PhieuCham;

public interface PhieuChamRepository extends JpaRepository<PhieuCham, String>{

	@Query(value = "select a.maPhieu, diemPhieuCham, tenPhieu, a.maDeTai, a.maGiangVien from phieucham a "
			+ "join detai d on a.madetai = d.madetai "
			+ "join ketqua k on a.maPhieu = k.maPhieu "
			+ " where a.maGiangVien = :maGiangVien and maHocKy = :mahocky ; " ,nativeQuery = true)
	List<PhieuCham> layDsDeTaiTheoNamHocKy(@Param("maGiangVien") String maGiangVien, @Param("mahocky") String mahocky);
	
	@Query(value = "select a.maPhieu, diemPhieuCham, tenPhieu, a.maDeTai, a.maGiangVien from phieucham a    "
			+ "join phancong p on p.maGiangVien = a.maGiangVien  "
			+ "join detai d on a.madetai = d.madetai  "
			+ "join ketqua k on a.maPhieu = k.maPhieu "
			+ "where a.maGiangVien = :maGiangVien and diemPhieuCham >= 8 and tenPhieu = 'PB' "
			+ "and viTriPhanCong = :viTriPhanCong and maHocKy = :mahocky ; " ,nativeQuery = true)
	List<PhieuCham> layPhieuTheoPPChamHD(@Param("maGiangVien") String maGiangVien, @Param("mahocky") String mahocky, 
			@Param("viTriPhanCong") String viTriPhanCong);
	
	@Query(value = "select a.maPhieu, diemPhieuCham, tenPhieu, a.maDeTai, a.maGiangVien from phieucham a    "
			+ "join phancong p on p.maGiangVien = a.maGiangVien  "
			+ "join detai d on a.madetai = d.madetai "
			+ "join ketqua k on a.maPhieu = k.maPhieu "
			+ "where a.maGiangVien = :maGiangVien and diemPhieuCham < 8 and tenPhieu = 'PB' "
			+ "and viTriPhanCong = :viTriPhanCong and maHocKy = :mahocky ; " ,nativeQuery = true)
	List<PhieuCham> layPhieuTheoPPChamPOSTER(@Param("maGiangVien") String maGiangVien, @Param("mahocky") String mahocky, 
			@Param("viTriPhanCong") String viTriPhanCong);
	
	@Query(value = "select a.maPhieu, diemPhieuCham, tenPhieu, a.maDeTai, a.maGiangVien from phieucham a   \n"
			+ "join detai d on a.madetai = d.madetai  "
			+ "join ketqua k on a.maPhieu = k.maPhieu  "
			+ "where a.maGiangVien = :maGiangVien and tenPhieu = :tenPhieu and maHocKy = :mahocky ; " ,nativeQuery = true)
	List<PhieuCham> layPhieuTheoVaiTro(@Param("maGiangVien") String maGiangVien, @Param("mahocky") String mahocky, 
			@Param("tenPhieu") String tenPhieu);
	
	
}
