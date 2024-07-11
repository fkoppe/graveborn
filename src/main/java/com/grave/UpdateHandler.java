package com.grave;

public interface UpdateHandler {
    public void init();
    public void shutdown();
    public void update(float tpf);
}
