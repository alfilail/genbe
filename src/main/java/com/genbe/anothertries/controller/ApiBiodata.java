package com.genbe.anothertries.controller;

import java.util.*;
import java.util.stream.Collectors;

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
	
	@GetMapping("/edit/{idPerson}")
	public DataDto getDtobyid(@PathVariable Integer idPerson) {
		Person person = personRepository.findById(idPerson).get();
		DataDto dataDto = new DataDto();
		dataDto.setId(person.getIdPerson());
		dataDto.setNik(person.getNik());
		dataDto.setName(person.getNama());
		dataDto.setAddress(person.getAlamat());
		dataDto.setHp(person.getBiodata().getNoHp());
		dataDto.setTgl(person.getBiodata().getTanggalLahir());
		dataDto.setTempatLahir(person.getBiodata().getTempatLahir());
		dataDto.setIdBio(person.getBiodata().getIdBiodata());
		return dataDto;
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
				statusDto.setStatus("true1");
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
		if (personRepository.findByNikLike(dataDto.getNik()).isEmpty() && 
				dataDto.getNik().length()==16 && Integer.parseInt(dlDto.getUmur()) >= 30) {
			Person person = convertToPerson(dataDto);
			personRepository.save(person);
			Biodata biodata = convertToBiodata(dataDto, person.getIdPerson());
			biodataRepository.save(biodata);
			statusDto.setStatus("true");
			statusDto.setMessage("data berhasil masuk");
			return statusDto;
		} else if (personRepository.findByNikLike(dataDto.getNik()).isEmpty() == false) {
			statusDto.setStatus("false");
			statusDto.setMessage("nik sudah ada");
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
	
	@PostMapping("/editbio")
	public StatusDto insertedit(@RequestBody DataDto dataDto) {
		StatusDto statusDto = new StatusDto();
		DataLengkapDto dlDto = new DataLengkapDto();
		dataService.getAge(dlDto, dataDto);
		if (dataDto.getNik().length()==16 && Integer.parseInt(dlDto.getUmur()) >= 30) {
			Person person = convertToPerson2(dataDto);
			personRepository.save(person);
			Biodata biodata = convertToBiodata2(dataDto, person.getIdPerson());
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
	
	@DeleteMapping("/{idPerson}")
	public void delete(@PathVariable Integer idPerson) {
		if (pendidikanRepository.findAllByPersonIdPerson(idPerson).isEmpty()==false) {
			List<Pendidikan> pendidikan = pendidikanRepository.findAllByPersonIdPerson(idPerson).stream().collect(Collectors.toList());
			pendidikanRepository.deleteAll(pendidikan);
		}
		biodataRepository.deleteById(idPerson);
		personRepository.deleteById(idPerson);
	}
	
	public DataDto convertToDataDto(Person person) {
		DataDto dataDto = new DataDto();
		dataDto.setId(person.getIdPerson());
		dataDto.setName(person.getNama());
		dataDto.setNik(person.getNik());
		dataDto.setAddress(person.getAlamat());
		dataDto.setHp(person.getBiodata().getNoHp());
		dataDto.setTempatLahir(person.getBiodata().getTempatLahir());
		dataDto.setTgl(person.getBiodata().getTanggalLahir());
		dataDto.setIdBio(person.getBiodata().getIdBiodata());
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
	
	public Person convertToPerson(DataDto dataDto) {
		Person person = new Person();
		person.setNik(dataDto.getNik());
		person.setNama(dataDto.getName());
		person.setAlamat(dataDto.getAddress());
		return person;
	}
	
	public Person convertToPerson2(DataDto dataDto) {
		Person person = new Person();
		person.setIdPerson(dataDto.getId());
		person.setNik(dataDto.getNik());
		person.setNama(dataDto.getName());
		person.setAlamat(dataDto.getAddress());
		return person;
	}
	
	public Biodata convertToBiodata(DataDto dataDto, Integer idPerson) {
		Biodata biodata = new Biodata();
		biodata.setNoHp(dataDto.getHp());
		biodata.setTanggalLahir(dataDto.getTgl());
		biodata.setTempatLahir(dataDto.getTempatLahir());
		if (personRepository.findById(idPerson).isPresent()) {
			Person person = personRepository.findById(idPerson).get();
			biodata.setPerson(person);
		}
		return biodata;
	}
	
	public Biodata convertToBiodata2(DataDto dataDto, Integer idPerson) {
		Biodata biodata = new Biodata();
		biodata.setIdBiodata(dataDto.getIdBio());
		biodata.setNoHp(dataDto.getHp());
		biodata.setTanggalLahir(dataDto.getTgl());
		biodata.setTempatLahir(dataDto.getTempatLahir());
		if (personRepository.findById(idPerson).isPresent()) {
			Person person = personRepository.findById(idPerson).get();
			biodata.setPerson(person);
		}
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
//@GetMapping("/pendidikan/{idPerson}")
//public List<PendidikanDto> getpendidikan(@PathVariable Integer idPerson) {
//	if(personRepository.findById(idPerson).isPresent()) {
//		List<Pendidikan> p = pendidikanRepository.findAllByPersonIdPerson(idPerson);
//		List<PendidikanDto> dto = p.stream().map(this::convertToPendidikanDto).collect(Collectors.toList());
//		return dto;
//	} else {
//		throw new IllegalArgumentException();
//	}
//}

//@GetMapping("/byidpend/{idpend}")
//public PendidikanDto dto(@PathVariable Integer idpend) {
//	Pendidikan pendidikan = pendidikanRepository.findById(idpend).get();
//	PendidikanDto dto = new PendidikanDto();
//	dto.setIdPendidikan(pendidikan.getIdPendidikan());
//	dto.setJenjang(pendidikan.getJenjang());
//	dto.setInstitusi(pendidikan.getInstitusi());
//	dto.setMasuk(pendidikan.getTahunmasuk());
//	dto.setLulus(pendidikan.getTahunlulus());
//	return dto;
//}
//@GetMapping("/test")
//public List<Bioperson> get() {
//	List<Bioperson> bioperson = new ArrayList<>();
//	Bioperson b = new Bioperson();
//	b.setNamadepan("nur");
//	b.setNamabelakang("alfilail");
//	b.setTempattinggal("depok");
//	bioperson.add(b);
//	return bioperson;
//}
//@PostMapping("/editdata")
//public DataDto editdata(@RequestBody DataDto dataDto) {
//	Biodata b = convertToBiodata(dataDto);
//	Person p = convertToPerson(dataDto);
//	biodataRepository.save(p.getBiodata());
//	b.setIdBiodata(Integer.parseInt(dataDto.getIdBio()));
//	p.setBiodata(b);
//	personRepository.save(p);
//	DataDto dataDtob = convertToDataDtoEdit(p);
//	return dataDtob;
//}
//@PostMapping("/data")
//public DataDto editSaveData(@RequestBody DataDto dataDto) {
//	Biodata biodata = modelMapper.map(dataDto, Biodata.class);
//	Person person = modelMapper.map(dataDto, Person.class);
//	biodata.setPerson(person);
//	personRepository.save(biodata.getPerson());
//	biodataRepository.save(biodata);
//	return dataDto;
//}
//@PostMapping("/pendidikan2/{idPerson}")
//public StatusDto insert(@RequestBody PendidikanDto pendidikanDto, @PathVariable Integer idPerson) {
//	StatusDto statusDto = new StatusDto();
//	try {
//		if (personRepository.findById(idPerson).isPresent()) {
//			Pendidikan pendidikan = convertToPendidikan(pendidikanDto, idPerson);
//			pendidikanRepository.save(pendidikan);
//			pendidikanDto.setIdPendidikan(pendidikan.getIdPendidikan());
//			statusDto.setStatus("true");
//			statusDto.setMessage("data berhasil masuk");
//		} else {
//			throw new IllegalArgumentException();
//		}
//	} catch (Exception e) {
//		statusDto.setStatus("false");
//		statusDto.setMessage("data gagal masuk");
//	}
//	return statusDto;
//}

//public DataDto convertToDataDtoEdit(Person p) {
//	DataDto dataDto = new DataDto();
//	dataDto.setIdPerson(p.getIdPerson());
//	dataDto.setNik(p.getNik());
//	dataDto.setName(p.getNama());
//	dataDto.setAddress(p.getAlamat());
//	dataDto.setHp(p.getBiodata().getNoHp());
//	dataDto.setTempatLahir(p.getBiodata().getTempatLahir());
//	dataDto.setTgl(p.getBiodata().getTanggalLahir());
//	dataDto.setIdBio(p.getBiodata().getIdBiodata().toString());
//	return dataDto;
//}
//public PendidikanDto convertToPendidikanDto(Pendidikan pendidikan) {
//PendidikanDto pendidikanDto = new PendidikanDto();
//pendidikanDto.setJenjang(pendidikan.getJenjang());
//pendidikanDto.setInstitusi(pendidikan.getInstitusi());
//pendidikanDto.setMasuk(pendidikan.getTahunmasuk());
//pendidikanDto.setLulus(pendidikan.getTahunlulus());
//return pendidikanDto;
//}