package de.rocketinternet.android.tracking.trackers;

import android.test.InstrumentationTestCase;

import java.util.Properties;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.trackers.mocks.RINewRelicTrackerMock;
import de.rocketinternet.android.tracking.utils.RIResourceUtils;

/**
 * @author alessandro.balocco
 */
public class RINewRelicTrackerTest extends InstrumentationTestCase {

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
        Properties properties = RIResourceUtils.getProperties(getInstrumentation().getContext(), propertiesFileName);
        RITrackingConfiguration.getInstance().loadPropertiesFromFile(properties);

        RINewRelicTrackerMock tracker = new RINewRelicTrackerMock();
        assertFalse(tracker.initializeTracker(getInstrumentation().getContext()));
    }

    public void testSuccessfulInitialization() {
        String propertiesFileName = "properties/valid_ri_tracking_config.properties";
        Properties properties = RIResourceUtils.getProperties(getInstrumentation().getContext(), propertiesFileName);
        RITrackingConfiguration.getInstance().loadPropertiesFromFile(properties);

        RINewRelicTrackerMock tracker = new RINewRelicTrackerMock();
        assertTrue(tracker.initializeTracker(getInstrumentation().getContext()));
    }
}
