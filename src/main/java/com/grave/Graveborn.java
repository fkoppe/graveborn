package com.grave;

import java.util.Scanner;

import com.grave.Object.Objectmanager;
import com.grave.Networking.GraveClient;
import com.grave.Networking.GraveServer;

import com.jme3.app.SimpleApplication;
import com.jme3.system.JmeContext;

public class Graveborn extends SimpleApplication {
    static public final int DEFAULT_PORT = 6143;
    
    static private JmeContext.Type context;

    private Objectmanager objectmanager = null;

    private Mode mode;
    private String ip;
    private int port;
    private GraveServer server = null;
    private GraveClient client = null;
    
    public static void main(String[] args) {
        Arguments arguments = new Arguments(args);

        Graveborn app = new Graveborn(arguments);
        app.start(context);
    }

    public Graveborn(Arguments arguments)
    {
        objectmanager = new Objectmanager();

        mode = arguments.getMode();
        ip = arguments.getIp();
        port = arguments.getPort();

        Scanner scanner = new Scanner(System.in);

        if(Mode.NONE == mode)
        {
            mode = Configurator.askForMode(scanner);
        }
        
        switch (mode) {
            case CLIENT:
                context = JmeContext.Type.Display;

                if (null == ip) {
                    ip = Configurator.askForIp(scanner);
                }

                if (-1 == port) {
                    port = Configurator.askForPort(scanner, DEFAULT_PORT);
                }

                client = new GraveClient(ip, port);
                break;
            case SERVER:
                context = JmeContext.Type.Headless;

                if (-1 == port) {
                    port = Configurator.askForPort(scanner, DEFAULT_PORT);
                }

                server = new GraveServer(port);
                ip = server.getIp();
                break;
            case HOST:
                context = JmeContext.Type.Display;

                if (-1 == port) {
                    port = Configurator.askForPort(scanner, DEFAULT_PORT);
                }
                
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
        objectmanager.init();
 
        if (null != server) {
            server.init();
        }

        if (null != client) {
            client.init();
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        objectmanager.update();

        if (null != server) {
            server.update();
        }

        if (null != client) {
            client.update();
        }
    }
    
    public void simpleShutdown()
    {
        if (null != client) {
            client.shutdown();
        }

        if (null != server) {
            server.shutdown();
        }

        objectmanager.shutdown();
    }
}

