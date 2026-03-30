package com.alfredhub.factory;

import com.alfredhub.devices.Camera;
import com.alfredhub.devices.Light;
import com.alfredhub.devices.XiaomiCamera;
import com.alfredhub.devices.XiaomiLight;

public class XiaomiFactory implements DeviceFactory {
    @Override
    public Light createLight(String name) {
        return new XiaomiLight(name);
    }

    @Override
    public Camera createCamera(String name) {
        return new XiaomiCamera(name);
    }
}