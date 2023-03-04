package com.iuh.backendkltn32.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iuh.backendkltn32.entity.DeTai;

public interface DeTaiRepository extends JpaRepository<DeTai, String> {
	
	@Query(value = "select maDeTai, gioiHanSoNhomThucHien, moTa, mucTieuDeTai, trangThai, sanPhamDuKien, tenDeTai, yeuCauDauVao, maGiangVien, d.maHocKy "
			+ "from DeTai d join hocKy h on d.hocKy = h.maHocKy "
			+ "where namHocKy = ? ; " ,nativeQuery = true)
	List<DeTai> layDsDeTaiTheoNamHocKy(String namHocKy);
	
	@Query(value = "select maDeTai, gioiHanSoNhomThucHien, moTa, mucTieuDeTai, sanPhamDuKien, tenDeTai, yeuCauDauVao, trangThai,maGiangVien, d.maHocKy "
			+ " from DeTai d join hocKy h on d.hocKy = h.maHocKy "
			+ " where namHocKy = ? and trangThai = 2; " ,nativeQuery = true)
	List<DeTai> layDsDeTaiTheoNamHocKyDaPheDuyet(String namHocKy);
	
	@Query(value = "select count(*) from nhom group by(:maDeTai);" ,nativeQuery = true)
	Integer laySoNhomDaDangKyDeTai(String maDeTai);
	
	@Query(value = "select maDeTai, gioiHanSoNhomThucHien, moTa, mucTieuDeTai, sanPhamDuKien, tenDeTai, trangThai, yeuCauDauVao, maGiangVien, d.maHocKy "
			+ "from DeTai d join hocKy h on d.maHocKy = h.maHocKy "
			+ "where namHocKy = :namHocKy order by maDeTai desc limit 1; " ,nativeQuery = true)
	DeTai layDeTaiCuoiCungTrongNamHocKy(String namHocKy); 
	
	
	

}
