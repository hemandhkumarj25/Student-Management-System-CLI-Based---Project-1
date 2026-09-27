package com.registry.service;

import com.registry.io.FileManager;
import com.registry.model.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StudentService {

    private final Map<Integer, Student> students = new HashMap<>();
    private final FileManager fileManager;

    public StudentService(FileManager fileManager) {
        this.fileManager = fileManager;
        this.students.putAll(fileManager.load());
    }

    public void addStudent(Student student) {
        if (students.containsKey(student.getRollNo())) {
            throw new IllegalArgumentException("Roll number " + student.getRollNo() + " is already in use.");
        }
        students.put(student.getRollNo(), student);
        persist();
    }

    public List<Student> listAll() {
        List<Student> all = new ArrayList<>(students.values());
        all.sort(Comparator.comparingInt(Student::getRollNo));
        return all;
    }

    public Student find(int rollNo) {
        Student student = students.get(rollNo);
        if (student == null) {
            throw new StudentNotFoundException(rollNo);
        }
        return student;
    }

    public List<Student> searchByName(String fragment) {
        String needle = fragment.toLowerCase();
        List<Student> matches = new ArrayList<>();
        for (Student student : students.values()) {
            if (student.getName().toLowerCase().contains(needle)) {
                matches.add(student);
            }
        }
        matches.sort(Comparator.comparingInt(Student::getRollNo));
        return matches;
    }

    public void updateStudent(int rollNo, String name, Integer age, String course, Double marks) {
        Student student = find(rollNo);
        if (name != null && !name.isBlank()) student.setName(name);
        if (age != null) student.setAge(age);
        if (course != null && !course.isBlank()) student.setCourse(course);
        if (marks != null) student.setMarks(marks);
        persist();
    }

    public void deleteStudent(int rollNo) {
        if (students.remove(rollNo) == null) {
            throw new StudentNotFoundException(rollNo);
        }
        persist();
    }

    public List<Student> sortByMarksDescending() {
        List<Student> ranked = new ArrayList<>(students.values());
        ranked.sort(Comparator.comparingDouble(Student::getMarks).reversed());
        return ranked;
    }

    public double classAverage() {
        return students.values().stream()
                .mapToDouble(Student::getMarks)
                .average()
                .orElse(0.0);
    }

    public Optional<Student> topper() {
        return students.values().stream()
                .max(Comparator.comparingDouble(Student::getMarks));
    }

    public boolean isEmpty() {
        return students.isEmpty();
    }

    private void persist() {
        fileManager.save(students.values());
    }
}
