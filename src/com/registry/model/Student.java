package com.registry.model;

import java.util.Objects;

public class Student {

    private int rollNo;
    private String name;
    private int age;
    private String course;
    private double marks;

    public Student(int rollNo, String name, int age, String course, double marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.age = age;
        this.course = course;
        this.marks = marks;
    }

    public int getRollNo() {
        return rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public String toCsvRow() {
        return rollNo + "," + name + "," + age + "," + course + "," + marks;
    }

    public static Student fromCsvRow(String line) {
        String[] parts = line.split(",");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Expected 5 fields, got " + parts.length);
        }
        int rollNo = Integer.parseInt(parts[0].trim());
        String name = parts[1].trim();
        int age = Integer.parseInt(parts[2].trim());
        String course = parts[3].trim();
        double marks = Double.parseDouble(parts[4].trim());
        return new Student(rollNo, name, age, course, marks);
    }

    @Override
    public String toString() {
        return String.format("%-6d %-20s %-4d %-12s %6.2f", rollNo, name, age, course, marks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        return rollNo == ((Student) o).rollNo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rollNo);
    }
}
