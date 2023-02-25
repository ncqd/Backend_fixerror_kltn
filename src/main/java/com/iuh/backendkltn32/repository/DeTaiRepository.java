package com.iuh.backendkltn32.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iuh.backendkltn32.entity.DeTai;

public interface DeTaiRepository extends JpaRepository<DeTai, String> {
	
	@Query(value = "select maDeTai, gioiHanSoNhomThucHien, moTa, mucTieuDeTai, sanPhamDuKien, tenDeTai, yeuCauDauVao, trangThai,maGiangVien, d.maLopHocPhan "
			+ " from DeTai d join lophocphan l on d.maLopHocPhan = l.maLopHocPhan join hocphankhoaluantotnghiep h on "
			+ " l.maHocPhan = h.maHocPhan join hocky k on h.maHocKy = k.maHocKy "
			+ " where namHoc = ? ; " ,nativeQuery = true)
	List<DeTai> layDsDeTaiTheoNamHocKy(String namHocKy);
	
	@Query(value = "select maDeTai, gioiHanSoNhomThucHien, moTa, mucTieuDeTai, sanPhamDuKien, tenDeTai, yeuCauDauVao, trangThai,maGiangVien, d.maLopHocPhan "
			+ " from DeTai d join lophocphan l on d.maLopHocPhan = l.maLopHocPhan join hocphankhoaluantotnghiep h on "
			+ " l.maHocPhan = h.maHocPhan join hocky k on h.maHocKy = k.maHocKy "
			+ " where namHoc = ? and trangThai = 2; " ,nativeQuery = true)
	List<DeTai> layDsDeTaiTheoNamHocKyDaPheDuyet(String namHocKy);
	
	@Query(value = "select count(*) from nhom group by(:maDeTai);" ,nativeQuery = true)
	Integer laySoNhomDaDangKyDeTai(String maDeTai);

}
