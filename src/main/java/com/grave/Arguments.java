package com.grave;

public class Arguments {
    private Mode mode = Mode.NONE;
    private String ip = null;
    private int port = -1;
    
    public Arguments(String[] args)
    {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-c":
                    if (mode != Mode.NONE) {
                        System.out.println("Redundant option -c after mode was already set");
                        return;
                    }
                    mode = Mode.CLIENT;
                    break;
                case "-h":
                    if (mode != Mode.NONE) {
                        System.out.println("Redundant option -h after mode was already set");
                        return;
                    }
                    mode = Mode.HOST;
                    break;
                case "-s":
                    if (mode != Mode.NONE) {
                        System.out.println("Redundant option -s after mode was already set");
                        return;
                    }
                    mode = Mode.SERVER;
                    break;
                case "-i":
                    if (i + 1 < args.length) {
                        ip = args[++i];
                    } else {
                        throw new IllegalArgumentException("IP address not provided after -i");
                    }
                    break;
                case "-p":
                    if (i + 1 < args.length) {
                        try {
                            port = Integer.parseInt(args[++i]);
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Port must be an integer");
                        }
                    } else {
                        throw new IllegalArgumentException("Port not provided after -p");
                    }
                    break;
                default:
                    System.out.println("Unknown option: " + args[i]);
                    return;
            }
        }
    }

    public Mode getMode()
    {
        return mode;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
