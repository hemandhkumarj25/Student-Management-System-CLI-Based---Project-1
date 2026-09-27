package com.registry.ui;

import com.registry.model.Student;
import com.registry.service.StudentNotFoundException;
import com.registry.service.StudentService;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private final StudentService service;
    private final Scanner scanner = new Scanner(System.in);
    private boolean running = true;

    public ConsoleMenu(StudentService service) {
        this.service = service;
    }

    public void run() {
        System.out.println("=== Student Management System ===");
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1 -> addStudentFlow();
                case 2 -> viewAllFlow();
                case 3 -> searchFlow();
                case 4 -> updateFlow();
                case 5 -> deleteFlow();
                case 6 -> reportFlow();
                case 0 -> {
                    running = false;
                    System.out.println("Goodbye.");
                }
                default -> System.out.println("Not a valid option — try again.");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Add student");
        System.out.println("2. View all students");
        System.out.println("3. Search student");
        System.out.println("4. Update student");
        System.out.println("5. Delete student");
        System.out.println("6. Class report");
        System.out.println("0. Exit");
    }

    private void addStudentFlow() {
        System.out.println("\n-- Add student --");
        int rollNo = readInt("Roll number: ");
        String name = readNonEmpty("Name: ");
        int age = readInt("Age: ");
        String course = readNonEmpty("Course: ");
        double marks = readDouble("Marks: ");
        try {
            service.addStudent(new Student(rollNo, name, age, course, marks));
            System.out.println("Added roll number " + rollNo + ".");
        } catch (IllegalArgumentException e) {
            System.out.println("Could not add student: " + e.getMessage());
        }
    }

    private void viewAllFlow() {
        System.out.println("\n-- All students --");
        printTable(service.listAll());
    }

    private void searchFlow() {
        System.out.println("\n-- Search --");
        System.out.println("1. By roll number");
        System.out.println("2. By name");
        int mode = readInt("Choose an option: ");
        if (mode == 1) {
            int rollNo = readInt("Roll number: ");
            try {
                printTable(List.of(service.find(rollNo)));
            } catch (StudentNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } else if (mode == 2) {
            String fragment = readNonEmpty("Name contains: ");
            List<Student> matches = service.searchByName(fragment);
            if (matches.isEmpty()) {
                System.out.println("No students matched \"" + fragment + "\".");
            } else {
                printTable(matches);
            }
        } else {
            System.out.println("Not a valid option.");
        }
    }

    private void updateFlow() {
        System.out.println("\n-- Update student --");
        int rollNo = readInt("Roll number to update: ");
        try {
            Student existing = service.find(rollNo);
            System.out.println("Current record:");
            printTable(List.of(existing));
            System.out.println("Leave a field blank to keep its current value.");

            String name = readOptional("Name [" + existing.getName() + "]: ");
            Integer age = readOptionalInt("Age [" + existing.getAge() + "]: ");
            String course = readOptional("Course [" + existing.getCourse() + "]: ");
            Double marks = readOptionalDouble("Marks [" + existing.getMarks() + "]: ");

            service.updateStudent(rollNo, name, age, course, marks);
            System.out.println("Updated roll number " + rollNo + ".");
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteFlow() {
        System.out.println("\n-- Delete student --");
        int rollNo = readInt("Roll number to delete: ");
        try {
            service.deleteStudent(rollNo);
            System.out.println("Deleted roll number " + rollNo + ".");
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void reportFlow() {
        System.out.println("\n-- Class report --");
        if (service.isEmpty()) {
            System.out.println("No students on record yet.");
            return;
        }
        System.out.println("Ranked by marks:");
        printTable(service.sortByMarksDescending());
        System.out.printf("Class average: %.2f%n", service.classAverage());
        service.topper().ifPresent(top -> System.out.println("Topper: " + top.getName() + " (" + top.getMarks() + ")"));
    }

    private void printTable(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students on record yet.");
            return;
        }
        System.out.printf("%-6s %-20s %-4s %-12s %6s%n", "Roll", "Name", "Age", "Course", "Marks");
        for (Student student : students) {
            System.out.println(student);
        }
    }

    // ---- input helpers: each loops until it gets something usable ----

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("This field can't be empty.");
        }
    }

    private String readOptional(String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine().trim();
        return line.isEmpty() ? null : line;
    }

    private Integer readOptionalInt(String prompt) {
        while (true) {
            String line = readOptional(prompt);
            if (line == null) return null;
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number, or leave blank.");
            }
        }
    }

    private Double readOptionalDouble(String prompt) {
        while (true) {
            String line = readOptional(prompt);
            if (line == null) return null;
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number, or leave blank.");
            }
        }
    }
}
