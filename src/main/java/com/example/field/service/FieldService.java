package com.example.field.service;

import com.example.field.model.City;
import com.example.field.model.Field;
import com.example.user.model.User;
import com.example.web.dto.FieldCreateRequest;

import java.util.List;

public interface FieldService {

    Field createField(User owner, FieldCreateRequest fieldCreateRequest);

    List<Field> getPendingFields();

    void approveField(Long fieldId);

    List<Field> getFieldsByCity(City city);

    List<Field> searchByName(String name);

    Field getFieldById(Long fieldId);
}