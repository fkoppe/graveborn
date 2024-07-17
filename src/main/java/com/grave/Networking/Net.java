package com.grave.Networking;

import com.grave.Game.ObjectManager;

public abstract class Net {
    protected ObjectManager objectmanager;
    protected String name;

    public Net(ObjectManager objectmanager_, String name_)
    {
        objectmanager = objectmanager_;
        name = name_;
    }

    abstract public void init();
    abstract public void shutdown();

    abstract public void update(float tpf);

    public String getName() {
        return name;
    }
}
