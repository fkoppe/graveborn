package com.grave;

public class Arguments {
    private Mode mode = Mode.NONE;
    private String ip = null;
    private int port = -1;

    private boolean cFlag = false;
    private boolean hFlag = false;
    private boolean sFlag = false;
    private boolean iFlag = false;
    private boolean pFlag = false;
    
    public Arguments(String[] args)
    {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-c":
                    cFlag = true;
                    break;
                case "-h":
                    hFlag = true;
                    break;
                case "-s":
                    sFlag = true;
                    break;
                case "-i":
                    if (i + 1 < args.length) {
                        ip = args[++i];
                    } else {
                        throw new IllegalArgumentException("Ip address not provided after -i");
                    }
                    iFlag = true;
                    break;
                case "-p":
                    if (i + 1 < args.length) {
                        try {
                            port = Integer.parseInt(args[++i]);
                        } catch (NumberFormatException exception) {
                            throw new IllegalArgumentException("Port must be an integer");
                        }
                    } else {
                        throw new IllegalArgumentException("Port not provided after -p");
                    }
                    pFlag = true;
                    break;
                default:
                    System.out.println("Unknown option: " + args[i]);
                    return;
            }
        }

        int modeFlagCount = 0;

        if (cFlag) {
            modeFlagCount++;
            mode = Mode.CLIENT;
        }
        
        if (hFlag) {
            modeFlagCount++;
            mode = Mode.HOST;
        }

        if (sFlag) {
            modeFlagCount++;
            mode = Mode.SERVER;
        }

        if(modeFlagCount > 1) {
            throw new IllegalArgumentException("Only one mode flag (c/h/s) allowed");
        }

        if (hFlag) {
            if (iFlag) {
                throw new IllegalArgumentException("Host mode does not allow -i flag");
            }

            if (pFlag) {
                throw new IllegalArgumentException("Host mode does not allow -p flag");
            }
        }
        
        if (sFlag) {
            if (iFlag) {
                throw new IllegalArgumentException("Server mode does not allow -i flag");
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
