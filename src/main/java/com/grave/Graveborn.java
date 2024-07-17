package com.grave;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.grave.Game.ObjectManager;
import com.grave.Game.Player;
import com.grave.Game.World;
import com.grave.Networking.Net;
import com.grave.Networking.NetClient;
import com.grave.Networking.NetServer;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;

public class Graveborn extends SimpleApplication {
    private static final Logger LOGGER = Logger.getLogger(Graveborn.class.getName());
    private static final int DEFAULT_PORT = 6143;
    private static final int DEFAULT_WIDTH = 1280;
    private static final int DEFAULT_HEIGHT = 720;

    private static JmeContext.Type context;

    private Mode mode = Mode.NONE;

    private Net net = null;
    private Player player = null;

    private ObjectManager objectManager;
    private World world;

    public static void main(String[] args) {

        Arguments arguments = new Arguments(args);

        AppSettings settings = new AppSettings(false);
        settings.setCenterWindow(true);
        settings.setWidth(DEFAULT_WIDTH);
        settings.setHeight(DEFAULT_HEIGHT);

        Graveborn app = new Graveborn(arguments);
        app.setSettings(settings);
        app.setShowSettings(false);
        app.start(context);
    }

    public Graveborn(Arguments arguments)
    {
        objectManager = new ObjectManager(this);
        world = new World(objectManager);

        Scanner scanner = new Scanner(System.in);

        if(Mode.NONE == arguments.mode)
        {
            arguments.mode = Configurator.askForMode(scanner);
        }

        switch (arguments.mode) {
            case SERVER:
                context = JmeContext.Type.Headless;

                if (-1 == arguments.port) {
                    arguments.port = Configurator.askForPort(scanner, DEFAULT_PORT);
                }
                if (null == arguments.serverName) {
                    arguments.serverName = Configurator.askForName(scanner, "server");
                }

                net = new NetServer(objectManager, arguments.serverName, arguments.port);
                arguments.ip = ((NetServer)net).getIp();
                break;
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

                net = new NetClient(objectManager, arguments.clientName, arguments.ip, arguments.port);
                break;
            case HOST:
                throw new RuntimeException("host mode not implemented");
            case STANDALONE:
                throw new RuntimeException("standalone mode not implemented");
            default:
                throw new RuntimeException("invalid mode");
        }

        scanner.close();

        mode = arguments.mode;
    }

    @Override
    public void simpleInitApp() {
        objectManager.init();
        world.init();

        if (null != player) {
            player.init();
        }

        if(null != net)
        {
            net.init();
        }

        LOGGER.log(Level.INFO, "finished initialisation");
    }

    @Override
    public void simpleUpdate(float tpf) {
        objectManager.update(tpf);
        world.update(tpf);

        if (null != player) {
            player.update(tpf);
        }

        if (null != net) {
            net.update(tpf);
        }
    }

    @Override
    public void destroy() {
        LOGGER.log(Level.INFO, "begin destruction");

        if (null != net) {
            net.init();
        }

        if (null != player) {
            player.init();
        }

        world.shutdown();
        objectManager.shutdown();
    }
}
