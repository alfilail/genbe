package com.genbe.anothertries.repository;

import com.genbe.anothertries.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>{
	List<Person> findByNikLike(String nik);
}
