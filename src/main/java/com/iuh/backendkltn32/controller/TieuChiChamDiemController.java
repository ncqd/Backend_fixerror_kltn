package com.iuh.backendkltn32.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.iuh.backendkltn32.entity.TieuChiChamDiem;
import com.iuh.backendkltn32.export.FileMauGiangVienExporter;
import com.iuh.backendkltn32.export.FileMauTieuChiChamDiem;
import com.iuh.backendkltn32.importer.TieuChiChamDiemImporter;
import com.iuh.backendkltn32.service.TieuChiChamDiemService;

@RestController
@RequestMapping("/api/tieu-chi-cham-diem")
public class TieuChiChamDiemController {
	
	@Autowired
	private TieuChiChamDiemService tieuChiChamDiemService;
	
	@Autowired
	private TieuChiChamDiemImporter tieuChiChamDiemImporter;
	
	@PostMapping("/them")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public TieuChiChamDiem themTieuChiChamDiem(@RequestBody TieuChiChamDiem tieuChiChamDiem) {

		try {
				
			return tieuChiChamDiemService.luu(tieuChiChamDiem);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PutMapping("/cap-nhat")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public TieuChiChamDiem capNhatTieuChiChamDiem(@RequestBody TieuChiChamDiem tieuChiChamDiem) {

		try {
				
			return tieuChiChamDiemService.capNhat(tieuChiChamDiem);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@DeleteMapping("/xoa/{ma}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public String xoaTieuChiChamDiem(@PathVariable("ma") String ma) {

		try {
				
			return tieuChiChamDiemService.xoa(ma);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping("/lay/{maPhieuCham}")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public List<TieuChiChamDiem> layTieuChiChamDiemTheoMaPhieu(@PathVariable("maPhieuCham") String ma) {
		try {
			return tieuChiChamDiemService.laydsTieuChiChamDiemTheoPhieuCham(ma);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@GetMapping("/lay-het")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public List<TieuChiChamDiem> layHetTieuChamDiem() {
		try {
			return tieuChiChamDiemService.layHet();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping("/them-tieu-chi-excel")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public ResponseEntity<?> themTieuChiChamDiem(@RequestParam("file") MultipartFile file) throws RuntimeException {
		if (TieuChiChamDiemImporter.isValidExcelFile(file)) {
			try {
				
				List<TieuChiChamDiem> tieuChiChamDiems = tieuChiChamDiemImporter.addDataFDromExcel(file.getInputStream());
				tieuChiChamDiemService.luuDs(tieuChiChamDiems);
				
				return ResponseEntity.ok(tieuChiChamDiems);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return null;
	}
	
	@GetMapping("/xuat-tieu-chi-excel-mau")
	@PreAuthorize("hasAuthority('ROLE_QUANLY')")
	public void xuatTieuChiChamMau(HttpServletResponse response) throws RuntimeException {
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=ketqua_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		FileMauTieuChiChamDiem tieuChiChamExporter = new FileMauTieuChiChamDiem();
		try {
			tieuChiChamExporter.export(response);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
