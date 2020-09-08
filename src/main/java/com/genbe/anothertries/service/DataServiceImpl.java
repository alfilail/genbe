package com.genbe.anothertries.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.genbe.anothertries.dto.DataDto;
import com.genbe.anothertries.dto.DataLengkapDto;
import com.genbe.anothertries.repository.BiodataRepository;
import com.genbe.anothertries.repository.PersonRepository;
import com.genbe.anothertries.entity.*;

import java.time.LocalDate;
import java.time.Period;

@Service
@Transactional
public class DataServiceImpl implements DataService {

	@Autowired
	private PersonRepository personRepository;
	private BiodataRepository biodataRepository;
	
	@Override
	public DataLengkapDto getAge(DataLengkapDto dlDto, DataDto dataDto) {
		java.sql.Date date = dataDto.getTgl();
		LocalDate today = LocalDate.now();
		LocalDate birth = date.toLocalDate();
		Period age = Period.between(birth, today);
		dlDto.setUmur(Integer.toString(age.getYears()));
		return dlDto;
	}
	
	@Override
	public DataLengkapDto getAge1(DataLengkapDto dlDto) {
		java.sql.Date date = dlDto.getTgl();
		LocalDate today = LocalDate.now();
		LocalDate birth = date.toLocalDate();
		Period age1 = Period.between(birth, today);
		dlDto.setUmur(Integer.toString(age1.getYears()));
		return dlDto;
	}
	
//	@Override
//	public Person saveData(Person person) {
//		biodataRepository.save(person.getBiodata());
//		personRepository.save(person);
//		return person;
//	}
}
