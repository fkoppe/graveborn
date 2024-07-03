package com.grave;

import com.grave.Networking.GraveClient;
import com.grave.Networking.GraveServer;
import com.jme3.app.SimpleApplication;
import com.jme3.system.JmeContext;

public class Graveborn extends SimpleApplication {

    static private JmeContext.Type context;

    private GraveServer server = null;
    private GraveClient client = null;
    
    public static void main(String[] args) {
        Arguments arguments = new Arguments(args);

        Graveborn app = new Graveborn(arguments.getMode());
        app.start(context);
    }

    public Graveborn(Mode mode)
    {
        if(Mode.NONE == mode)
        {
            mode = ModeSelector.askForMode();
        }
        
        switch (mode) {
            case CLIENT:
                context = JmeContext.Type.Display;
                client = new GraveClient();
                break;
            case SERVER:
                context = JmeContext.Type.Headless;
                server = new GraveServer();
                break;
            case HOST:
                context = JmeContext.Type.Display;
                client = new GraveClient();
                server = new GraveServer();
                break;
            default:
                throw new RuntimeException("Unknown Error");
        }
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

