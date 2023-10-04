package com.bocxy.Property.Repository;


import com.bocxy.Property.Entity.SchemeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Repository
public interface SchemeDataRepo extends JpaRepository<SchemeData, Long> {

    @Query(value = "select d.v_division_code from division_office d where d.v_division_name = ?1", nativeQuery = true)
    String getDivisionCode(@RequestParam("divName") String divName);
    @Query(value = "select count(s.v_division)+1 from scheme_data s where s.v_division = ?1", nativeQuery = true)
    int getDivSchemeCount(@RequestParam("divName") String divName);

    @Query(value = "SELECT v_division_name FROM division_office ORDER BY v_division_name", nativeQuery = true)
    List<String> getAllDivision();



}