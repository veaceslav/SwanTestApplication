package com.example.slavik.swantestapplication;

/**
 * Created by slavik on 6/22/16.
 */
public class SwanExpressionsForTest {

    public static String phone_expr[] = {
            "self@wear_movement:x?delay=10$server_storage=FALSE{ANY,0}",
            "self@wear_movement:x?delay=100$server_storage=FALSE{ANY,0}",
            "self@wear_movement:x?delay=1000$server_storage=FALSE{ANY,0}",

            "self@wear_heartrate:heart_rate?delay=10$server_storage=FALSE{ANY,0}",
            "self@wear_heartrate:heart_rate?delay=100$server_storage=FALSE{ANY,0}",
            "self@wear_heartrate:heart_rate?delay=1000$server_storage=FALSE{ANY,0}",

            "self@wear_movement:x?delay=10$server_storage=FALSE{ANY,10}",
            "self@wear_movement:x?delay=100$server_storage=FALSE{ANY,10}",
            "self@wear_movement:x?delay=1000$server_storage=FALSE{ANY,10}",

            "self@wear_heartrate:heart_rate?delay=10$server_storage=FALSE{ANY,10}",
            "self@wear_heartrate:heart_rate?delay=100$server_storage=FALSE{ANY,10}",
            "self@wear_heartrate:heart_rate?delay=1000$server_storage=FALSE{ANY,10}"

    };

    public static String wear_expr[] = {
            "wear@accelerometer:x?delay=10$server_storage=FALSE{ANY,0}",
            "wear@accelerometer:x?delay=100$server_storage=FALSE{ANY,0}",
            "wear@accelerometer:x?delay=1000$server_storage=FALSE{ANY,0}",

            "wear@heartrate:heart_rate?delay=10$server_storage=FALSE{ANY,0}",
            "wear@heartrate:heart_rate?delay=100$server_storage=FALSE{ANY,0}",
            "wear@heartrate:heart_rate?delay=1000$server_storage=FALSE{ANY,0}",

            "wear@accelerometer:x?delay=10$server_storage=FALSE{ANY,10}",
            "wear@accelerometer:x?delay=100$server_storage=FALSE{ANY,10}",
            "wear@accelerometer:x?delay=1000$server_storage=FALSE{ANY,10}",

            "wear@heartrate:heart_rate?delay=10$server_storage=FALSE{ANY,10}",
            "wear@heartrate:heart_rate?delay=100$server_storage=FALSE{ANY,10}",
            "wear@heartrate:heart_rate?delay=1000$server_storage=FALSE{ANY,10}"
    };
}
