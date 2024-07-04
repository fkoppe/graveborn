package com.grave;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Configurator {

    public static Mode askForMode(Scanner scanner) {
        Mode mode = Mode.NONE;

        System.out.println("Select a mode:");
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

        return mode;
    }

    public static String askForIp(Scanner scanner) {
        String ipAddress = null;
        String ipPattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

        while (true) {
            System.out.print("Enter a valid IP address or press Enter to use 'localhost': ");
            ipAddress = scanner.nextLine();

            if (ipAddress.isEmpty()) {
                ipAddress = "127.0.0.1";
                break;
            }

            if (Pattern.matches(ipPattern, ipAddress)) {
                break;
            } else {
                System.out.println("Invalid IP address. Enter a valid IPv4 address or press Enter to use 'localhost'.");
            }
        }

        return ipAddress;
    }
    
    public static int askForPort(Scanner scanner, int defaultPort) {
        int port = -1;

        while (true) {
            System.out.print("Enter a port number (1-65535) or press Enter to use the default port (" + defaultPort + "): ");
            String input = scanner.nextLine();

            if (input.isEmpty()) {
                port = defaultPort;
                break;
            }

            try {
                port = Integer.parseInt(input);
                if (port >= 1 && port <= 65535) {
                    break;
                } else {
                    System.out.println("Invalid port number. Enter a value between 1 and 65535.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a numeric value.");
            }
        }

        return port;
    }
}
