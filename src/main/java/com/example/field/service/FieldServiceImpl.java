package com.example.field.service;

import com.example.exception.DomainException;
import com.example.field.model.City;
import com.example.field.model.Field;
import com.example.field.repository.FieldRepository;
import com.example.user.model.User;
import com.example.web.dto.FieldCreateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FieldServiceImpl implements FieldService {

    private final FieldRepository fieldRepository;

    @Autowired
    public FieldServiceImpl(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    @Override
    public Field createField(User owner, FieldCreateRequest fieldCreateRequest) {
        Field field = Field.builder()
                .name(fieldCreateRequest.getName())
                .city(fieldCreateRequest.getCity())
                .locationUrl(fieldCreateRequest.getLocationUrl())
                .openHour(fieldCreateRequest.getOpenHour())
                .closeHour(fieldCreateRequest.getCloseHour())
                .imageUrl(fieldCreateRequest.getImageUrl())
                .description(fieldCreateRequest.getDescription())
                .pricePerHour(fieldCreateRequest.getPricePerHour())
                .isAvailable(false)
                .isApproved(false)
                .owner(owner)
                .build();

        Field savedField = fieldRepository.save(field);

        log.info("Field [%s] is saved by user [%s] and waits for approving.".formatted(savedField.getName(), owner.getUsername()));

        return savedField;
    }

    @Override
    public List<Field> getPendingFields() {
        return fieldRepository.findAllByIsApprovedFalse();
    }

    @Override
    public void approveField(Long fieldId) {
        Field field = getFieldById(fieldId);

        field.setApproved(true);
        field.setAvailable(true);

        fieldRepository.save(field);

        log.info("Field [%s] is approved and now is available.".formatted(field.getName()));
    }

    public Field getFieldById(Long fieldId) {
        return fieldRepository.findById(fieldId)
                .orElseThrow(() -> new DomainException("Field with id [%s] not found.".formatted(fieldId)));
    }

    @Override
    public List<Field> getFieldsByCity(City city) {
        return fieldRepository.findAllByCityAndIsApprovedTrue(city);
    }

    @Override
    public List<Field> searchByName(String name) {
        return fieldRepository.findAllByNameContainingIgnoreCaseAndIsApprovedTrue(name);
    }
}