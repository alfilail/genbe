package com.genbe.anothertries.dto;

import java.sql.Date;

public class DataDto {

	private Integer idPerson;
	private String nik;
	private String name;
	private String address;
	private String hp;
	private Date tgl;
	private String tempatLahir;
	private Integer idBio;
	
	public Integer getIdBio() {
		return idBio;
	}
	public void setIdBio(Integer idBio) {
		this.idBio = idBio;
	}
	public Integer getIdPerson() {
		return idPerson;
	}
	public void setIdPerson(Integer idPerson) {
		this.idPerson = idPerson;
	}
	public String getNik() {
		return nik;
	}
	public void setNik(String nik) {
		this.nik = nik;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getHp() {
		return hp;
	}
	public void setHp(String hp) {
		this.hp = hp;
	}
	public Date getTgl() {
		return tgl;
	}
	public void setTgl(Date tgl) {
		this.tgl = tgl;
	}
	public String getTempatLahir() {
		return tempatLahir;
	}
	public void setTempatLahir(String tempatLahir) {
		this.tempatLahir = tempatLahir;
	}
	
}
