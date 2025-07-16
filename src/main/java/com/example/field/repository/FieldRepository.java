package com.example.field.repository;

import com.example.field.model.City;
import com.example.field.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {

    List<Field> findAllByIsApprovedFalse();

    List<Field> findAllByCityAndIsApprovedTrue(City city);

    List<Field> findAllByNameContainingIgnoreCaseAndIsApprovedTrue(String name);
}