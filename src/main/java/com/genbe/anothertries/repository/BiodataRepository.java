package com.genbe.anothertries.repository;

import com.genbe.anothertries.entity.Biodata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BiodataRepository extends JpaRepository<Biodata, Integer>{
	Biodata findByPersonIdPerson(Integer idPerson);
}
