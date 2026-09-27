package com.registry;

import com.registry.io.FileManager;
import com.registry.service.StudentService;
import com.registry.ui.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
        FileManager fileManager = new FileManager("students.csv");
        StudentService service = new StudentService(fileManager);
        ConsoleMenu menu = new ConsoleMenu(service);
        menu.run();
    }
}
