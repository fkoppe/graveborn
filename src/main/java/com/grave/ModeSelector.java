package com.grave;

import java.util.Scanner;

public class ModeSelector {
    public static Mode askForMode() {
        Scanner scanner = new Scanner(System.in);
        Mode mode = Mode.NONE;

        System.out.println("Please select a mode:");
        System.out.println("1. Client");
        System.out.println("2. Host");
        System.out.println("3. Server");

        while (true) {
            System.out.print("Enter the number corresponding to the mode: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    mode = Mode.CLIENT;
                    break;
                case "2":
                    mode = Mode.HOST;
                    break;
                case "3":
                    mode = Mode.SERVER;
                    break;
                default:
                    System.out.println("Invalid input. Please enter 1, 2, or 3.");
                    continue;
            }
            break;
        }

        scanner.close();

        return mode;
    }
}
