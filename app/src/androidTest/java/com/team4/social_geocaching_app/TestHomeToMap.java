package com.team4.social_geocaching_app;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.test.*;
import android.test.suitebuilder.annotation.*;
import android.view.*;
import android.widget.*;

import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNull.*;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;

public class TestHomeToMap extends InstrumentationTestCase {

    @MediumTest
    public void testHomeToMap() {

        Instrumentation instrumentation = getInstrumentation();

        // Register we are interested in the authentication activity
        Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(HomeScreen.class.getName(), null, false);

        // Start the authentication activity as the first activity
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(instrumentation.getTargetContext(), HomeScreen.class.getName());
        instrumentation.startActivitySync(intent);

        // Wait for the Activity to start
        Activity currentActivity = getInstrumentation().waitForMonitor(monitor);
        assertThat(currentActivity, is(notNullValue()));

        // Register we are interested in the map activity...
        // this has to be done before we do something that will send us to that
        // activity
        instrumentation.removeMonitor(monitor);
        Instrumentation.ActivityMonitor monitor2 = instrumentation.addMonitor(Map.class.getName(), null, false);

        // Click the maps button
        final View currentView = currentActivity.findViewById(R.id.maps_button);
        assertThat(currentView, is(notNullValue()));
        assertThat(currentView, instanceOf(Button.class));

        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //click the save button
                currentView.performClick();
            }
        });

        currentActivity = instrumentation.waitForMonitor(monitor2);
        assertThat(currentActivity, is(notNullValue()));
        assertThat(currentActivity, instanceOf(Map.class));

        String previousScreen = "";
        Bundle b = currentActivity.getIntent().getExtras();
        if(b.containsKey("previousScreen")){
            previousScreen = b.getString("previousScreen");
        }

        assertEquals("HomeScreen", previousScreen);

        instrumentation.removeMonitor(monitor2);
    }
}