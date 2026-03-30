package com.alfredhub.devices;

public interface Camera {
    String getThumbnail();
    String getLiveStream();
    void startRecording();
    void stopRecording();
    String getStatus();
    String getBrand();
    String getName();
    void setName(String name);
}