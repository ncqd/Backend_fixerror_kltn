package com.iuh.backendkltn32.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.DeTai;

public interface DeTaiRepository extends JpaRepository<DeTai, String> {
	
	@Query(value = "select maDeTai, gioiHanSoNhomThucHien, moTa, mucTieuDeTai, trangThai, sanPhamDuKien, tenDeTai, yeuCauDauVao, doKhoDeTai, maGiangVien, d.maHocKy "
			+ "from DeTai d join hocKy h on d.maHocKy = h.maHocKy "
			+ "where  h.maHocKy = :maHocKy and h.soHocKy = :soHocKy and maGiangVien = :maGianVien ; " ,nativeQuery = true)
	List<DeTai> layDsDeTaiTheoNamHocKyTheoMaGiangVien(@Param("maHocKy") String maHocKy,@Param("soHocKy") String soHocKy, @Param("maGianVien") String maGiangVien);
	
	@Query(value = "select maDeTai, gioiHanSoNhomThucHien, moTa, mucTieuDeTai, trangThai, sanPhamDuKien, tenDeTai, yeuCauDauVao,doKhoDeTai, maGiangVien, d.maHocKy "
			+ "from DeTai d join hocKy h on d.maHocKy = h.maHocKy "
			+ "where  h.maHocKy = :maHocKy and h.soHocKy = :soHocKy  ; " ,nativeQuery = true)
	List<DeTai> layDsDeTaiTheoNamHocKy(@Param("maHocKy") String maHocKy,@Param("soHocKy") String soHocKy);
	
	@Query(value = "select maDeTai, gioiHanSoNhomThucHien, moTa, mucTieuDeTai, sanPhamDuKien, tenDeTai, yeuCauDauVao,doKhoDeTai, trangThai,maGiangVien, d.maHocKy "
			+ " from DeTai d join hocKy h on d.maHocKy = h.maHocKy "
			+ " where h.maHocKy = :maHocKy and h.soHocKy = :soHocKy and trangThai = 2; " ,nativeQuery = true)
	List<DeTai> layDsDeTaiTheoNamHocKyDaPheDuyet(@Param("maHocKy") String maHocKy,@Param("soHocKy") String soHocKy);
	
	@Query(value = "select count(*) from nhom  where maDeTai = :maDeTai group by(:maDeTai);" ,nativeQuery = true)
	Integer laySoNhomDaDangKyDeTai(String maDeTai);
	
	@Query(value = "select maDeTai, gioiHanSoNhomThucHien, moTa, mucTieuDeTai, sanPhamDuKien, tenDeTai, trangThai,doKhoDeTai, yeuCauDauVao, maGiangVien, d.maHocKy "
			+ "from DeTai d join hocKy h on d.maHocKy = h.maHocKy "
			+ "where h.maHocKy = :maHocKy and h.soHocKy = :soHocKy order by maDeTai desc limit 1; " ,nativeQuery = true)
	DeTai layDeTaiCuoiCungTrongNamHocKy(@Param("maHocKy") String maHocKy,@Param("soHocKy") String soHocKy); 
	
	@Query(value = "select maDeTai, gioiHanSoNhomThucHien, moTa, mucTieuDeTai, trangThai, sanPhamDuKien, tenDeTai,doKhoDeTai, yeuCauDauVao, maGiangVien, d.maHocKy "
			+ "from DeTai d join hocKy h on d.maHocKy = h.maHocKy "
			+ "where  h.maHocKy = :maHocKy and h.soHocKy = :soHocKy and maGiangVien = :maGianVien and trangThai = :trangThai ;" ,nativeQuery = true)
	List<DeTai> layDsDeTaiTheoNamHocKyTheoTrangThaiCoMaGv(
			@Param("maHocKy") String maHocKy,
			@Param("soHocKy") String soHocKy, 
			@Param("maGianVien") String maGiangVien, 
			@Param("trangThai") Integer trangThai);
	
	@Query(value = "select maDeTai, gioiHanSoNhomThucHien, moTa, mucTieuDeTai, trangThai, sanPhamDuKien, tenDeTai, doKhoDeTai,yeuCauDauVao, maGiangVien, d.maHocKy "
			+ "from DeTai d join hocKy h on d.maHocKy = h.maHocKy "
			+ "where  h.maHocKy = :maHocKy and h.soHocKy = :soHocKy and trangThai = :trangThai ;" ,nativeQuery = true)
	List<DeTai> layDsDeTaiTheoNamHocKyTheoTrangThai(
			@Param("maHocKy") String maHocKy,
			@Param("soHocKy") String soHocKy, 
			@Param("trangThai") Integer trangThai);
	
	@Query(value = "select maDeTai, gioiHanSoNhomThucHien, moTa, mucTieuDeTai, trangThai, sanPhamDuKien, tenDeTai,doKhoDeTai, yeuCauDauVao, maGiangVien, d.maHocKy "
			+ "from DeTai d join hocKy h on d.maHocKy = h.maHocKy "
			+ "where  h.maHocKy = :maHocKy and h.soHocKy = :soHocKy and maGiangVien = :maGianVien and trangThai = 1; " ,nativeQuery = true)
	List<DeTai> layDsDeTaiTheoNamHocKyChuaDat(@Param("maHocKy") String maHocKy,@Param("soHocKy") String soHocKy, @Param("maGianVien") String maGiangVien);
	
	@Query(value = "select * from DeTai "
			+ "where  maHocKy = :maHocKy and trangThai = 2  order by maGiangVien ; " ,nativeQuery = true)
	List<DeTai> layDsDeTaiXuaExcel(@Param("maHocKy") String maHocKy);
	

}
