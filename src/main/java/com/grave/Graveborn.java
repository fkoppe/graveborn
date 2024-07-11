package com.grave;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.grave.Game.GameClient;
import com.grave.Game.ObjectManager;
import com.grave.Networking.NetClient;
import com.grave.Networking.NetServer;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.bullet.PhysicsSpace;

public class Graveborn extends SimpleApplication {
    private static final Logger LOGGER = Logger.getLogger(Graveborn.class.getName());
    private static final int DEFAULT_PORT = 6143;

    private static JmeContext.Type context;

    private ObjectManager objectManager = null;

    private NetServer server = null;
    private NetClient client = null;
    //private GameClient gameClient = null;

    public Mode mode = Mode.NONE;

    private PhysicsSpace physicsSpace = null;

    public static void main(String[] args) {

        Arguments arguments = new Arguments(args);

        AppSettings settings = new AppSettings(true);
        settings.setCenterWindow(true);
        settings.setHeight(800);
        settings.setWidth(1000);

        Graveborn app = new Graveborn(arguments);
        app.setSettings(settings);
        app.setShowSettings(false);
        app.start(context);
    }

    public Graveborn(Arguments arguments)
    {
        objectManager = new ObjectManager(this);

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
                throw new RuntimeException("invalid mode");
        }

        scanner.close();

        mode = arguments.mode;
    }

    @Override
    public void simpleInitApp() {
        objectManager.init();

        switch (mode) {
            case CLIENT:
                client.init();
                break;
            case SERVER:
                server.init();
                break;
            case HOST:
                server.init();
                client.init();
                break;
            default:
                throw new RuntimeException("invalid mode");
        }

        LOGGER.log(Level.INFO, "finished initialisation");
    }

    @Override
    public void simpleUpdate(float tpf) {
        objectManager.update(tpf);

        switch (mode) {
            case CLIENT:
                client.update(tpf);
                break;
            case SERVER:
                server.update();
                break;
            case HOST:
                server.update();
                client.update(tpf);
                break;
            default:
                throw new RuntimeException("invalid mode");
        }
    }
    
    @Override
    public void destroy() {
        LOGGER.log(Level.INFO, "begin destruction");

        switch (mode) {
            case CLIENT:
                client.shutdown();
                break;
            case SERVER:
                server.shutdown();
                break;
            case HOST:
                client.shutdown();
                server.shutdown();
                break;
            default:
                throw new RuntimeException("invalid mode");
        }

        objectManager.shutdown();
    }

    public NetServer getNetServer() {
        return server;
    }

    public NetClient getNetClient() {
        return client;
    }

    public ObjectManager getObjectManager(){
        return objectManager;
    }

    public PhysicsSpace getPhysicsSpace() {
        return physicsSpace;
    }

    public void setPhysicsSpace(PhysicsSpace physicsSpace_) {
        physicsSpace = physicsSpace_;
    }
}
