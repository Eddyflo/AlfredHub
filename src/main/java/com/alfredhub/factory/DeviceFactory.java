package com.alfredhub.factory;

import com.alfredhub.devices.Camera;
import com.alfredhub.devices.Light;

public interface DeviceFactory {
    Light createLight(String name);
    Camera createCamera(String name);
}