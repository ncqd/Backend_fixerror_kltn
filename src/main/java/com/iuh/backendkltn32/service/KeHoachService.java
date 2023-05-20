package com.iuh.backendkltn32.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.repository.query.Param;

import com.iuh.backendkltn32.entity.KeHoach;

public interface KeHoachService extends AbstractService<KeHoach>{
	
	List<KeHoach> layKeHoachTheoMaHocKy(String maHocKy);
	
	List<KeHoach> layKeHoachTheoVaiTro( String maHocKy,String vaiTro);
	
	List<KeHoach> layKeHoachTheoMaNguoiDung( String maHocKy,String maNguoiDung);
	
	List<KeHoach> layTheoTenVaMaHocKyVaiTro( String maHocKy,String tenKeHoach, String vaiTro);
	
	List<KeHoach> layKeHoachTheoMaHocKyVaMaLoai(String maHocKy, String maLoai, String maNguoiDung);

	List<String> layMaNGuoiDung(Timestamp thoiGianBatDau,String phong);
	
	List<String> layPhong(Timestamp thoiGianBatDau);
	
	List<KeHoach> layTheoTenKhongMaNguoiDung( String maHocKy,String tenKeHoach, String vaiTro);
	
	List<String> layTheoPhongTg( String phong, Timestamp thoiGianBatDau);

	List<KeHoach> layTheoPhongTGChuaChiaNhom(String phong, Timestamp thoiGianBatDau);
	
	List<KeHoach> layKeHoachTheoMaNguoiDungMaLoai( String maHocKy, String maNguoiDung,  String maLoai);
	
	List<KeHoach> layKeHoachPB( String phong,Timestamp thoiGianBatDau);
}
