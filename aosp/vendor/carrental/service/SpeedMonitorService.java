// vendor/aosp/carrental/service/SpeedMonitorService.java
package com.aosp.vendor.carrental.service;

import android.app.Service;
import android.car.Car;
import android.car.hardware.CarPropertyManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.automotive.vehicle.V2_0.VehicleProperty;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class SpeedMonitorService extends Service { // Extend Service
    private static final String TAG = "SpeedMonitorService";
    private User user;
    private int speedLimit;
    private CarPropertyManager carPropertyManager;
    private Handler mainHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "SpeedMonitorService created.");

        // Initialize user (you might get this from an intent or other mechanism)
        // For demonstration, I'm creating a dummy user:
        user = new User("dummyUser", "default"); // Replace with actual user retrieval logic

        speedLimit = ConfigManager.getSpeedLimit(user.getCustomerId()); // Get speed limit
        mainHandler = new Handler(Looper.getMainLooper());

        Car car = Car.createCar(this); // Use 'this' (the Service context)
        carPropertyManager = (CarPropertyManager) car.getCarManager(Car.PROPERTY_SERVICE);
        subscribeToVehicleSpeed();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null; // This is a started service, not a bound one
    }

    private void subscribeToVehicleSpeed() {
        try {
            carPropertyManager.registerCallback(new CarPropertyManager.CarPropertyEventCallback() {
                @Override
                public void onChangeEvent(CarPropertyManager.CarPropertyEvent event) {
                    if (event.getPropertyId() == VehicleProperty.VEHICLE_SPEED) {
                        float speed = (float) event.getValue();
                        onVehicleSpeedUpdate((int) speed);
                    }
                }

                @Override
                public void onErrorEvent(int propertyId, int zone) {
                    Log.e(TAG, "Error reading vehicle speed property");
                }
            }, VehicleProperty.VEHICLE_SPEED, CarPropertyManager.SENSOR_RATE_ONCHANGE);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to register callback for vehicle speed", e);
        }
    }

    public void onVehicleSpeedUpdate(int currentSpeed) {
        if (currentSpeed > speedLimit) {
            Log.i(TAG, "Speed limit exceeded for user " + user.getUserId() + ". Current speed: " + currentSpeed);

            mainHandler.post(() -> {
                Toast.makeText(this, "Speed limit exceeded! Current Speed: " + currentSpeed, Toast.LENGTH_SHORT).show(); // Use 'this'
            });

            NotificationService.sendNotification(user, "Speed limit exceeded! Current Speed: " + currentSpeed);
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "SpeedMonitorService destroyed.");
        // Unregister the callback when the service is stopped to prevent memory leaks.
        if (carPropertyManager != null) {
            try {
                carPropertyManager.unregisterCallback(yourCallback); // Replace yourCallback with the actual callback object.
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to unregister callback", e);
            }
        }
        super.onDestroy();
    }


    private CarPropertyManager.CarPropertyEventCallback yourCallback = new CarPropertyManager.CarPropertyEventCallback() {
        // Implement the callback methods here as before.
        @Override
        public void onChangeEvent(CarPropertyManager.CarPropertyEvent event) {
            if (event.getPropertyId() == VehicleProperty.VEHICLE_SPEED) {
                float speed = (float) event.getValue();
                onVehicleSpeedUpdate((int) speed);
            }
        }

        @Override
        public void onErrorEvent(int propertyId, int zone) {
            Log.e(TAG, "Error reading vehicle speed property");
        }
    };
}
