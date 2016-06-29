package com.example.slavik.swantestapplication;

/**
 * Created by slavik on 6/22/16.
 */
public class SwanExpressionsForTest {


    public static String phone_expr[] = {
            "self@wear_movement:x?delay={$delay}$server_storage=FALSE{ANY,0}",
            "self@wear_gyroscope:x?delay={$delay}$server_storage=FALSE{ANY,0}",
            "self@wear_linearacceleration:x?delay={$delay}$server_storage=FALSE{ANY,0}",
            "self@wear_gravity:x?delay={$delay}$server_storage=FALSE{ANY,0}",
            "self@wear_heartrate:heart_rate?delay={$delay}$server_storage=FALSE{ANY,0}"
    };


    public static String wear_expr[] = {
            "wear@movement:x?delay={$delay}$server_storage=FALSE{ANY,0}",
            "wear@gyroscope:x?delay={$delay}$server_storage=FALSE{ANY,0}",
            "wear@linearacceleration:x?delay={$delay}$server_storage=FALSE{ANY,0}",
            "wear@gravity:x?delay={$delay}$server_storage=FALSE{ANY,0}",
            "wear@heartrate:heart_rate?delay={$delay}$server_storage=FALSE{ANY,0}",

    };
}
