package com.aosp.vendor.carrental.service;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static final Map<String, Integer> speedLimits = new HashMap<>();

    static {
        speedLimits.put("default", 100); // Default speed limit
    }

    public static int getSpeedLimit(String customerId) {
        return speedLimits.getOrDefault(customerId, speedLimits.get("default"));
    }

    public static void setSpeedLimit(String customerId, int speed) {
        speedLimits.put(customerId, speed);
    }
}
