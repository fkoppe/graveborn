package com.grave;

public class Arguments {
    private Mode mode = Mode.NONE;
    
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
}
