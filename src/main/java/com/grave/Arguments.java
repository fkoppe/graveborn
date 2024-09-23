package com.grave;

public class Arguments {
    public Mode mode = Mode.NONE;
    public String ip = null;
    public int port = -1;
    public String serverName = null;
    public String playerName = null;

    private boolean cFlag = false;
    private boolean hFlag = false;
    private boolean sFlag = false;
    private boolean saFlag = false;
    private boolean iFlag = false;
    private boolean pFlag = false;
    private boolean cnFlag = false;
    private boolean snFlag = false;
    
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
                case "-sa":
                    saFlag = true;
                    break;
                case "-i":
                    if (i + 1 < args.length) {
                        ip = args[++i];
                    } else {
                        throw new IllegalArgumentException("ip address not provided after -i");
                    }
                    iFlag = true;
                    break;
                case "-p":
                    if (i + 1 < args.length) {
                        try {
                            port = Integer.parseInt(args[++i]);
                        } catch (NumberFormatException exception) {
                            throw new IllegalArgumentException("port must be an integer");
                        }
                    } else {
                        throw new IllegalArgumentException("port not provided after -p");
                    }
                    pFlag = true;
                    break;
                case "-cn":
                    if (i + 1 < args.length) {
                        playerName = args[++i];
                    } else {
                        throw new IllegalArgumentException("client name not provided after -n");
                    }
                    cnFlag = true;
                    break;
                case "-sn":
                    if (i + 1 < args.length) {
                        serverName = args[++i];
                    } else {
                        throw new IllegalArgumentException("server name not provided after -n");
                    }
                    snFlag = true;
                    break;
                default:
                    System.out.println("unknown option: " + args[i]);
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

        if (sFlag) {
            modeFlagCount++;
            mode = Mode.STANDALONE;
        }

        if (modeFlagCount > 1) {
            throw new IllegalArgumentException("only one mode flag (c/h/s/sa) allowed");
        }
        
        if (cFlag) {
            if (snFlag) {
                throw new IllegalArgumentException("client mode does not allow -sn flag");
            }
        }

        if (hFlag) {
            if (iFlag) {
                throw new IllegalArgumentException("host mode does not allow -i flag");
            }
        }
        
        if (sFlag) {
            if (iFlag) {
                throw new IllegalArgumentException("server mode does not allow -i flag");
            }

            if (cnFlag) {
                throw new IllegalArgumentException("server mode does not allow -cn flag");
            }
        }

        if (saFlag) {
            if (iFlag) {
                throw new IllegalArgumentException("standalone mode does not allow -i flag");
            }

            if (cnFlag) {
                throw new IllegalArgumentException("standalone mode does not allow -cn flag");
            }

            if (snFlag) {
                throw new IllegalArgumentException("standalone mode does not allow -sn flag");
            }
        }

    }
}
