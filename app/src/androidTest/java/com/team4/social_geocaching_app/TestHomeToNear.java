package com.team4.social_geocaching_app;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.widget.Button;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class TestHomeToNear extends InstrumentationTestCase {

    @MediumTest
    public void testHomeToNear() {

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
        Instrumentation.ActivityMonitor monitor2 = instrumentation.addMonitor(NearMe.class.getName(), null, false);

        // Click the maps button
        final View currentView = currentActivity.findViewById(R.id.near_me_button);
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
        assertThat(currentActivity, instanceOf(NearMe.class));


        instrumentation.removeMonitor(monitor2);
    }
}