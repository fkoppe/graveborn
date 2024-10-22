package com.grave;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.grave.Game.Player;
import com.grave.Game.World;
import com.grave.Networking.Net;
import com.grave.Networking.NetClient;
import com.grave.Networking.NetServer;
import com.grave.Object.ObjectManager;
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
    private int port;

    private String ip = null;
    private String serverName = null;
    private String playerName = null;
    private Net net = null;
    private Player player = null;

    private ObjectManager objectManager;
    private World world;

    public static void main(String[] args) {

        Arguments arguments = new Arguments(args);

        AppSettings settings = new AppSettings(true);
        settings.setCenterWindow(true);
        settings.setWidth(DEFAULT_WIDTH);
        settings.setHeight(DEFAULT_HEIGHT);

        Graveborn app = new Graveborn(arguments);
        app.setSettings(settings);
        app.setShowSettings(false);
        app.setDisplayStatView(false);
        app.setDisplayFps(false);
        app.setPauseOnLostFocus(false);
        app.start(context);
    }

    public Graveborn(Arguments arguments)
    {
        Scanner scanner = new Scanner(System.in);

        if (Mode.NONE == arguments.mode) {
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

                serverName = arguments.serverName;
                port = arguments.port;
                break;
            case CLIENT:
                context = JmeContext.Type.Display;

                if (null == arguments.ip) {
                    arguments.ip = Configurator.askForIp(scanner);
                }
                if (-1 == arguments.port) {
                    arguments.port = Configurator.askForPort(scanner, DEFAULT_PORT);
                }
                if (null == arguments.playerName) {
                    arguments.playerName = Configurator.askForName(scanner, "player");
                }

                playerName = arguments.playerName;
                ip = arguments.ip;
                port = arguments.port;
                break;
            case HOST:
                context = JmeContext.Type.Display;

                if (-1 == arguments.port) {
                    arguments.port = Configurator.askForPort(scanner, DEFAULT_PORT);
                }
                if (null == arguments.serverName) {
                    arguments.serverName = Configurator.askForName(scanner, "server");
                }
                if (null == arguments.playerName) {
                    arguments.playerName = Configurator.askForName(scanner, "player");
                }

                serverName = arguments.serverName;
                playerName = arguments.playerName;
                ip = arguments.ip;
                port = arguments.port;
                break;
            case STANDALONE:
                context = JmeContext.Type.Display;
                break;
            default:
                throw new RuntimeException("invalid mode");
        }

        scanner.close();

        mode = arguments.mode;
    }

    @Override
    public void simpleInitApp() {
        switch (mode) {
            case SERVER:
                objectManager = new ObjectManager(this, true);
                world = new World(this, objectManager);
                net = new NetServer(objectManager, serverName, port);
                ip = ((NetServer) net).getIp();
                break;
            case CLIENT:
                objectManager = new ObjectManager(this, false);
                net = new NetClient(objectManager, playerName, ip, port);
                player = new Player(this, objectManager, playerName);
                break;
            case HOST:
                objectManager = new ObjectManager(this, true);
                world = new World(this, objectManager);
                net = new NetServer(objectManager, serverName, port);
                ip = ((NetServer) net).getIp();
                player = new Player(this, objectManager, playerName);
                break;
            case STANDALONE:
                objectManager = new ObjectManager(this, true);
                world = new World(this, objectManager);
                player = new Player(this, objectManager, playerName);
                break;
            default:
                throw new RuntimeException("invalid mode");
        }

        objectManager.init();

        if (null != world) {
            world.init();
        }

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

        if (null != world) {
            world.update(tpf);
        }

        if (null != player) {
            player.update(tpf);
        }

        if (null != net) {
            net.update(tpf);
        }

        switch (mode) {
            case CLIENT:
                assert(null != net);
                NetClient client = (NetClient)net;
                if(client.restart())
                {
                    destroy();
                    simpleInitApp();
                }
                break;
        }
    }

    @Override
    public void destroy() {
        LOGGER.log(Level.INFO, "begin destruction");

        if (null != net) {
            net.shutdown();
        }

        if (null != player) {
            player.shutdown();
        }

        if (null != world) {
            world.shutdown();
        }

        objectManager.shutdown();
    }
}
