package com.alfredhub.devices;

public class SamsungLight implements Light {
    private boolean isOn;
    private int brightness;
    private String name;

    public SamsungLight(String name) {
        this.name = name;
        this.isOn = false;
        this.brightness = 0;
    }

    @Override
    public void turnOn() {
        isOn = true;
    }

    @Override
    public void turnOff() {
        isOn = false;
    }

    @Override
    public void setBrightness(int level) {
        this.brightness = Math.max(0, Math.min(100, level));
    }

    @Override
    public String getStatus() {
        return (isOn ? "ENCENDIDA" : "APAGADA") + " (" + brightness + "%)";
    }

    @Override
    public String getBrand() {
        return "Samsung";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}