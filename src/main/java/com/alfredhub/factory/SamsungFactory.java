package com.alfredhub.factory;

import com.alfredhub.devices.Camera;
import com.alfredhub.devices.Light;
import com.alfredhub.devices.SamsungCamera;
import com.alfredhub.devices.SamsungLight;

public class SamsungFactory implements DeviceFactory {
    @Override
    public Light createLight(String name) {
        return new SamsungLight(name);
    }

    @Override
    public Camera createCamera(String name) {
        return new SamsungCamera(name);
    }
}