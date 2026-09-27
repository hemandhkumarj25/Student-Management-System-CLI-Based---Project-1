package com.registry.service;

public class StudentNotFoundException extends RuntimeException {

    private final int rollNo;

    public StudentNotFoundException(int rollNo) {
        super("No student found with roll number " + rollNo);
        this.rollNo = rollNo;
    }

    public int getRollNo() {
        return rollNo;
    }
}
