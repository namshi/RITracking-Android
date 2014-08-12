package de.rocketinternet.android.tracking.core;

import android.test.InstrumentationTestCase;

import java.util.Properties;

import de.rocketinternet.android.tracking.utils.RIResourceUtils;

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

    public void testFailedTrackingConfigurationLoadingFromPropertyListFile() {
        String googleAnalyticsTrackingID = "RIGoogleAnalyticsTrackingID";
        String propertiesFileName = "properties/empty_ri_tracking_config.properties";
        Properties properties = RIResourceUtils.getProperties(getInstrumentation().getContext(), propertiesFileName);

        assertNull(RITrackingConfiguration.getInstance().getValueFromKeyMap(googleAnalyticsTrackingID));

        RITrackingConfiguration.getInstance().loadPropertiesFromFile(properties);
        assertNull(RITrackingConfiguration.getInstance().getValueFromKeyMap(googleAnalyticsTrackingID));
    }

    public void testSuccessfulTrackingConfigurationLoadingFromPropertyListFile() {
        String googleAnalyticsTrackingID = "RIGoogleAnalyticsTrackingID";
        String propertiesFileName = "properties/valid_ri_tracking_config.properties";
        Properties properties = RIResourceUtils.getProperties(getInstrumentation().getContext(), propertiesFileName);

        assertNull(RITrackingConfiguration.getInstance().getValueFromKeyMap(googleAnalyticsTrackingID));

        RITrackingConfiguration.getInstance().loadPropertiesFromFile(properties);
        assertEquals("validId", RITrackingConfiguration.getInstance().getValueFromKeyMap(googleAnalyticsTrackingID));
    }

    public void testResourceFileNotFound() {
        String fileName = "wrong_file_name";
        String folderToSearch = "raw";

        boolean fileIsInResources =
                RITrackingConfiguration.getInstance().isResourceAvailable(getInstrumentation().getContext(), fileName, folderToSearch);
        assertFalse(fileIsInResources);
    }

    public void testResourceFileFound() {
        String fileName = "gtm_container";
        String folderToSearch = "raw";

        boolean fileIsInResources =
                RITrackingConfiguration.getInstance().isResourceAvailable(getInstrumentation().getContext(), fileName, folderToSearch);
        assertTrue(fileIsInResources);
    }
}
