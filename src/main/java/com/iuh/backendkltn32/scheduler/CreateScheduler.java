package com.iuh.backendkltn32.scheduler;

import java.util.Calendar;
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

	@SuppressWarnings({ "unused" })
	@Scheduled(cron = "* * * 1 6 *")
	public void createHocKy1VaHocPhanKhoaLuan() throws Exception {
		Date date = new Date();

		HocKy hocKy = new HocKy();

		String maHK = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2);

		hocKy.setMaHocKy(maHK + "1");
		hocKy.setSoHocKy("1");

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
	
	@SuppressWarnings({ "unused"})
	@Scheduled(cron = "* * * 1 11 *")
	public void createHocKy2VaHocPhanKhoaLuan() throws Exception {
		Date date = new Date();

		HocKy hocKy = new HocKy();

		String maHK = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2);

		hocKy.setMaHocKy(maHK + "2");
		hocKy.setSoHocKy("2");

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
