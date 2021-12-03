package com.pplflw.test.pplflwtest.model;

public enum EmployeeState {
    ADDED(1),
    IN_CHECK(2),
    APPROVED(3),
    ACTIVE(4);

    private int code;

    private EmployeeState(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
