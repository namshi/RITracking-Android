package de.rocketinternet.android.tracking.core;

import android.test.InstrumentationTestCase;

import java.util.Properties;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.utils.FileUtils;

/**
 * @author alessandro.balocco
 */
public class RITrackingConfigurationTest extends InstrumentationTestCase {

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

    public void testTrackingConfigurationLoadingFromPropertyListFile() {

        String googleAnalyticsTrackingID = "RIGoogleAnalyticsTrackingID";
        String propertiesFileName;
        Properties properties;

        assertNull(RITrackingConfiguration.getInstance().getValueFromKeyMap(googleAnalyticsTrackingID));

        propertiesFileName = "properties/empty_ri_tracking_config.properties";
        properties = FileUtils.getProperties(getInstrumentation().getContext(), propertiesFileName);
        RITrackingConfiguration.getInstance().loadPropertiesFromFile(properties);
        assertNull(RITrackingConfiguration.getInstance().getValueFromKeyMap(googleAnalyticsTrackingID));

        propertiesFileName = "properties/valid_ri_tracking_config.properties";
        properties = FileUtils.getProperties(getInstrumentation().getContext(), propertiesFileName);
        RITrackingConfiguration.getInstance().loadPropertiesFromFile(properties);
        assertEquals("validId", RITrackingConfiguration.getInstance().getValueFromKeyMap(googleAnalyticsTrackingID));
    }
}
