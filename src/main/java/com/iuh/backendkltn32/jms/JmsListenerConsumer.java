package com.iuh.backendkltn32.jms;

import java.util.List;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import com.iuh.backendkltn32.dto.DangKyNhomRequest;
import com.iuh.backendkltn32.entity.DeTai;
import com.iuh.backendkltn32.entity.HocKy;
import com.iuh.backendkltn32.entity.Nhom;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.service.DeTaiService;
import com.iuh.backendkltn32.service.HocKyService;
import com.iuh.backendkltn32.service.NhomService;
import com.iuh.backendkltn32.service.SinhVienService;
import com.iuh.backendkltn32.utils.Constants;

@Component
public class JmsListenerConsumer implements MessageListener {

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private MessageConverter messageConverter;

	@Autowired
	private DeTaiService deTaiService;

	@Autowired
	private SinhVienService sinhVienService;

	@Autowired
	private NhomService nhomService;

	@Autowired
	private HocKyService hocKyService;

	public String receiveMessage() {
		try {
			Message message = jmsTemplate.receive();
			String response = (String) messageConverter.fromMessage(message);
			System.out.println(response);
			return response;

		} catch (Exception exe) {
			exe.printStackTrace();
		}

		return null;
	}

	
	public ResponseEntity<?> listenerDeTaiChannel() throws Exception {
		jmsTemplate.setDefaultDestinationName("detai_channel");
		Message message = jmsTemplate.receive();
		if (message instanceof MapMessage) {
			MapMessage mapMessage = (MapMessage) message;
			DeTai deTai = deTaiService.layTheoMa(mapMessage.getString("maDeTai"));
			Integer soNhomDaDKDeTai = deTaiService.laySoNhomDaDangKyDeTai(mapMessage.getString("maDeTai")) != null ? 
					deTaiService.laySoNhomDaDangKyDeTai(mapMessage.getString("maDeTai")) : 0;

			if (soNhomDaDKDeTai >= deTai.getGioiHanSoNhomThucHien()) {
				throw new Exception("Khong the dang ky de tai " + deTai.getTenDeTai() + " da day");
			}
			Nhom nhomDangKy = nhomService.layTheoMa(mapMessage.getString("maNhom"));
			nhomDangKy.setDeTai(deTai);
			return ResponseEntity.ok(nhomService.capNhat(nhomDangKy));
		}
		return null;
	}

	public ResponseEntity<?> listenerNhomChannel() throws Exception {
		jmsTemplate.setDefaultDestinationName("nhom_channel");
		Message message = jmsTemplate.receive();
		if (message instanceof MapMessage) {
			MapMessage mapMessage = (MapMessage) message;
			@SuppressWarnings("unchecked")
			List<String> dsMaSv = (List<String>) mapMessage.getObject("dsMaSinhVien");
			System.out.println("Listener - maNhom - " + mapMessage);
			DangKyNhomRequest request = new DangKyNhomRequest(dsMaSv, mapMessage.getString("maNhom"), mapMessage.getString("maDeTai"), mapMessage.getString("password"));
			System.out.println("Listener - dang ky nhom - " + request);
			try {
				if (request.getMaNhom() == null) {
					for (String maSv : request.getDsMaSinhVien()) {
						SinhVien sv = sinhVienService.layTheoMa(maSv); // check SV co nhom chua
						if (sv != null) {
							if (sv.getNhom() != null) {
								throw new Exception("Khong the dang ky nhom vi sinh vien " + sv.getTenSinhVien() + " da co nhom");
							}
						} else {
							throw new Exception("Ma sinh vien: " + maSv + " khong dung");
						}

					}
					String maNhomMoi = taoMaMoi();
					DeTai deTai = null;
					if (request.getMaDeTai() != null) {
						deTai = deTaiService.layTheoMa(request.getMaDeTai());
					}
					Nhom nhom = nhomService.luu(new Nhom(maNhomMoi, "Nhom " + maNhomMoi.substring(5), deTai, 0, 0, request.getPassword()));
					for (String maSv : request.getDsMaSinhVien()) {
						SinhVien sv = sinhVienService.layTheoMa(maSv);
						sv.setNhom(nhom);
						sinhVienService.capNhat(sv);
					}
					return ResponseEntity.ok(nhom);
				} else {
					Nhom nhomJoin = nhomService.layTheoMa(request.getMaNhom());
					if (nhomJoin == null) {
						return ResponseEntity.status(500).body(
								"Khong the dang ky vi nhom khong ton tai");
					}

					if (nhomService.laySoSinhVienTrongNhomTheoMa(request.getMaNhom()) >= 2) {
						throw new Exception(
								"Khong the dang ky nhom vi sinh vien vi " + nhomJoin.getTenNhom() + " da day");
					}
					SinhVien sinhVien2 = sinhVienService.layTheoMa(request.getDsMaSinhVien().get(1));
					if (sinhVien2.getNhom() != null) {
						 throw new Exception(
								"Khong the dang ky nhom vi sinh vien " + sinhVien2.getTenSinhVien() + " da co nhom");
					}
					sinhVien2.setNhom(nhomJoin);
					sinhVienService.capNhat(sinhVien2);
					return ResponseEntity.ok(nhomJoin);

				}
			} catch (Exception e) {
				 throw new Exception(e.getMessage());
			}
		}
		return null;
	}

	@Override
	public void onMessage(Message message) {
		try {
			String response = (String) messageConverter.fromMessage(message);
			System.out.println(response);

		} catch (Exception exe) {
			exe.printStackTrace();
		}

	}

	private String taoMaMoi() {
		HocKy hocKy = hocKyService.layHocKyCuoiCungTrongDS();
		Nhom nhom = nhomService
				.layNhomTheoThoiGianHienThuc(Constants.NHOM + "" + hocKy.getMaHocKy() + hocKy.getSoHocKy());
		if (nhom != null) {
			Integer soNhomHienHanh = Integer.parseInt(nhom.getMaNhom().substring(5)) + 1;
			String maNhom = null;
			if (soNhomHienHanh < 9) {
				maNhom = "00" + soNhomHienHanh;
			} else if (soNhomHienHanh >= 9 && soNhomHienHanh < 100) {
				maNhom = "0" + soNhomHienHanh;
			} else {
				maNhom = "" + soNhomHienHanh;
			}

			return Constants.NHOM + "" + hocKy.getMaHocKy() + hocKy.getSoHocKy() + maNhom;
		}
		return Constants.NHOM + "" + hocKy.getMaHocKy() + hocKy.getSoHocKy() + "001";
	}
}