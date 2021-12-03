package com.pplflw.test.pplflwtest.model.converters;

import com.pplflw.test.pplflwtest.model.EmployeeState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class EmployeeStateConverter implements AttributeConverter<EmployeeState, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EmployeeState employeeState) {
        if (employeeState == null) {
            return null;
        }
        return employeeState.getCode();
    }

    @Override
    public EmployeeState convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }

        return Stream.of(EmployeeState.values())
                .filter(c -> c.getCode() == code)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
