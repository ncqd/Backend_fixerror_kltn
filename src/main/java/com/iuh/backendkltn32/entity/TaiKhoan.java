package com.iuh.backendkltn32.entity;




import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan{
	
	@Id
	@Column(name = "tenTaiKhoan")
	private String tenTaiKhoan;
	
	@Column(name = "password", columnDefinition = "varchar(255)" ,nullable = false)
	private String password = "1111";
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "vaiTro", referencedColumnName = "maVaiTro")
	private VaiTro vaiTro;

	@Override
	public String toString() {
		return "TaiKhoan [tenTaiKhoan=" + tenTaiKhoan + ", password=" + password + ", vaiTro=" + vaiTro + "]";
	}
	
//	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@MapsId
//	@JoinColumn(name = "tenTaiKhoan")
//	private GiangVien giangVien;
	
//	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@MapsId
//	@JoinColumn(name = "tenTaiKhoan")
//	private SinhVien sinhVien;
	
	


}
