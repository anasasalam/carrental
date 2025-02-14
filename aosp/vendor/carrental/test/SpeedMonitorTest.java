package com.aosp.vendor.carrental.test;

import android.content.Context;
import com.aosp.vendor.carrental.service.ConfigManager;
import com.aosp.vendor.carrental.service.SpeedMonitorService;
import com.aosp.vendor.carrental.service.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SpeedMonitorTest {

    @Mock
    private Context mockContext;


    @Test
    public void testSpeedMonitor() {

        mockContext = mock(Context.class);

        User user1 = new User("user123", "companyA");
        User user2 = new User("user456", "companyB");

        ConfigManager.setSpeedLimit("companyA", 80);
        ConfigManager.setSpeedLimit("companyB", 120);

        SpeedMonitorService monitor1 = new SpeedMonitorService(mockContext, user1);
        SpeedMonitorService monitor2 = new SpeedMonitorService(mockContext, user2);

        monitor1.onVehicleSpeedUpdate(90); // Should trigger notification
        monitor2.onVehicleSpeedUpdate(110); // Should NOT trigger notification
        monitor2.onVehicleSpeedUpdate(130); // Should trigger notification

    }
}
