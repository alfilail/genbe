package com.genbe.anothertries.repository;

import com.genbe.anothertries.entity.Pendidikan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PendidikanRepository extends JpaRepository<Pendidikan, Integer>{
	List<Pendidikan> findAllByPersonIdPerson(Integer idPerson);
	
	@Query(value = "SELECT jenjang FROM public.t_pendidikan where idperson = ?1 order by tahunlulus desc limit 1", nativeQuery = true)
	String pendidikanTerakhir(Integer idPerson);
}
