package com.alfredhub.devices;

public class XiaomiCamera implements Camera {
    private boolean isRecording;
    private String name;

    public XiaomiCamera(String name) {
        this.name = name;
        this.isRecording = false;
    }

    @Override
    public String getThumbnail() {
        return "[XIAOMI] Miniatura de " + name;
    }

    @Override
    public String getLiveStream() {
        return "[XIAOMI HD] Stream de " + name;
    }

    @Override
    public void startRecording() {
        isRecording = true;
    }

    @Override
    public void stopRecording() {
        isRecording = false;
    }

    @Override
    public String getStatus() {
        return isRecording ? "GRABANDO" : "EN ESPERA";
    }

    @Override
    public String getBrand() {
        return "Xiaomi";
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