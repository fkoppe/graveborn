package com.grave;

import java.util.Scanner;

import com.grave.Object.Objectmanager;
import com.grave.Networking.NetClient;
import com.grave.Networking.NetServer;

import com.jme3.app.SimpleApplication;
import com.jme3.system.JmeContext;

public class Graveborn extends SimpleApplication {
    static public final int DEFAULT_PORT = 6143;
    
    static private JmeContext.Type context;

    private Objectmanager objectmanager = null;

    private NetServer server = null;
    private NetClient client = null;
    
    public static void main(String[] args) {
        Arguments arguments = new Arguments(args);

        Graveborn app = new Graveborn(arguments);
        app.start(context);
    }

    public Graveborn(Arguments arguments)
    {
        objectmanager = new Objectmanager();

        Scanner scanner = new Scanner(System.in);

        if(Mode.NONE == arguments.mode)
        {
            arguments.mode = Configurator.askForMode(scanner);
        }
        
        switch (arguments.mode) {
            case CLIENT:
                context = JmeContext.Type.Display;

                if (null == arguments.ip) {
                    arguments.ip = Configurator.askForIp(scanner);
                }
                if (-1 == arguments.port) {
                    arguments.port = Configurator.askForPort(scanner, DEFAULT_PORT);
                }
                if (null == arguments.clientName)
                {
                    arguments.clientName = Configurator.askForName(scanner, "client");
                }

                client = new NetClient(this, arguments.clientName, arguments.ip, arguments.port);
                break;
            case SERVER:
                context = JmeContext.Type.Headless;

                if (-1 == arguments.port) {
                    arguments.port = Configurator.askForPort(scanner, DEFAULT_PORT);
                }
                if (null == arguments.serverName) {
                    arguments.serverName = Configurator.askForName(scanner, "server");
                }

                server = new NetServer(this, arguments.serverName, arguments.port);
                arguments.ip = server.getIp();
                break;
            case HOST:
                context = JmeContext.Type.Display;

                if (-1 == arguments.port) {
                    arguments.port = Configurator.askForPort(scanner, DEFAULT_PORT);
                }
                if (null == arguments.serverName) {
                    arguments.serverName = Configurator.askForName(scanner, "server");
                }
                
                server = new NetServer(this, arguments.serverName, arguments.port);
                arguments.ip = server.getIp();

                if (null == arguments.clientName) {
                    arguments.clientName = Configurator.askForName(scanner, "client");
                }

                client = new NetClient(this, arguments.clientName, arguments.ip, arguments.port);
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

        if (null != server) {
            server.start();
        }

        if (null != client) {
            client.start();
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

    public Objectmanager getObjectmanager()
    {
        return objectmanager;
    }

    public NetServer getServer() {
        return server;
    }

    public NetClient getClient() {
        return client;
    }
}

