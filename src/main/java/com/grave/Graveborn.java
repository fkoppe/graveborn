package com.grave;

import java.util.Scanner;

import com.grave.Networking.GraveClient;
import com.grave.Networking.GraveServer;
import com.jme3.app.SimpleApplication;
import com.jme3.system.JmeContext;

public class Graveborn extends SimpleApplication {
    static private JmeContext.Type context;

    private GraveServer server = null;
    private GraveClient client = null;

    private String ip;
    private int port;
    
    public static void main(String[] args) {
        Arguments arguments = new Arguments(args);

        Graveborn app = new Graveborn(arguments.getMode());
        app.start(context);
    }

    public Graveborn(Mode mode)
    {
        Scanner scanner = new Scanner(System.in);

        if(Mode.NONE == mode)
        {
            mode = Configurator.askForMode(scanner);
        }
        
        switch (mode) {
            case CLIENT:
                context = JmeContext.Type.Display;

                ip = Configurator.askForIp(scanner);
                port = Configurator.askForPort(scanner);

                client = new GraveClient(ip, port);
                break;
            case SERVER:
                context = JmeContext.Type.Headless;

                port = Configurator.askForPort(scanner);

                server = new GraveServer(port);
                break;
            case HOST:
                context = JmeContext.Type.Display;

                port = Configurator.askForPort(scanner);
                
                server = new GraveServer(port);
                ip = server.getIp();

                client = new GraveClient(ip, port);
                break;
            default:
                throw new RuntimeException("Unknown Error");
        }

        scanner.close();
    }

    @Override
    public void simpleInitApp() {
        if (null != server) {
            server.init();
        }

        if (null != client) {
            client.init();
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        if(null != server)
        {
            server.update();
        }

        if (null != client) {
            client.update();
        }
    }
}

