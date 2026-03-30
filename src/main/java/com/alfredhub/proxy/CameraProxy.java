package com.alfredhub.proxy;

import com.alfredhub.devices.Camera;
import com.alfredhub.devices.SamsungCamera;
import com.alfredhub.devices.XiaomiCamera;

public class CameraProxy implements Camera {
    private Camera realCamera;
    private String brand;
    private String location;
    private String cachedThumbnail;
    private boolean isRealActive;
    private String cameraName;

    public CameraProxy(String brand, String location) {
        this.brand = brand;
        this.location = location;
        this.cachedThumbnail = null;
        this.isRealActive = false;
        this.cameraName = "Camara Proxy";
    }

    private Camera getRealCamera() {
        if (realCamera == null) {
            if ("Samsung".equals(brand)) {
                realCamera = new SamsungCamera(cameraName);
            } else {
                realCamera = new XiaomiCamera(cameraName);
            }
            isRealActive = true;
        }
        return realCamera;
    }

    @Override
    public String getThumbnail() {
        if (cachedThumbnail == null) {
            cachedThumbnail = "[CACHE] " + brand + " - " + location;
        }
        return cachedThumbnail;
    }

    @Override
    public String getLiveStream() {
        return getRealCamera().getLiveStream() + " - " + brand + " en " + location;
    }

    @Override
    public void startRecording() {
        getRealCamera().startRecording();
    }

    @Override
    public void stopRecording() {
        if (realCamera != null) {
            realCamera.stopRecording();
        }
    }

    @Override
    public String getStatus() {
        if (realCamera != null) {
            return realCamera.getStatus();
        }
        return "INACTIVO";
    }

    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    public String getName() {
        return cameraName;
    }

    @Override
    public void setName(String name) {
        this.cameraName = name;
        if (realCamera != null) {
            realCamera.setName(name);
        }
    }
}