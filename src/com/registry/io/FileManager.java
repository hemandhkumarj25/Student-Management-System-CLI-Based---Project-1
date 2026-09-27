package com.registry.io;

import com.registry.model.Student;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Only class in the project that touches java.io / java.nio.file.
 * Stores one student per line as a comma-separated row.
 */
public class FileManager {

    private final Path filePath;

    public FileManager(String fileName) {
        this.filePath = Path.of(fileName);
    }

    public Map<Integer, Student> load() {
        Map<Integer, Student> students = new HashMap<>();
        if (!Files.exists(filePath)) {
            return students;
        }
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            int lineNo = 0;
            while ((line = reader.readLine()) != null) {
                lineNo++;
                if (line.isBlank()) continue;
                try {
                    Student student = Student.fromCsvRow(line);
                    students.put(student.getRollNo(), student);
                } catch (RuntimeException malformed) {
                    System.out.println("Skipping malformed row " + lineNo + " in " + filePath + ": " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read " + filePath + ": " + e.getMessage());
        }
        return students;
    }

    public void save(Collection<Student> students) {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (Student student : students) {
                writer.write(student.toCsvRow());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save to " + filePath + ": " + e.getMessage());
        }
    }
}
