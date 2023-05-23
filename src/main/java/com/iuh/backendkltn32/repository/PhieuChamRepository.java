package com.iuh.backendkltn32.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.PhieuCham;

public interface PhieuChamRepository extends JpaRepository<PhieuCham, Integer> {

	@Query(value = "select a.maPhieu, diemPhieuCham, tenPhieu, a.maDeTai, a.maGiangVien from phieucham a "
			+ "join detai d on a.madetai = d.madetai " + "join ketqua k on a.maPhieu = k.maPhieu "
			+ " where a.maGiangVien = :maGiangVien and maHocKy = :mahocky ; ", nativeQuery = true)
	List<PhieuCham> layDsDeTaiTheoNamHocKy(@Param("maGiangVien") String maGiangVien, @Param("mahocky") String mahocky);

	@Query(value = "select distinct a.maPhieu, diemPhieuCham, tenPhieu, a.maDeTai, a.maGiangVien from phieucham a    "
			+ "join phancong p on p.maGiangVien = a.maGiangVien  " + "join detai d on a.madetai = d.madetai  "
			+ "join ketqua k on a.maPhieu = k.maPhieu "
			+ "where a.maGiangVien = :maGiangVien and diemPhieuCham >= 8 and tenPhieu =  :tenPhieu "
			+ "and viTriPhanCong = :viTriPhanCong and maHocKy = :mahocky ; ", nativeQuery = true)
	List<PhieuCham> layPhieuTheoPPChamHD(@Param("maGiangVien") String maGiangVien, @Param("mahocky") String mahocky,
			@Param("viTriPhanCong") String viTriPhanCong, 
			@Param("tenPhieu") String tenPhieu);

	@Query(value = "select distinct a.maPhieu, diemPhieuCham, tenPhieu, a.maDeTai, a.maGiangVien from phieucham a    "
			+ "join phancong p on p.maGiangVien = a.maGiangVien  " + "join detai d on a.madetai = d.madetai "
			+ "join ketqua k on a.maPhieu = k.maPhieu "
			+ "where a.maGiangVien = :maGiangVien and diemPhieuCham < 8 and tenPhieu = :tenPhieu "
			+ "and viTriPhanCong = :viTriPhanCong and maHocKy = :mahocky ; ", nativeQuery = true)
	List<PhieuCham> layPhieuTheoPPChamPOSTER(@Param("maGiangVien") String maGiangVien, @Param("mahocky") String mahocky,
			@Param("viTriPhanCong") String viTriPhanCong, 
			@Param("tenPhieu") String tenPhieu);

	@Query(value = "select a.maPhieu, diemPhieuCham, tenPhieu, a.maDeTai, a.maGiangVien from phieucham a   \n"
			+ "join detai d on a.madetai = d.madetai  " + "join ketqua k on a.maPhieu = k.maPhieu  "
			+ "where a.maGiangVien = :maGiangVien and tenPhieu = :tenPhieu and maHocKy = :mahocky ; ", nativeQuery = true)
	List<PhieuCham> layPhieuTheoVaiTro(@Param("maGiangVien") String maGiangVien, @Param("mahocky") String mahocky,
			@Param("tenPhieu") String tenPhieu);

	@Query(value = "select a.maPhieu, diemPhieuCham, tenPhieu,   a.maDeTai, a.maGiangVien from phieucham a   "
			+ "join ketqua k on a.maPhieu = k.maPhieu "
			+ "where maSinhVien = :maSinhVien and tenPhieu = :tenPhieu order by a.maPhieu desc ; ", nativeQuery = true)
	List<PhieuCham> layPhieuTheoMaSinhVienTenVaiTro(@Param("maSinhVien") String maSinhVien,
			@Param("tenPhieu") String tenPhieu);

	@Query(value = "select p.maPhieu from phieucham p " + "join ketqua k on k.maPhieu = p.maPhieu "
			+ "join sinhvien s on k.maSInhVien = s.maSInHVien " + "join nhom n on n.maNhom = s.maNhom "
			+ "join detai d on d.maDeTai = n.maDeTai "
			+ "where d.maHocKy = :maHocKy and p.tenPhieu = :tenPhieu and n.maNhom = :maNhom  ; ", nativeQuery = true)
	List<String> layMaPhieuPhieuTheoMaSinhVienTenVaiTro(@Param("maHocKy") String maHocKy,
			@Param("tenPhieu") String tenPhieu, @Param("maNhom") String maNhom);
	
	@Query(value = "select  a.maPhieu, diemPhieuCham, tenPhieu, a.maDeTai, a.maGiangVien from phieucham a "
			+ "join detai d on a.madetai = d.madetai " + "join ketqua k on a.maPhieu = k.maPhieu "
			+ " where maHocKy = :mahocky and k.maSinhVien = :maSinhVien  ; ", nativeQuery = true)
	List<PhieuCham> layDsPhieuTheoNamHocKyQL( @Param("mahocky") String mahocky, @Param("maSinhVien") String maSInhVien);

	@Query(value = "select  a.maPhieu, diemPhieuCham, tenPhieu, a.maDeTai, a.maGiangVien from phieucham a    "
			+ "join phancong p on p.maGiangVien = a.maGiangVien  " + "join detai d on a.madetai = d.madetai  "
			+ "join ketqua k on a.maPhieu = k.maPhieu "
			+ "where diemPhieuCham >= 8 and tenPhieu = :tenPhieu  and k.maSinhVien = :maSinhVien "
			+ "and viTriPhanCong = :viTriPhanCong and maHocKy = :mahocky ; ", nativeQuery = true)
	List<PhieuCham> layPhieuTheoPPChamHDQL( @Param("mahocky") String mahocky , 
			@Param("viTriPhanCong") String viTriPhanCong , @Param("maSinhVien") String maSInhVien, 
			@Param("tenPhieu") String tenPhieu);

	@Query(value = "select a.maPhieu, diemPhieuCham, tenPhieu, a.maDeTai, a.maGiangVien from phieucham a    "
			+ "join phancong p on p.maGiangVien = a.maGiangVien  " + "join detai d on a.madetai = d.madetai "
			+ "join ketqua k on a.maPhieu = k.maPhieu "
			+ "where diemPhieuCham < 8 and tenPhieu = :tenPhieu  and k.maSinhVien = :maSinhVien "
			+ "and viTriPhanCong = :viTriPhanCong and maHocKy = :mahocky ; ", nativeQuery = true)
	List<PhieuCham> layPhieuTheoPPChamPOSTERQL(@Param("mahocky") String mahocky,
			@Param("viTriPhanCong") String viTriPhanCong , @Param("maSinhVien") String maSInhVien, 
			@Param("tenPhieu") String tenPhieu);

	@Query(value = "select a.maPhieu, diemPhieuCham, tenPhieu, a.maDeTai, a.maGiangVien from phieucham a   \n"
			+ "join detai d on a.madetai = d.madetai  " + "join ketqua k on a.maPhieu = k.maPhieu  "
			+ "where tenPhieu = :tenPhieu and maHocKy = :mahocky  and k.maSinhVien = :maSinhVien ; ", nativeQuery = true)
	List<PhieuCham> layPhieuTheoVaiTroQL(@Param("mahocky") String mahocky ,
			@Param("tenPhieu") String tenPhieu , @Param("maSinhVien") String maSInhVien);
}
