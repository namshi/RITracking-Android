package de.rocketinternet.android.tracking.trackers;

import android.test.InstrumentationTestCase;

import com.google.android.gms.analytics.GoogleAnalytics;

import java.util.Properties;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.utils.FileUtils;

/**
 * @author alessandro.balocco
 */
public class RIGoogleAnalyticsTrackerTest extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        RITrackingConfiguration.getInstance().clear();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        RITrackingConfiguration.getInstance().clear();
    }

    public void testFailedInitialization() {
        String propertiesFileName = "properties/empty_ri_tracking_config.properties";
        Properties properties = FileUtils.getProperties(getInstrumentation().getContext(), propertiesFileName);
        RITrackingConfiguration.getInstance().loadPropertiesFromFile(properties);

        RIGoogleAnalyticsTracker tracker = new RIGoogleAnalyticsTracker();
        assertFalse(tracker.initializeTracker(getInstrumentation().getContext()));
    }

    public void testSuccessfulInitialization() {
        String propertiesFileName = "properties/valid_ri_tracking_config.properties";
        Properties properties = FileUtils.getProperties(getInstrumentation().getContext(), propertiesFileName);
        RITrackingConfiguration.getInstance().loadPropertiesFromFile(properties);

        RIGoogleAnalyticsTracker tracker = new RIGoogleAnalyticsTracker();
        assertTrue(tracker.initializeTracker(getInstrumentation().getContext()));
    }
}
