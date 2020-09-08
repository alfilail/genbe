package com.genbe.anothertries.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.genbe.anothertries.repository.BiodataRepository;
import com.genbe.anothertries.repository.PendidikanRepository;
import com.genbe.anothertries.repository.PersonRepository;
import com.genbe.anothertries.service.DataService;
import com.genbe.anothertries.dto.*;
import com.genbe.anothertries.entity.*;

@RestController
@RequestMapping("/biodata")
public class ApiBiodata {
	private final PersonRepository personRepository;
	private final BiodataRepository biodataRepository;
	private final PendidikanRepository pendidikanRepository;
	
	@Autowired
	private DataService dataService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	public ApiBiodata(PersonRepository personRepository, BiodataRepository biodataRepository, 
			PendidikanRepository pendidikanRepository) {
		this.personRepository = personRepository;
		this.biodataRepository = biodataRepository;
		this.pendidikanRepository = pendidikanRepository;
	}
	
	@GetMapping
	public List<DataLengkapDto> getDto() {
		List<Person> personList = personRepository.findAll();
		List<DataLengkapDto> dataDto = personList.stream().map(this::convertToDto).collect(Collectors.toList());
		return dataDto;
	}
	
	@GetMapping("/data")
	public List<DataDto> getdata() {
		List<Person> p = personRepository.findAll();
		List<DataDto> dto = p.stream().map(this::convertToDataDto).collect(Collectors.toList());
		return dto;
	}
	
	@GetMapping("/pendidikan/{idPerson}")
	public List<PendidikanDto> getpendidikan(@PathVariable Integer idPerson) {
		if(personRepository.findById(idPerson).isPresent()) {
			List<Pendidikan> p = pendidikanRepository.findAllByPersonIdPerson(idPerson);
			List<PendidikanDto> dto = p.stream().map(this::convertToPendidikanDto).collect(Collectors.toList());
			return dto;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	@GetMapping("/{id}")
	public DataDto getDtobyid(@PathVariable Integer id) {
		Person person = personRepository.findById(id).get();
		DataDto dataDto = new DataDto();
		dataDto.setNik(person.getNik());
		dataDto.setName(person.getNama());
		dataDto.setAddress(person.getAlamat());
		dataDto.setHp(person.getBiodata().getNoHp());
		dataDto.setTgl(person.getBiodata().getTanggalLahir());
		dataDto.setTempatLahir(person.getBiodata().getTempatLahir());
		return dataDto;
	}
	
	@GetMapping("/test")
	public List<Bioperson> get() {
		List<Bioperson> bioperson = new ArrayList<>();
		Bioperson b = new Bioperson();
		b.setNamadepan("nur");
		b.setNamabelakang("alfilail");
		b.setTempattinggal("depok");
		bioperson.add(b);
		return bioperson;
	}
	
	@GetMapping("/bynik/{nik}")
	public List<Object> get(@PathVariable String nik) {
		List<Object> object = new ArrayList<>();
		StatusDataLengkapDto statusdlDto = new StatusDataLengkapDto();
		StatusDto statusDto = new StatusDto();
		if (nik.length()==16) {
			if (personRepository.findByNikLike(nik).isEmpty() == false) {
				Person person = personRepository.findByNikLike(nik).get(0);
				DataLengkapDto dlDto = convertToDto(person);
				statusdlDto.setStatus("true");
				statusdlDto.setMessage("berhasil");
				statusdlDto.setData(dlDto);
				object.add(statusdlDto);
			} else {
				statusDto.setStatus("true");
				statusDto.setMessage("data dengan nik "+nik+" tidak ditemukan");
				object.add(statusDto);
			}
		} else {
			statusDto.setStatus("false");
			statusDto.setMessage("data gagal masuk, jumlah digit nik tidak sama dengan 16");
			object.add(statusDto);
		}
		return object;
	}
	
	@PostMapping
	public StatusDto insert(@RequestBody DataDto dataDto) {
		StatusDto statusDto = new StatusDto();
		DataLengkapDto dlDto = new DataLengkapDto();
		dataService.getAge(dlDto, dataDto);
		if (dataDto.getNik().length()==16 && Integer.parseInt(dlDto.getUmur()) >= 30) {
			Person person = convertToPerson(dataDto);
			personRepository.save(person);
			Biodata biodata = convertToBiodata(dataDto);
			biodataRepository.save(biodata);
			statusDto.setStatus("true");
			statusDto.setMessage("data berhasil masuk");
			return statusDto;
		} else if (dataDto.getNik().length()!=16 && Integer.parseInt(dlDto.getUmur()) >= 30) {
			statusDto.setStatus("false");
			statusDto.setMessage("data gagal masuk, jumlah digit nik tidak sama dengan 16");
			return statusDto;
		} else if (dataDto.getNik().length()==16 && Integer.parseInt(dlDto.getUmur()) < 30) {
			statusDto.setStatus("false");
			statusDto.setMessage("data gagal masuk, umur kurang dari 30 tahun");
			return statusDto;
		}
		statusDto.setStatus("false");
		statusDto.setMessage("data gagal masuk, jumlah digit nik tidak sama dengan 16 dan umur kurang dari 30 tahun");
		return statusDto;
	}
	
	@PostMapping("/data")
	public DataDto editSaveData(@RequestBody DataDto dataDto) {
		Biodata biodata = modelMapper.map(dataDto, Biodata.class);
		Person person = modelMapper.map(dataDto, Person.class);
		biodata.setPerson(person);
		personRepository.save(biodata.getPerson());
		biodataRepository.save(biodata);
		return dataDto;
	}
	
	@PostMapping("/{idPerson}")
	public StatusDto insert(@RequestBody List<PendidikanDto> pendidikanDto, @PathVariable Integer idPerson) {
		StatusDto statusDto = new StatusDto();
		try {
			if (personRepository.findById(idPerson).isPresent()) {
				List<Pendidikan> pendidikan = pendidikanDto.stream().map(x -> convertToPendidikan(x, idPerson)).collect(Collectors.toList());
				pendidikanRepository.saveAll(pendidikan);
				statusDto.setStatus("true");
				statusDto.setMessage("data berhasil masuk");
			} else {
				throw new IllegalArgumentException();
			}
		} catch (Exception e) {
			statusDto.setStatus("false");
			statusDto.setMessage("data gagal masuk");
		}
		return statusDto;
	}
	
	public DataDto mapToDataDto(Person person) {
		DataDto dataDto = modelMapper.map(person, DataDto.class);
		modelMapper.map(person.getBiodata(), dataDto);
		return dataDto;
	}
	
	public DataLengkapDto convertToDto(Person person) {
		DataLengkapDto dlDto = new DataLengkapDto();
		dlDto.setNik(person.getNik());
		dlDto.setName(person.getNama());
		dlDto.setAddress(person.getAlamat());
		dlDto.setHp(person.getBiodata().getNoHp());
		dlDto.setTgl(person.getBiodata().getTanggalLahir());
		dlDto.setTempatLahir(person.getBiodata().getTempatLahir());
		dataService.getAge1(dlDto);
		dlDto.setPendidikanTerakhir(pendidikanRepository.pendidikanTerakhir(person.getIdPerson()));
		return dlDto;
	}
	
	public PendidikanDto convertToPendidikanDto(Pendidikan pendidikan) {
		PendidikanDto pendidikanDto = new PendidikanDto();
		pendidikanDto.setJenjang(pendidikan.getJenjang());
		pendidikanDto.setInstitusi(pendidikan.getInstitusi());
		pendidikanDto.setMasuk(pendidikan.getTahunmasuk());
		pendidikanDto.setLulus(pendidikan.getTahunlulus());
		return pendidikanDto;
	}
	
	public DataDto convertToDataDto(Person person) {
		DataDto dataDto = new DataDto();
		dataDto.setName(person.getNama());
		dataDto.setNik(person.getNik());
		dataDto.setAddress(person.getAlamat());
		dataDto.setHp(person.getBiodata().getNoHp());
		dataDto.setTempatLahir(person.getBiodata().getTempatLahir());
		dataDto.setTgl(person.getBiodata().getTanggalLahir());
		return dataDto;
	}
	
	public Person convertToPerson(DataDto dataDto) {
		Person person = new Person();
		person.setNik(dataDto.getNik());
		person.setNama(dataDto.getName());
		person.setAlamat(dataDto.getAddress());
		return person;
	}
	
	Integer i= 0;
	public Biodata convertToBiodata(DataDto dataDto) {
		Biodata biodata = new Biodata();
		biodata.setNoHp(dataDto.getHp());
		biodata.setTanggalLahir(dataDto.getTgl());
		biodata.setTempatLahir(dataDto.getTempatLahir());
		Person person = personRepository.findAll().get(this.i);
		biodata.setPerson(person);
		this.i+=1;
		return biodata;
	}
	
	public Pendidikan convertToPendidikan(PendidikanDto pendidikanDto, Integer idPerson) {
		Pendidikan pendidikan = new Pendidikan();
		pendidikan.setJenjang(pendidikanDto.getJenjang());
		pendidikan.setInstitusi(pendidikanDto.getInstitusi());
		pendidikan.setTahunmasuk(pendidikanDto.getMasuk());
		pendidikan.setTahunlulus(pendidikanDto.getLulus());
		if (personRepository.findById(idPerson).isPresent()) {
			Person person = personRepository.findById(idPerson).get();
			pendidikan.setPerson(person);
		}
		return pendidikan;
	}
	
}
