# carrental
AAOSP Vendor Service to send notification to rental company when speed limit exceeds

# Starting the Service
Intent intent = new Intent(context, SpeedMonitorService.class);
context.startService(intent);

# Startat boot

add a service declaration to your device's init.rc
service carrental-service /system/bin/app_process32 # or app_process64 depending on your architecture
    user system
    group system
    class main


## Build Instructions (AOSP Integration)

1.  Place the code in vendor source directory (e.g., `vendor/<your_vendor>/<your_module>/`).
2.  Add the service to your device's `init.rc`
3.  build aosp
