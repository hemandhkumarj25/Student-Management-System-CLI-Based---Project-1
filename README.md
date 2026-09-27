# Student Management System

A simple console-based Student Management System written in plain Java (no external dependencies). Student records are kept in memory during a session and persisted to a CSV file on disk.

## Features

- Add a student (roll number, name, age, course, marks)
- View all students in a formatted table
- Search by roll number or by a name fragment
- Update any field of an existing student (blank input keeps the current value)
- Delete a student by roll number
- Class report: students ranked by marks, class average, and the topper
- Data persisted automatically to `students.csv` after every change
- Malformed rows in the CSV file are skipped with a warning instead of crashing the app

## Project structure

```
src/com/registry/
├── Main.java                          # Entry point — wires everything together
├── model/
│   └── Student.java                   # Student record + CSV (de)serialization
├── io/
│   └── FileManager.java                # Only class that touches java.io / java.nio.file
├── service/
│   ├── StudentService.java             # Business logic (add/find/update/delete/report)
│   └── StudentNotFoundException.java   # Thrown when a roll number doesn't exist
└── ui/
    └── ConsoleMenu.java                 # Interactive console menu loop
```

## Requirements

- JDK 17 or later (the code uses `switch` expressions and `Path.of`)

## Build and run

From the project root:

```bash
# Compile
javac -d out $(find src -name "*.java")   # macOS/Linux
# or on Windows PowerShell:
javac -d out (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })

# Run
java -cp out com.registry.Main
```

On first run, `students.csv` is created automatically in the working directory the first time a student is added.

## CSV format

Each line in `students.csv` represents one student:

```
rollNo,name,age,course,marks
1,Ada Lovelace,22,Computer Science,91.5
```

## License

No license specified yet — add one (e.g. MIT) if you plan to share or open-source this project.
