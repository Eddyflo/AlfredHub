package com.alfredhub.devices;

public class SamsungCamera implements Camera {
    private boolean isRecording;
    private String name;

    public SamsungCamera(String name) {
        this.name = name;
        this.isRecording = false;
    }

    @Override
    public String getThumbnail() {
        return "[SAMSUNG] Miniatura de " + name;
    }

    @Override
    public String getLiveStream() {
        return "[SAMSUNG HD] Stream de " + name;
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