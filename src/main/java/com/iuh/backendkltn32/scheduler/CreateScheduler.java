package com.iuh.backendkltn32.scheduler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.HocPhanKhoaLuanTotNghiep;
import com.iuh.backendkltn32.service.HocKyService;
import com.iuh.backendkltn32.service.HocPhanKhoaLuanTotNghiepService;

@Component
public class CreateScheduler {

	@Autowired
	private HocKyService hocKyService;

	@Autowired
	private HocPhanKhoaLuanTotNghiepService hocPhanKhoaLuanTotNghiepService;

	@SuppressWarnings({ "unused", "deprecation" })
	@Scheduled(cron = "* * * 1 6 *")
	public void createHocKy1VaHocPhanKhoaLuan() throws Exception {
		Date date = new Date();

		HocKy hocKy = new HocKy();

		String maHK = "001";

		if (hocKy == null) {
			maHK = "001";
		} else {
			Long soMaHPKL = Long.parseLong(hocKy.getMaHocKy().substring(2)) + 1;
			if (soMaHPKL < 9) {
				maHK = "00" + soMaHPKL ;
			} else if (soMaHPKL >= 9 && soMaHPKL < 100) {
				maHK = "0" + soMaHPKL;
			} else {
				maHK = "" + soMaHPKL;
			}
		}

		hocKy.setMaHocKy("HK" + maHK);
		hocKy.setNamHocKy("HK1 (" + date.getYear() + "-" + date.getYear() + 1 + ")");

		hocKyService.luu(hocKy);

		HocPhanKhoaLuanTotNghiep hpkl = new HocPhanKhoaLuanTotNghiep();

		HocPhanKhoaLuanTotNghiep hocKhoaLuanTotNghiep = hocPhanKhoaLuanTotNghiepService.layHocPhanCuoiTrongDS();
		String maHPKL = "001";
		if (hocKhoaLuanTotNghiep == null) {

			maHPKL = "001";
		} else {
			Long soMaHPKL = Long.parseLong(hocKhoaLuanTotNghiep.getMaHocPhan().substring(5)) + 1;
			if (soMaHPKL < 9) {
				maHPKL = "00" + soMaHPKL ;
			} else if (soMaHPKL >= 9 && soMaHPKL < 100) {
				maHPKL = "0" + soMaHPKL ;
			} else {
				maHPKL = "" + soMaHPKL ;
			}
		}

		hpkl.setHocKy(hocKy);
		hpkl.setHocPhantienQuyet(true);
		hpkl.setMaHocPhan("42223" + maHPKL);
		hpkl.setSoTinChi("5");
		hpkl.setTenHocPhan("Khoa Luan Tot Nghiep");
		hocPhanKhoaLuanTotNghiepService.luu(hocKhoaLuanTotNghiep);
	}
	
	@SuppressWarnings({ "unused", "deprecation" })
	@Scheduled(cron = "* * * 1 11 *")
	public void createHocKy2VaHocPhanKhoaLuan() throws Exception {
		Date date = new Date();

		HocKy hocKy = new HocKy();

		String maHK = "001";

		if (hocKy == null) {
			maHK = "001";
		} else {
			Long soMaHPKL = Long.parseLong(hocKy.getMaHocKy().substring(2)) + 1;
			if (soMaHPKL < 9) {
				maHK = "00" + soMaHPKL ;
			} else if (soMaHPKL >= 9 && soMaHPKL < 100) {
				maHK = "0" + soMaHPKL ;
			} else {
				maHK = "" + soMaHPKL;
			}
		}

		hocKy.setMaHocKy("HK" + maHK);
		hocKy.setNamHocKy("HK2 (" + date.getYear() + "-" + date.getYear() + 1 + ")");

		hocKyService.luu(hocKy);

		HocPhanKhoaLuanTotNghiep hpkl = new HocPhanKhoaLuanTotNghiep();

		HocPhanKhoaLuanTotNghiep hocKhoaLuanTotNghiep = hocPhanKhoaLuanTotNghiepService.layHocPhanCuoiTrongDS();
		String maHPKL = "001";
		if (hocKhoaLuanTotNghiep == null) {

			maHPKL = "001";
		} else {
			Long soMaHPKL = Long.parseLong(hocKhoaLuanTotNghiep.getMaHocPhan().substring(5)) + 1;
			if (soMaHPKL < 9) {
				maHPKL = "00" + soMaHPKL;
			} else if (soMaHPKL >= 9 && soMaHPKL < 100) {
				maHPKL = "0" + soMaHPKL ;
			} else {
				maHPKL = "" + soMaHPKL ;
			}
		}

		hpkl.setHocKy(hocKy);
		hpkl.setHocPhantienQuyet(true);
		hpkl.setMaHocPhan("42223" + maHPKL);
		hpkl.setSoTinChi("5");
		hpkl.setTenHocPhan("Khoa Luan Tot Nghiep");
		hocPhanKhoaLuanTotNghiepService.luu(hocKhoaLuanTotNghiep);
	}

}
