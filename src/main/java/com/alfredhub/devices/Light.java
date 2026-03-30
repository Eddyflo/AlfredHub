package com.alfredhub.devices;

public interface Light {
    void turnOn();
    void turnOff();
    void setBrightness(int level);
    String getStatus();
    String getBrand();
    String getName();
    void setName(String name);
}