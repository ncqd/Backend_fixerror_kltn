package com.iuh.backendkltn32.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.iuh.backendkltn32.entity.*;
import com.iuh.backendkltn32.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.dto.DuyetRequest;
import com.iuh.backendkltn32.dto.LapKeHoachDto;
import com.iuh.backendkltn32.dto.LayDeTaiRquestDto;
import com.iuh.backendkltn32.dto.LoginRequest;
import com.iuh.backendkltn32.dto.PhanCongDto;
import com.iuh.backendkltn32.dto.PhanCongDto2;
import com.iuh.backendkltn32.export.DanhSachDeTaiExporter;
import com.iuh.backendkltn32.export.DanhSachNhomExporter;
import com.iuh.backendkltn32.export.KetQuaKLTNExporter;
import com.iuh.backendkltn32.export.SinhVienExcelExporoter;

@RestController
@RequestMapping("/api/quan-ly")
public class QuanLyBoMonController {

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private GiangVienService giangVienService;

    @Autowired
    private DeTaiService deTaiService;

    @Autowired
    private NhomService nhomService;

    @Autowired
    private KeHoachService keHoachService;

    @Autowired
    private HocKyService hocKyService;

    @Autowired
    private PhanCongService phanCongService;

    @Autowired
    private TinNhanSerivce tinNhanSerivce;

    @Autowired
    private PhongService phongService;

    @Autowired
    private DanhSachNhomExporter danhSachNhomExporter;

    @Autowired
    private PhieuChamMauService phieuChamMauService;

    @Autowired
    private TieuChiChamDiemService tieuChiChamDiemService;

    @GetMapping("/thong-tin-ca-nhan/{maQuanLy}")
    @PreAuthorize("hasAuthority('ROLE_QUANLY')")
    public GiangVien hienThiThongTinCaNhan(@PathVariable String maQuanLy, @RequestBody LoginRequest loginRequest) {
        try {
            if (!loginRequest.getTenTaiKhoan().equals(maQuanLy)) {
                throw new Exception("Khong Dung Ma Giang Vien");
            }
            GiangVien giangVien = giangVienService.layTheoMa(maQuanLy);

            return giangVien;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/xuat-ds-sinhvien")
    @PreAuthorize("hasAuthority('ROLE_QUANLY')")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=danh-sach-sinh-vien_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<SinhVien> dsSinhVien = sinhVienService.layTatCaSinhVien();

        SinhVienExcelExporoter excelExporter = new SinhVienExcelExporoter(dsSinhVien);

        excelExporter.export(response);
    }

    @PostMapping("/duyet-de-tai")
    @PreAuthorize("hasAuthority('ROLE_QUANLY')")
    public DeTai pheDuyetDeTai(@RequestBody DuyetRequest duyetDeTaiRequest) {
        try {
            DeTai deTai = deTaiService.layTheoMa(duyetDeTaiRequest.getMa());
            deTai.setTrangThai(duyetDeTaiRequest.getTrangThai());
            deTaiService.capNhat(deTai);
            TinNhan tinNhan;
            if (duyetDeTaiRequest.getTrangThai() == 1) {
                tinNhan = new TinNhan("Đề " + deTai.getTenDeTai() + " chưa đủ điều kiện duyệt.Chi Tiết: " + duyetDeTaiRequest.getLoiNhan(),
                        "12392401", deTai.getGiangVien().getMaGiangVien(), 0,
                        new Timestamp(System.currentTimeMillis()));
            } else {
                tinNhan = new TinNhan("Đề Tài " + deTai.getTenDeTai() + " Đã Được Duyệt Bởi người quản lý", "12392401",
                        deTai.getGiangVien().getMaGiangVien(), 0, new Timestamp(System.currentTimeMillis()));
            }

            tinNhanSerivce.luu(tinNhan);
            return deTai;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @PostMapping("/duyet-nhom")
    @PreAuthorize("hasAuthority('ROLE_QUANLY')")
    public Nhom pheDuyetNhom(@RequestBody DuyetRequest duyetNhomRequest) {
        try {
            Nhom nhom = nhomService.layTheoMa(duyetNhomRequest.getMa());

            nhom.setTinhTrang(duyetNhomRequest.getTrangThai());
            nhomService.capNhat(nhom);
            return nhom;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @PostMapping("/them-ke-hoach")
    @PreAuthorize("hasAuthority('ROLE_QUANLY')")
    public LapKeHoachDto themKeHoach(@RequestBody LapKeHoachDto lapKeHoachDto) throws Exception {
        HocKy hocKy = hocKyService.layTheoMa(lapKeHoachDto.getMaHocKy());
        KeHoach keHoach = new KeHoach(lapKeHoachDto.getTenKeHoach(), lapKeHoachDto.getChuThich(),
                lapKeHoachDto.getDsNgayThucHienKhoaLuan() != null ? lapKeHoachDto.getDsNgayThucHienKhoaLuan().toString()
                        .substring(1, lapKeHoachDto.getDsNgayThucHienKhoaLuan().toString().length() - 1) : null,
                hocKy, lapKeHoachDto.getThoiGianBatDau(), lapKeHoachDto.getThoiGianKetThuc(),
                lapKeHoachDto.getTinhTrang(), lapKeHoachDto.getVaiTro(), lapKeHoachDto.getMaNguoiDung(),
                new LoaiKeHoach(lapKeHoachDto.getMaLoaiLich()));
        keHoachService.luu(keHoach);
        return lapKeHoachDto;
    }

    @PutMapping("/cap-nhat-ke-hoach")
    @PreAuthorize("hasAuthority('ROLE_QUANLY')")
    public LapKeHoachDto capNhatKeHoach(@RequestBody LapKeHoachDto lapKeHoachDto) throws Exception {
        HocKy hocKy = hocKyService.layTheoMa(lapKeHoachDto.getMaHocKy());
        KeHoach keHoach = new KeHoach(lapKeHoachDto.getId(), lapKeHoachDto.getTenKeHoach(), lapKeHoachDto.getChuThich(),
                lapKeHoachDto.getDsNgayThucHienKhoaLuan() != null ? lapKeHoachDto.getDsNgayThucHienKhoaLuan().toString()
                        .substring(1, lapKeHoachDto.getDsNgayThucHienKhoaLuan().toString().length() - 1) : null,
                hocKy, lapKeHoachDto.getThoiGianBatDau(), lapKeHoachDto.getThoiGianKetThuc(),
                lapKeHoachDto.getTinhTrang(), lapKeHoachDto.getVaiTro(), lapKeHoachDto.getMaNguoiDung(),
                new LoaiKeHoach(lapKeHoachDto.getMaLoaiLich()), "");
        keHoachService.capNhat(keHoach);
        return lapKeHoachDto;
    }

    @DeleteMapping("/xoa-ke-hoach/{maKeHoach}")
    @PreAuthorize("hasAuthority('ROLE_QUANLY')")
    public String xoaKeHoach(@PathVariable String maKeHoach) throws Exception {
        return keHoachService.xoa(maKeHoach);
    }

    @GetMapping("/lay-ke-hoach/{maKeHoach}")
    @PreAuthorize("hasAuthority('ROLE_QUANLY')")
    public LapKeHoachDto layKeHoachTheoMa(@PathVariable String maKeHoach) throws Exception {
        KeHoach kh = keHoachService.layTheoMa(maKeHoach);

        String[] ngayThucHienKL = kh.getDsNgayThucHienKhoaLuan() != null ? kh.getDsNgayThucHienKhoaLuan().split(",\\s")
                : new String[0];
        LapKeHoachDto lapKeHoachDto = new LapKeHoachDto(kh.getId(), kh.getTenKeHoach(), kh.getChuThich(),
                Arrays.asList(ngayThucHienKL), kh.getHocKy(), new Timestamp(kh.getThoiGianBatDau().getTime()),
                new Timestamp(kh.getThoiGianKetThuc().getTime()), kh.getTinhTrang(), kh.getVaiTro(),
                kh.getMaNguoiDung(), kh.getLoaiKeHoach().getId());
        return lapKeHoachDto;
    }

    @GetMapping("/lay-ke-hoach-theo-hocky/{maHocKy}")
    @PreAuthorize("hasAuthority('ROLE_QUANLY')")
    public List<LapKeHoachDto> layKeHoachTheoHk(@PathVariable String maHocKy) throws Exception {
        List<LapKeHoachDto> ds = new ArrayList<>();
        keHoachService.layKeHoachTheoMaHocKy(maHocKy).stream().forEach(kh -> {

            String[] ngayThucHienKL = kh.getDsNgayThucHienKhoaLuan() != null
                    ? kh.getDsNgayThucHienKhoaLuan().split(",\\s")
                    : new String[0];
            LapKeHoachDto lapKeHoachDto = new LapKeHoachDto(kh.getId(), kh.getTenKeHoach(), kh.getChuThich(),
                    Arrays.asList(ngayThucHienKL), kh.getHocKy(), new Timestamp(kh.getThoiGianBatDau().getTime()),
                    new Timestamp(kh.getThoiGianKetThuc().getTime()), kh.getTinhTrang(), kh.getVaiTro(),
                    kh.getMaNguoiDung(), kh.getLoaiKeHoach().getId());
            ds.add(lapKeHoachDto);
        });
        return ds;
    }

    @PostMapping("/them-phan-cong")
    @PreAuthorize("hasAuthority('ROLE_QUANLY')")
    public ResponseEntity<?> themPhanCongGiangVien(@RequestBody PhanCongDto phanCongDto) throws Exception {
        Nhom nhom = nhomService.layTheoMa(phanCongDto.getMaNhom() == null ? "123" : phanCongDto.getMaNhom());
        List<PhanCong> phanCongs = phanCongDto.getDsMaGiangVienPB().stream().map(ma -> {
            GiangVien giangVien;
            try {
                if (nhom != null) {
                    if (nhom.getDeTai().getGiangVien().getMaGiangVien().equals(ma)) {
                        throw new Exception("Không cho phép giảng viên hướng dẫn phản biện đề tài này");
                    }
                }

                giangVien = giangVienService.layTheoMa(ma);

                PhanCong phanCong = new PhanCong(phanCongDto.getViTriPhanCong(), phanCongDto.getChamCong(), nhom,
                        giangVien);
                phanCong.setMaPhanCong(phanCongDto.getMaPhanCong());
                return phanCongService.luu(phanCong);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).toList();
        Integer gioBatDau = Integer.parseInt(phanCongDto.getTietBatDau()) + 5;
        Integer gioKetThuc = Integer.parseInt(phanCongDto.getTietKetThuc()) + 5;
        Timestamp tgbd = new Timestamp(phanCongDto.getNgay().getTime());
        tgbd.setHours(gioBatDau);
        Timestamp tgkt = new Timestamp(phanCongDto.getNgay().getTime());
        tgkt.setHours(gioKetThuc);
        sinhVienService.layTatCaSinhVienTheoNhom(phanCongDto.getMaNhom() == null ? "123" : nhom.getMaNhom()).stream().forEach(sv -> {
            try {

                KeHoach keHoach = new KeHoach("Lịch phản biện sinh viên", phanCongDto.getPhong(), null,
                        hocKyService.layTheoMa(phanCongDto.getMaHocKy()), tgbd, tgkt, 1, "ROLE_SINHVIEN", sv,
                        new LoaiKeHoach(3));
                keHoachService.luu(keHoach);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return ResponseEntity.ok(phanCongs);
    }

    @PostMapping("/them-ds-phan-cong")
    @PreAuthorize("hasAuthority('ROLE_QUANLY')")
    public ResponseEntity<?> themPhanCongGiangVien(@RequestBody List<PhanCongDto2> phanCongDtos) throws Exception {
        List<PhanCong> phanCongs = new ArrayList<>();
        for (PhanCongDto2 phanCongDto : phanCongDtos) {
            Nhom nhom = nhomService.layTheoMa(phanCongDto.getMaNhom() == null ? "123" : phanCongDto.getMaNhom());
            if (phanCongDto.getViTriPhanCong().equals("phan bien")) {
                for (String ma : phanCongDto.getDsMaGiangVienPB()) {
                    GiangVien giangVien;
                    try {
                        if (nhom != null) {
                            if (nhom.getDeTai().getGiangVien().getMaGiangVien().equals(ma)) {
                                return ResponseEntity.status(500).body("Không cho phép giảng viên hướng dẫn phản biện đề tài này");
                            }
                        }
                        System.out.println(ma);
                        giangVien = giangVienService.layTheoMa(ma);

                        PhanCong phanCong = new PhanCong(phanCongDto.getViTriPhanCong(), phanCongDto.getChamCong(), nhom,
                                giangVien);
                        phanCong.setMaPhanCong(phanCongDto.getMaPhanCong());
                        phanCongs.add(phanCongService.luu(phanCong));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (phanCongDto.getViTriPhanCong().equals("hoi dong")) {
                try {
                    String maGVCT = phanCongDto.getDsMaGiangVienPB().get(0);
                    String maGVTK = phanCongDto.getDsMaGiangVienPB().get(1);
                    String maGVTV3 = phanCongDto.getDsMaGiangVienPB().get(2);
                    GiangVien giangVienCT = giangVienService.layTheoMa(maGVCT);
                    GiangVien giangVienTK = giangVienService.layTheoMa(maGVTK);
                    GiangVien giangVientv3 = giangVienService.layTheoMa(maGVTV3);

                    if (nhom != null) {
                        if (nhom.getDeTai().getGiangVien().getMaGiangVien().equals(maGVCT)) {
                            return ResponseEntity.status(500).body("Không cho phép giảng viên hướng dẫn phản biện đề tài này");
                        }
                    }
                    if (nhom != null) {
                        if (nhom.getDeTai().getGiangVien().getMaGiangVien().equals(maGVTK)) {
                            return ResponseEntity.status(500).body("Không cho phép giảng viên hướng dẫn phản biện đề tài này");
                        }
                    }
                    if (nhom != null) {
                        if (nhom.getDeTai().getGiangVien().getMaGiangVien().equals(maGVTV3)) {
                            return ResponseEntity.status(500).body("Không cho phép giảng viên hướng dẫn phản biện đề tài này");
                        }
                    }

                    PhanCong phanCongCT = new PhanCong("chu tich", phanCongDto.getChamCong(), nhom,
                            giangVienCT);
                    phanCongCT.setMaPhanCong(phanCongDto.getMaPhanCong());
                    phanCongs.add(phanCongService.luu(phanCongCT));

                    PhanCong phanCongTK = new PhanCong("thu ky", phanCongDto.getChamCong(), nhom,
                            giangVienTK);
                    phanCongTK.setMaPhanCong(phanCongDto.getMaPhanCong());
                    phanCongs.add(phanCongService.luu(phanCongTK));

                    PhanCong phanCongTV3 = new PhanCong("thanh vien 3", phanCongDto.getChamCong(), nhom,
                            giangVientv3);
                    phanCongTV3.setMaPhanCong(phanCongDto.getMaPhanCong());
                    phanCongs.add(phanCongService.luu(phanCongTV3));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        Timestamp tgbd = new Timestamp(phanCongDto.getNgay().getTime());
        Timestamp tgkt = new Timestamp(phanCongDto.getNgay().getTime());
        switch (phanCongDto.getTiet()) {
            case "1-2":
                tgbd.setHours(6);
                tgbd.setMinutes(30);
                tgkt.setHours(8);
                tgkt.setMinutes(10);
                break;
            case "3-4":
                tgbd.setHours(8);
                tgbd.setMinutes(10);
                tgkt.setHours(10);
                break;
            case "5-6":
                tgbd.setHours(10);
                tgkt.setHours(11);
                tgbd.setMinutes(40);
                break;
            case "7-8":
                tgbd.setHours(12);
                tgbd.setMinutes(30);
                tgkt.setHours(2);
                tgbd.setMinutes(10);
                break;
            case "9-10":
                tgbd.setHours(14);
                tgbd.setMinutes(10);
                tgkt.setHours(3);
                tgbd.setMinutes(50);
                break;
            case "11-12":
                tgbd.setHours(16);
                tgkt.setHours(17);
                tgbd.setMinutes(40);
                break;
        }
        sinhVienService.layTatCaSinhVienTheoNhom(phanCongDto.getMaNhom() == null ? "123" : nhom.getMaNhom()).stream().forEach(sv -> {
            try {
                Phong phong = phongService.layPhongTheoTenPhong(phanCongDto.getPhong());
                if (phanCongDto.getViTriPhanCong().equals("PB")) {
                    KeHoach keHoach = new KeHoach("Lịch phản biện sinh viên", phong.getTenPhong(), null,
                            hocKyService.layTheoMa(phanCongDto.getMaHocKy()), tgbd, tgkt, 1, "ROLE_SINHVIEN", sv,
                            new LoaiKeHoach(3), phong.getId()+"");
                    keHoachService.luu(keHoach);
                } else {
                    KeHoach keHoach = new KeHoach("Lịch chấm hội đồng sinh viên", phong.getTenPhong(), null,
                            hocKyService.layTheoMa(phanCongDto.getMaHocKy()), tgbd, tgkt, 1, "ROLE_SINHVIEN", sv,
                            new LoaiKeHoach(3), phong.getId()+"");
                    keHoachService.luu(keHoach);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
		return ResponseEntity.ok(phanCongs);

}

    @PutMapping("/cap-nhat-phan-cong")
    @PreAuthorize("hasAuthority('ROLE_QUANLY')")
    public ResponseEntity<?> capNhatPhanCongGiangVien(@RequestBody PhanCongDto phanCongDto) throws Exception {
        Nhom nhom = nhomService.layTheoMa(phanCongDto.getMaNhom());
        List<PhanCong> phanCongs = phanCongDto.getDsMaGiangVienPB().stream().map(ma -> {
            GiangVien giangVien;
            try {
                if (nhom.getDeTai().getGiangVien().getMaGiangVien().equals(ma)) {
                    throw new Exception("Không cho phép giảng viên hướng dẫn phản biện đề tài này");
                }
                giangVien = giangVienService.layTheoMa(ma);

                PhanCong phanCong = new PhanCong(phanCongDto.getViTriPhanCong(), phanCongDto.getChamCong(), nhom,
                        giangVien);
                phanCong.setMaPhanCong(phanCongDto.getMaPhanCong());
                return phanCongService.capNhat(phanCong);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }).toList();

        return ResponseEntity.ok(phanCongs);
    }

    @DeleteMapping("/xoa-phan-cong/{maPhanCong}")
    @PreAuthorize("hasAuthority('ROLE_QUANLY')")
    public ResponseEntity<?> xoaPhanCong(@PathVariable("maPhanCong") String maPhanCong) throws Exception {

        return ResponseEntity.ok(phanCongService.xoa(maPhanCong));
    }

    @GetMapping("/lay-ds-giang-vien")
    @PreAuthorize("hasAuthority('ROLE_QUANLY')")
    public ResponseEntity<?> xoaPhanCong() throws Exception {

        return ResponseEntity.ok(giangVienService.layDanhSach());
    }

    @PostMapping("/lay-ds-sinh-vien")
    @PreAuthorize("hasAuthority('ROLE_QUANLY')")
    public ResponseEntity<?> layDsSinhVien(@RequestBody LayDeTaiRquestDto request) {

        try {

            if (request.getMaHocKy() == null) {
                return ResponseEntity.ok(sinhVienService.layTatCaSinhVien());
            }

            return ResponseEntity
                    .ok(sinhVienService.layTatCaSinhVienTheoHocKy(request.getMaHocKy(), request.getSoHocKy()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/xuat-ds-nhom")
    @PreAuthorize("hasAuthority('ROLE_QUANLY')")
    public void xuatDSNhom(HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=danh-nhom-sinh-vien_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        danhSachNhomExporter = new DanhSachNhomExporter(hocKyService, nhomService, sinhVienService);
        danhSachNhomExporter.export(response);
    }

    @GetMapping("/xuat-ds-de-tai")
    @PreAuthorize("hasAuthority('ROLE_QUANLY')")
    public void xuatDSDeTai(HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=danh-de-tai_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
        DanhSachDeTaiExporter danhSachDeTaiExporter = new DanhSachDeTaiExporter(deTaiService, hocKy.getMaHocKy());
        danhSachDeTaiExporter.export(response);
    }

    @GetMapping("/xuat-mailmerge")
    @PreAuthorize("hasAuthority('ROLE_QUANLY')")
    public void xuatMailMerge(HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=danh-de-tai_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
        DanhSachDeTaiExporter danhSachDeTaiExporter = new DanhSachDeTaiExporter(deTaiService, hocKy.getMaHocKy());
        danhSachDeTaiExporter.export(response);
    }

    @GetMapping("/xuat-ketquanhom-kltn")
    @PreAuthorize("hasAuthority('ROLE_QUANLY')")
    public void xuatKetQuaNhomKLTN(HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ketqua_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
        KetQuaKLTNExporter ketQuaKLTNExporter = new KetQuaKLTNExporter(phieuChamMauService, tieuChiChamDiemService, hocKy);
        ketQuaKLTNExporter.export(response);
    }
}
