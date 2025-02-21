package com.grave.Networking;

import com.grave.Object.ObjectManager;

public abstract class Net {
    protected ObjectManager objectmanager;
    
    private String name;

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
