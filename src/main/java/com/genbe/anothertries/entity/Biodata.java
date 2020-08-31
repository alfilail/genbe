package com.genbe.anothertries.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "t_biodata")
public class Biodata {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_bio")
	private Integer idBiodata;
	
	@Column(name = "nohp", length = 16)
	private String noHp;
	
	@Column(name = "tanggal_lahir", nullable = false)
	private Date tanggalLahir;
	
	@Column(name = "tempat_lahir", length = 50)
	private String tempatLahir;
	
	@OneToOne
	@JoinColumn(name = "idperson", referencedColumnName = "id_person" , unique = true, nullable = false)
	private Person person;

	public Integer getIdBiodata() {
		return idBiodata;
	}

	public void setIdBiodata(Integer idBiodata) {
		this.idBiodata = idBiodata;
	}

	public String getNoHp() {
		return noHp;
	}

	public void setNoHp(String noHp) {
		this.noHp = noHp;
	}

	public Date getTanggalLahir() {
		return tanggalLahir;
	}

	public void setTanggalLahir(Date tanggalLahir) {
		this.tanggalLahir = tanggalLahir;
	}

	public String getTempatLahir() {
		return tempatLahir;
	}

	public void setTempatLahir(String tempatLahir) {
		this.tempatLahir = tempatLahir;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
}
