package com.genbe.anothertries.entity;

import javax.persistence.*;

@Entity
@Table(name = "pendidikan")
public class Pendidikan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pendidikan")
	private Integer idPendidikan;
	
	@ManyToOne
	@JoinColumn(name = "idperson", referencedColumnName = "id_person", nullable = false)
	private Person person;
	
	@Column(name = "jenjang", length = 10, nullable = false)
	private String jenjang;
	
	@Column(name = "institusi", length = 50, nullable = false)
	private String institusi;
	
	@Column(name = "tahunmasuk", length = 10, nullable = false)
	private String tahunmasuk;
	
	@Column(name = "tahunlulus", length = 10, nullable = false)
	private String tahunlulus;

	public Integer getIdPendidikan() {
		return idPendidikan;
	}

	public void setIdPendidikan(Integer idPendidikan) {
		this.idPendidikan = idPendidikan;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getJenjang() {
		return jenjang;
	}

	public void setJenjang(String jenjang) {
		this.jenjang = jenjang;
	}

	public String getInstitusi() {
		return institusi;
	}

	public void setInstitusi(String institusi) {
		this.institusi = institusi;
	}

	public String getTahunmasuk() {
		return tahunmasuk;
	}

	public void setTahunmasuk(String tahunmasuk) {
		this.tahunmasuk = tahunmasuk;
	}

	public String getTahunlulus() {
		return tahunlulus;
	}

	public void setTahunlulus(String tahunlulus) {
		this.tahunlulus = tahunlulus;
	}
	
	
}
